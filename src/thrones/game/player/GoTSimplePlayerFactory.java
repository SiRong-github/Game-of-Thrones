package thrones.game.player;

public class GoTSimplePlayerFactory {
	private static GoTSimplePlayerFactory _instance = new GoTSimplePlayerFactory();
	private GoTSimplePlayerFactory() {
		
	}
	
	public static GoTSimplePlayerFactory getInstance() {
		return _instance;
	}
	
	public GoTPlayer getPlayer(GoTPlayerType type) {
		switch(type) {
		case GOT_HUMAN:
			return new GoTHumanPlayer();
		case GOT_RANDOM:
			return new GoTRandomPlayer();
		case GOT_SIMPLE:
			return new GoTSimplePlayer();
		case GOT_SMART:
			return new GoTSmartPlayer();
		default:
			return new GoTRandomPlayer();
		}
	}
	
}
