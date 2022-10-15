package thrones.game.controller;

public class GoTSimpleGameControllerFactory {
	private static GoTSimpleGameControllerFactory _instance = new GoTSimpleGameControllerFactory(); 
	
	private GoTSimpleGameControllerFactory() {
		
	}
	
	public static GoTSimpleGameControllerFactory getInstance() {
		return _instance;
	}
	
	public GoTGameController getGameController(String controllerName) {
		switch(controllerName) {
		case "human":
			return new GoTHumanController();
		case "random":
			return new GoTRandomAIGameController();
		case "simple":
			return new GoTSimpleAIGameController();
		case "smart":
			return new GoTSmartAIGameController();
		default:
			break;
		}
		return new GoTRandomAIGameController();
	}
}
