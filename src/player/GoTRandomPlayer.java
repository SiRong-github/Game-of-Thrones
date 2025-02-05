package player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utility.*;

public class GoTRandomPlayer extends GoTPlayer {

	public GoTRandomPlayer(Hand hand, int player) {
		super(hand, player);
	}
	
	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.pickACorrectSuit(isCharacter, turn);
	}

	@Override
	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter){
		if (shortListCards.isEmpty() || (!isCharacter && GoTUtilities.getRandom().nextInt(GoTData.nbRounds) == 0)) {
			card = Optional.empty();
		} else {
			card = getRandomCard(shortListCards);
		}
		return card;
	}

	@Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn){
		return super.playCorrectCardPile(got, gotPiles, turn);
	}

}
