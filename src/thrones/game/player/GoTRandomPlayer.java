package thrones.game.player;

import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.utility.GoTPiles;

public class GoTRandomPlayer extends GoTPlayer {

	public GoTRandomPlayer(Hand hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}
	
	public Optional<Card> getCorrectSuit(boolean isCharacter) {
		return super.pickACorrectSuit(isCharacter);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}
	
}
