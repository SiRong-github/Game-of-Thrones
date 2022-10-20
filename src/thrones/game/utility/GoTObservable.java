package thrones.game.utility;

import java.util.ArrayList;

public abstract class GoTObservable {
	private ArrayList<GoTObserver> observers = new ArrayList<>();
	
	public GoTObservable() {
		
	}
	
	public final void addObserver(GoTObserver o) {
		observers.add(o);
	}
	
	public final void removeObserver(GoTObserver o) {
		observers.remove(o);
	}
	
	protected final void update() {
		for(GoTObserver o: observers) {
			o.update(this);
		}
	}
}
