package thrones.game.player;

import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.utility.GoTPiles;

public class GoTHumanPlayer extends GoTPlayer {

	public GoTHumanPlayer(Hand hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}
	
	// Override
	public Optional<Card> getCorrectSuit(boolean isCharacter) {
		return super.waitForCorrectSuit(isCharacter);
	}
	
	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.waitForPileSelection();
		return piles.getSelectedPile();
	}
	
}
