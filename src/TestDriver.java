import thrones.game.utility.PropertiesLoader;

public class TestDriver {
	public static void main(String[] args) {
		testPropertyLoader();
	}
	
	static void testPropertyLoader() {
		PropertiesLoader prop = new PropertiesLoader();
		assert prop.getProperty("seed") == "30006" : "seed is incorrect";
		assert prop.getProperty("watchingTime") == "5000" : "watchingTime is incorrect";
		assert prop.getProperty("player.0") == "smart" : "player.0 is incorrect";
		assert prop.getProperty("player.1") == "smart" : "player.1 is incorrect";
		assert prop.getProperty("player.2") == "simple" : "player.2 is incorrect";
		assert prop.getProperty("player.3") == "simple" : "player.3 is incorrect";
		
		prop = new PropertiesLoader("properties/got.properties");
		assert prop.getProperty("seed") == "30006" : "seed is incorrect";
		assert prop.getProperty("watchingTime") == "5000" : "watchingTime is incorrect";
		assert prop.getProperty("player.0") == "human" : "player.0 is incorrect";
		assert prop.getProperty("player.1") == "random" : "player.1 is incorrect";
		assert prop.getProperty("player.2") == "random" : "player.2 is incorrect";
		assert prop.getProperty("player.3") == "random" : "player.3 is incorrect";
		
		prop = new PropertiesLoader("properties/original.properties");
		assert prop.getProperty("seed") == "90014" : "seed is incorrect";
		assert prop.getProperty("watchingTime") == "0" : "watchingTime is incorrect";
		assert prop.getProperty("player.0") == "random" : "player.0 is incorrect";
		assert prop.getProperty("player.1") == "random" : "player.1 is incorrect";
		assert prop.getProperty("player.2") == "random" : "player.2 is incorrect";
		assert prop.getProperty("player.3") == "random" : "player.3 is incorrect";
		
		prop = new PropertiesLoader("properties/smart.properties");
		assert prop.getProperty("seed") == "30006" : "seed is incorrect";
		assert prop.getProperty("watchingTime") == "5000" : "watchingTime is incorrect";
		assert prop.getProperty("player.0") == "human" : "player.0 is incorrect";
		assert prop.getProperty("player.1") == "smart" : "player.1 is incorrect";
		assert prop.getProperty("player.2") == "simple" : "player.2 is incorrect";
		assert prop.getProperty("player.3") == "simple" : "player.3 is incorrect";
		
		System.out.println("testPropertyLoader successfully!");
	}
}
