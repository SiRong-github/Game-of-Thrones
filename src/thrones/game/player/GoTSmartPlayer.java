package thrones.game.player;

import java.util.*;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.card.*;
import thrones.game.utility.*;

public class GoTSmartPlayer extends GoTPlayer implements GoTObserver {
	private GameOfThrones got;
	private GoTPiles gotPiles;
	private GoTScore scores = new GoTScore(got, gotPiles);
	private GoTDisposePile played = new GoTDisposePile();
	private List<Rank> unknowns;

	public GoTSmartPlayer(Hand hand, int team) {
		super(hand, team);
		// TODO Auto-generated constructor stub
	}

	public GoTSmartPlayer(Hand hand, GoTScore scores, GoTDisposePile disposePile, int team) {
		super(hand, team);
		scores.addObserver((GoTObserver) this);
		disposePile.addObserver((GoTObserver) this);
	}

	public List<Rank> getRanks() {
		List<Rank> unknowns = Arrays.stream(Rank.values()).toList();
		return unknowns;
	}

	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.pickACorrectSuit(isCharacter, turn);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}

	@Override
	public void update(Object o) {
		// TODO Auto-generated method stub
		if (o.getClass().getName().equals(GoTDisposePile.class.getName())) {
			played = (GoTDisposePile) o;
		}

		if (o.getClass().getName().equals(GoTScore.class.getName())) {
			scores = (GoTScore) o;
		}
	}

	// Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn) {
		return super.playCorrectCardPile(got, gotPiles, turn);
	}

	// Override
	public GoTCardPilePair strategy(GoTCardPilePair cardPile) {
		Optional<Card> card = cardPile.getCard();
		int pileIndex = cardPile.getPileIndex();
		int ownPile = player % 2;
		int otherPile = (player + 1) % 2;
		Suit suit = (Suit) card.get().getSuit();
		if ((suit.isMagic() && pileIndex == ownPile) || (!suit.isMagic() && pileIndex == otherPile)) {
			cardPile = new GoTCardPilePair(Optional.empty(), 0);
		}
		return cardPile;
	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter, int turn) {
		int own = player % 2;
		int other = (player + 1) % 2;

		Collections.sort(shortListCards);

		if (isCharacter && !shortListCards.isEmpty()) {
			selected = Optional.of(shortListCards.get(0));
		} else if (!shortListCards.isEmpty() && scores.getScore()[own] < scores.getScore()[other]) {
			if (turn == 1) {
				selected = Optional.of(shortListCards.get(0));
			} else {
				ArrayList<Card> knowns = played.getPile().getCardsWithSuit(GoTSuit.MAGIC);
				Collections.reverse(knowns);

				for (Card known : knowns) {
					Rank knownRank = (Rank) known.getRank();
					unknowns.remove(knownRank);
				}

				if (!shortListCards.isEmpty() && unknowns != null) {
					for (Card card : shortListCards) {
						Rank cardRank = (Rank) card.getRank();
						for (Rank unknown: unknowns) {
							if (cardRank == unknown) {
								shortListCards.remove(card);
							}
						}
					}
				}
				selected = Optional.of(shortListCards.get(0));
			}
		} else {
			selected = Optional.empty();
		}
		return selected;
	}

}

