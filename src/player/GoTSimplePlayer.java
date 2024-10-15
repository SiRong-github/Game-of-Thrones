//Shanaia

package player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utility.GameOfThrones;
import card.Suit;
import utility.GoTCardPilePair;
import utility.GoTPiles;
import utility.GoTUtilities;

public class GoTSimplePlayer extends GoTPlayer {

	public GoTSimplePlayer(Hand hand, int player) {
		super(hand, player);
	}
	
	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.pickACorrectSuit(isCharacter, turn);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter, int turn){
		System.out.println("isChar " + isCharacter);
		if (shortListCards.isEmpty() || (!isCharacter && GoTUtilities.getRandom().nextInt(3) == 0)) {
			return Optional.empty();
		} else {
			return Optional.of(shortListCards.get(GoTUtilities.getRandom().nextInt(shortListCards.size())));
		}
	}

	// Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn){
		return super.playCorrectCardPile(got, gotPiles, turn);
	}

	// Override
	public GoTCardPilePair strategy(GoTCardPilePair cardPile){
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

}
