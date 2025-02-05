//Shanaia

package player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utility.*;
import card.Suit;

public class GoTSimplePlayer extends GoTPlayer {

	public GoTSimplePlayer(Hand hand, int player) {
		super(hand, player);
	}

	@Override
	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.pickACorrectSuit(isCharacter, turn);
	}

	@Override
	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter){
		if (shortListCards.isEmpty() || (!isCharacter && GoTUtilities.getRandom().nextInt(GoTData.nbRounds) == 0)) {
			return Optional.empty();
		} else {
			return getRandomCard(shortListCards);
		}
	}

	@Override
	public Optional<Card> strategy(){
		int ownPile = player % 2;
		int otherPile = (player + 1) % 2;

		Suit suit = (Suit) card.get().getSuit();
		if ((suit.isMagic() && pile == ownPile) || (!suit.isMagic() && pile == otherPile)) {
			return Optional.empty();
		}
		return card;
	}

	@Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn){
		return super.playCorrectCardPile(got, gotPiles, turn);
	}


}
