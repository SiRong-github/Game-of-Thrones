package thrones.game.player;

import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.utility.GoTObserver;
import thrones.game.utility.GoTPiles;

public class GoTSmartPlayer extends GoTPlayer implements GoTObserver {
	

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
	public void update(Object o) {
		// TODO Auto-generated method stub
		
	}
}
