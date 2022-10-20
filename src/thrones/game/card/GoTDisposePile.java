package thrones.game.card;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.utility.GoTData;
import thrones.game.utility.GoTObservable;

public class GoTDisposePile extends GoTObservable{
	private Hand pile;
	public GoTDisposePile() {
		this.pile = new Hand(GoTData.deck);
		this.pile.removeAll(false);
	}
	
	public void dispose(Card card) {
		pile.insert(card, false);
		super.update();
	}
}
