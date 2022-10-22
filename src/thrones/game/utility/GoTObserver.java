package thrones.game.utility;

import ch.aplu.jcardgame.Hand;

public interface GoTObserver {
	public void update(Hand pack, Hand played, int[] scores, int turn, int team);
}
