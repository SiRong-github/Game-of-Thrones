package thrones.game.player;

import ch.aplu.jcardgame.Hand;

public class GoTSimplePlayerFactory {
	private static GoTSimplePlayerFactory _instance = new GoTSimplePlayerFactory();
	private GoTSimplePlayerFactory() {
		
	}
	
	public static GoTSimplePlayerFactory getInstance() {
		return _instance;
	}
	
	public GoTPlayer getPlayer(GoTPlayerType type, Hand hand) {
		switch(type) {
		case GOT_HUMAN:
			return new GoTHumanPlayer(hand);
		case GOT_RANDOM:
			return new GoTRandomPlayer(hand);
		case GOT_SIMPLE:
			return new GoTSimplePlayer(hand);
		case GOT_SMART:
			return new GoTSmartPlayer(hand);
		default:
			return new GoTRandomPlayer(hand);
		}
	}
	
}
