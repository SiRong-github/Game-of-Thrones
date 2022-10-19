package thrones.game.card;

import ch.aplu.jcardgame.Card;

public abstract class GoTCharacter {
	protected Card card;
	public GoTCharacter() {
		
	}
	
	// set graphics object
	public void setCard(Card card) {
		this.card = card;
	}
	
	
}
