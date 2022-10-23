//Pratika
package thrones.game.card;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.utility.GoTData;

import java.awt.*;

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
