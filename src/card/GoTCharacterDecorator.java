//Pratika
package card;

import ch.aplu.jcardgame.Card;

public abstract class GoTCharacterDecorator extends GoTCharacter{
	
	protected GoTCharacter decoratee;
	
	public GoTCharacterDecorator(GoTCharacter decoratee, Card card) {
		this.decoratee = decoratee;
		setCard(card);
	}

	public GoTCharacter getDecoratee() {
		return decoratee;
	}

	@Override
	public int computeBaseCharacterRank() {
		return decoratee.computeBaseCharacterRank();
	}
}
