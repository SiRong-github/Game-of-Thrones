//Xiaojiang
package utility;

import java.util.Properties;

import player.GoTPlayerType;

import java.io.*;
import java.util.Random;

public class GoTPropertiesLoader {
	private Properties properties = new Properties();
	private int DEFAULT_WATCH_TIME = 5000;
	
	public GoTPropertiesLoader() {
	}
	
	public GoTPropertiesLoader(String file) {
		try {
			File f = new File(file);
			System.out.println(file);
			FileInputStream ins = new FileInputStream(f);
			properties.load(ins);
		} catch (FileNotFoundException e) {
			System.err.println("Property file not found: " + file);
		} catch (IOException e) {
			System.err.println("Property loading fail");
		}
		
		GoTData.watchingTime = getWatchingTime();
		GoTUtilities.initializeRandom(getSeed());
		boolean[] humanPlayers = {getPlayerType(0) == GoTPlayerType.GOT_HUMAN,
								  getPlayerType(1) == GoTPlayerType.GOT_HUMAN,
								  getPlayerType(2) == GoTPlayerType.GOT_HUMAN,
								  getPlayerType(3) == GoTPlayerType.GOT_HUMAN,
								 };
		GoTData.humanPlayers = humanPlayers;
	}
	
	public String getProperty(String field) {
		return properties.getProperty(field);
	}
	
	public int getSeed() {
		if (getProperty("seed") != null) {
			return ((Number)Integer.parseInt(getProperty("seed"))).intValue();
		}
		return new Random().nextInt();
	}
	
	public int getWatchingTime() {
		if (properties.getProperty("watchingTime") != null) {
			return ((Number)Integer.parseInt(properties.getProperty("watchingTime"))).intValue();
		}
		return DEFAULT_WATCH_TIME;
	}
	
	/**
	 * 
	 * @return player type, default random player
	 */
	public GoTPlayerType getPlayerType(int index) {
		if (index >= 0 && index < 4){
			return GoTPlayerType.toType(properties.getProperty("players."+index));
		}
		return GoTPlayerType.GOT_RANDOM;
	}
}
