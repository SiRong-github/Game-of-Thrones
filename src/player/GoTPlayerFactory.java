package player;

import ch.aplu.jcardgame.Hand;
import card.GoTDisposePile;
import utility.GoTScore;

public class GoTPlayerFactory {
	private static GoTPlayerFactory _instance = new GoTPlayerFactory();

	private GoTPlayerFactory() {
		
	}
	
	public static GoTPlayerFactory getInstance() {
		return _instance;
	}
	
	public GoTPlayer getPlayer(GoTPlayerType type, Hand hand, GoTScore score, GoTDisposePile disposePile, int team) {
		switch(type) {
		case GOT_HUMAN:
			return new GoTHumanPlayer(hand, team);
		case GOT_RANDOM:
			return new GoTRandomPlayer(hand, team);
		case GOT_SIMPLE:
			return new GoTSimplePlayer(hand, team);
		case GOT_SMART:
			return new GoTSmartPlayer(hand, score, disposePile, team);
		default:
			return new GoTRandomPlayer(hand, team);
		}
	}
	
}
