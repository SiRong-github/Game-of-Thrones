package thrones.game.card;

import java.util.LinkedList;
public class GoTHand {
	private LinkedList<GoTCharacter> cards = new LinkedList<>();
	
	public GoTHand() {
		
	}
	
	public void add(GoTCharacter card) {
		if (!cards.contains(card)) {
			cards.add(card);
		}
	}
	
	public void remove(GoTCharacter card) {
		if (cards.contains(card)) {
			cards.remove(card);
		}
	}
}
