package thrones.game.utility;

import java.util.Properties;

import thrones.game.player.GoTPlayerType;

import java.io.*;

public class GoTPropertiesLoader {
	private Properties properties = new Properties();
	
	public GoTPropertiesLoader() {
		initProperty();
	}
	
	public GoTPropertiesLoader(String file) {
		initProperty();
		try {
			File f = new File(file);
			FileInputStream ins = new FileInputStream(f);
				properties.load(ins);
		} catch (FileNotFoundException e) {
			System.err.println("Property file not found: " + file);
		} catch (IOException e) {
			System.err.println("Property loading fail");
		}
	}
	
	public String getProperty(String field) {
		return properties.getProperty(field);
	}
	
	public int getSeed() {
		return ((Number)Integer.parseInt(getProperty("seed"))).intValue();
	}
	
	/**
	 * 
	 * @return player type, default random player
	 */
	public GoTPlayerType getPlayerType(int index) {
		if (index >= 0 && index <= 4){
			return GoTPlayerType.toType("player."+index);
		}
		return GoTPlayerType.GOT_RANDOM;
	}
	
	
	private void initProperty() {
		properties.setProperty("seed", "30006");
		properties.setProperty("watchingTime", "5000");
		properties.setProperty("players.0", "smart");
		properties.setProperty("player.1", "smart");
		properties.setProperty("player.2", "simple");
		properties.setProperty("player.3", "simple");
	}
}
