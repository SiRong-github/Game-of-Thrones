package thrones.game.card;

import ch.aplu.jcardgame.Card;

public abstract class GoTCharacterDecorator extends GoTCharacter{
	
	protected GoTCharacter decoratee;
	
	public GoTCharacterDecorator(GoTCharacter decoratee) {
		this.decoratee = decoratee;
	}
	
}
