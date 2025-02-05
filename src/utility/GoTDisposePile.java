package utility;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import utility.GoTData;
import utility.GoTObservable;

public class GoTDisposePile extends GoTObservable{
	private Hand pile;

	public GoTDisposePile() {
		this.pile = new Hand(GoTData.deck);
		this.pile.removeAll(false);
	}

	public void addPlayed(Card card) {
		pile.insert(card, false);
		super.update();
	}

	public Hand getPile() {
		return pile;
	}


}