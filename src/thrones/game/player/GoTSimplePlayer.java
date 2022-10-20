package thrones.game.player;

import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.utility.GoTPiles;

public class GoTSimplePlayer extends GoTPlayer {

	public GoTSimplePlayer(Hand hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}
	
	public Optional<Card> getCorrectSuit(boolean isCharacter) {
		return super.pickACorrectSuit(isCharacter);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		// Implement simple player strategy...
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}
	
}
