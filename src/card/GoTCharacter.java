//Pratika
package card;

import ch.aplu.jcardgame.Card;

public abstract class GoTCharacter {
	protected Card card;

	public abstract int[] computeAttackAndDefend();
	public abstract int computeBaseCharacterRank();

	public GoTCharacter() {

	}
	
	// set graphics object
	public void setCard(Card card) {
		this.card = card;
	}

	public Card getCard() {
		return this.card;
	}
}
