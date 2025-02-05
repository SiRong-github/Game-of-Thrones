//Shanaia

package player;

import java.util.*;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utility.*;
import card.*;

public class GoTSmartPlayer extends GoTPlayer implements GoTObserver {
	private GameOfThrones got;
	private GoTPiles gotPiles = new GoTPiles(got);

	private GoTDisposePile played = new GoTDisposePile();
	private ArrayList<Rank> unknowns = new ArrayList<>(Arrays.stream(Rank.values()).toList());

	public GoTSmartPlayer(Hand hand, int team) {
		super(hand, team);
	}

	public GoTSmartPlayer(Hand hand, GoTPiles gotPiles, GoTDisposePile disposePile, int team) {
		super(hand, team);
		gotPiles.addObserver(this);
		disposePile.addObserver(this);
	}

	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.pickACorrectSuit(isCharacter, turn);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.selectPile(pile);
		return piles.getSelectedPile();
	}

	private void updateUnknowns() {
		Card c = played.getPile().getLast();
		if (c != null && ((Suit) c.getSuit()).isMagic()) {
			unknowns.remove(c.getRank());
		}
	}

	@Override
	public void update(Object o) {
		if (o.getClass().getName().equals(GoTDisposePile.class.getName())) {
			played = (GoTDisposePile) o;
			updateUnknowns();
		}

		if (o.getClass().getName().equals(GoTPiles.class.getName())) {
			gotPiles = (GoTPiles) o;
		}
	}

	@Override
	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter) {
		setUnknowns(); //remove unknown diamonds in hand
		if (!isCharacter) {
			Iterator<Card> itr = shortListCards.listIterator();
			while (itr.hasNext()) {
				Card c = itr.next();
				if (unknowns.contains((Rank) c.getRank())) {
					itr.remove();
				}
			}
		}

		if (shortListCards.isEmpty()) {
			return Optional.empty();
		} else {
			return getRandomCard(shortListCards);
		}
	}


	@Override
	public Optional<Card> strategy() {

		int ownPile = player % 2;
		int otherPile = (player + 1) % 2;

		if (card.isPresent()) {
			int[] ownRanks = gotPiles.getPileRanks()[ownPile];
			int[] otherRanks = gotPiles.getPileRanks()[otherPile];

			if (!((Suit) card.get().getSuit()).isMagic()) {
				if (((Suit) card.get().getSuit()).isAttack() &&
						ownRanks[GoTData.ATTACK_RANK_INDEX] > otherRanks[GoTData.DEFENCE_RANK_INDEX]) {
					card = Optional.empty();
				} else {
					pile = ownPile;
				}
			} else {
				if (otherRanks[GoTData.ATTACK_RANK_INDEX] != 0 &&
						ownRanks[GoTData.DEFENCE_RANK_INDEX] <= otherRanks[GoTData.ATTACK_RANK_INDEX]) {
					pile = otherPile;
				} else {
					card = Optional.empty();
				}
			}
		} else {
			card = Optional.empty();
		}

		return card;
	}

	@Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn){
		return super.playCorrectCardPile(got, gotPiles, turn);
	}

	private void setUnknowns() {
		for (Card c : hand.getCardsWithSuit(Suit.DIAMONDS)) {
			unknowns.remove(c.getRank());
		}
	}

}

