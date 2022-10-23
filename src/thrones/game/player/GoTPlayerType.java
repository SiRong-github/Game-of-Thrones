package thrones.game.player;

public enum GoTPlayerType {
	GOT_HUMAN, GOT_RANDOM, GOT_SIMPLE, GOT_SMART;
	
	public static boolean isAIPlayer(GoTPlayerType type) {
		return type != GOT_HUMAN;
	}
	
	public static GoTPlayerType toType(String type) {
		if (type.equals("human")) {
			return GOT_HUMAN;
		} else if(type.equals("random")) {
			return GOT_RANDOM;
		} else if(type.equals("simple")) {
			return GOT_SIMPLE;
		} else if(type.equals("smart")) {
			return GOT_SMART;
		}
		return GOT_SMART;
	}
}
