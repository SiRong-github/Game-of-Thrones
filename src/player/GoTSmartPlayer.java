//Shanaia

package player;

import java.util.*;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utility.*;
import card.*;

public class GoTSmartPlayer extends GoTPlayer implements GoTObserver {
	private GameOfThrones got;
	private GoTPiles gotPiles;
	private GoTScore scores = new GoTScore(got, gotPiles);
	private GoTDisposePile played = new GoTDisposePile();
	private List<Rank> unknowns = Arrays.stream(Rank.values()).toList();

	public GoTSmartPlayer(Hand hand, int team) {
		super(hand, team);
	}

	public GoTSmartPlayer(Hand hand, GoTScore scores, GoTDisposePile disposePile, int team) {
		super(hand, team);
		scores.addObserver((GoTObserver) this);
		disposePile.addObserver((GoTObserver) this);
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

//	protected void sortCards(List<Card> cards) {
//		List<Card> sorted = new ArrayList<>();
//		HashMap<Character, Character> map = new HashMap<>();
//		for (Card c : cards) {
//			map.put(c.getRank(), c.getSuit());
//		}
//
//
//	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter, int turn) {
		int own = player % 2;
		int other = (player + 1) % 2;

		for (Card c : shortListCards) {
			System.out.println("c " + c + " rank " + c.getRank() + " suit " + c.getSuit());
		}

		Collections.sort(shortListCards);

		if (!shortListCards.isEmpty() && scores.getScore()[own] < scores.getScore()[other] && turn != 1) {
			ArrayList<Card> knowns = played.getPile().getCardsWithSuit(GoTSuit.MAGIC);
			Collections.reverse(knowns);

			System.out.println("Unknowns: " + unknowns);
			for (Rank unknown : unknowns) {
				System.out.println(unknown);
			}

			for (Card known : knowns) {
				Rank knownRank = (Rank) known.getRank();
				unknowns.remove(knownRank);
			}

			Iterator itr = shortListCards.listIterator();
			while (itr.hasNext() && !unknowns.isEmpty()) {
				Card c = (Card) itr.next();
				for (Rank unknown : unknowns) {
					if (c.getRank().equals(unknown)) {
						itr.remove();
					}
				}
			}
		}

		if (shortListCards.isEmpty()) {
			return Optional.empty();
		} else {
			return Optional.of(shortListCards.get(0));
		}

	}

}

