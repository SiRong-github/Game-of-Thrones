package thrones.game.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.card.GoTMagic;
import thrones.game.card.GoTSuit;
import thrones.game.card.Rank;
import thrones.game.card.Suit;
import thrones.game.utility.GoTCardPilePair;
import thrones.game.utility.GoTObserver;
import thrones.game.utility.GoTPiles;
import thrones.game.utility.GoTUtilities;

import static ch.aplu.jcardgame.Hand.SortType.RANKPRIORITY;

public class GoTSmartPlayer extends GoTPlayer implements GoTObserver {

	private Hand pack;
	private Hand played;
	private int[] scores;
	private int turn;
	private int team;

	public GoTSmartPlayer(Hand hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}

	public Optional<Card> getCorrectSuit(boolean isCharacter) {
		return super.pickACorrectSuit(isCharacter);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		// Implement smart player strategy...
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}

	@Override
	public void update(Hand pack, Hand played, int[] scores, int turn, int team) {
		// TODO Auto-generated method stub
		this.pack = pack;
		this.played = played;
		this.scores = scores;
		this.turn = turn;
		this.team = team;
	}

	// Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int nextPlayer) {
		return super.playCorrectCardPile(got, gotPiles, nextPlayer);
	}

	// Override
	public GoTCardPilePair strategy(GoTCardPilePair cardPile, int playerNum) {
		Optional<Card> card = cardPile.getCard();
		int pileIndex = cardPile.getPileIndex();
		int ownPile = playerNum % 2;
		int otherPile = (playerNum + 1) % 2;
		Suit suit = (Suit) card.get().getSuit();
		if ((suit.isMagic() && pileIndex == ownPile) || (!suit.isMagic() && pileIndex == otherPile)) {
			cardPile = new GoTCardPilePair(Optional.empty(), 0);
		}
		return cardPile;
	}

	@Override
	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter) {

		int other;
		if (team == 0) {
			other = 1;
		} else {
			other = 0;
		}

		if (!shortListCards.isEmpty() && scores[team] < scores[other]) {
			Collections.reverse(shortListCards);
			if (turn == 1) {
				selected = Optional.of(shortListCards.get(0));
			} else {
				ArrayList<Card> unknowns = played.getCardsWithSuit(GoTSuit.MAGIC);
				Collections.reverse(unknowns);

				for (Card unknown : unknowns) {
					Rank unknownRank = (Rank) unknown.getRank();
					for (Card card : shortListCards) {
						Rank cardRank = (Rank) card.getRank();
						if (cardRank == unknownRank) {
							shortListCards.remove(card);
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
