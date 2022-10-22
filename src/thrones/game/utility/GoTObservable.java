package thrones.game.utility;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class GoTObservable {
	private ArrayList<GoTObserver> observers = new ArrayList<>();

	private Hand pack;
	private Hand played;
	private int[] scores;
	private int turn;
	private int team;

	
	public GoTObservable() {
		
	}

	public void totalCards(Hand pack) {
		this.pack = pack;
	}

	public final void addObserver(GoTObserver o) {
		observers.add(o);
	}
	
	public final void removeObserver(GoTObserver o) {
		observers.remove(o);
	}
	
	protected final void update() {
		for(GoTObserver o: observers) {
			o.update(pack, played, scores, turn, team);
		}
	}

	public void setPack(Hand pack) {
		this.pack = pack;
		notifyAll();
	}

	public void addPlayed(Card card) {
		this.played.insert(card, false);
		notifyAll();
	}

	public void setScores(int[] scores) {
		this.scores = scores;
		notifyAll();
	}

	public void setTurn(int turn) {
		this.turn = turn;
		notifyAll();
	}

	public void setTeam(int team) {
		this.team = team;
		notifyAll();
	}

}
