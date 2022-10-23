package thrones.game.card;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.utility.GoTData;
import thrones.game.utility.GoTObservable;
import thrones.game.utility.GoTObserver;

import java.util.ArrayList;

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