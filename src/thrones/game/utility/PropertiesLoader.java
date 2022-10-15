package thrones.game.utility;

import java.util.Properties;
import java.io.*;

public class PropertiesLoader {
	private Properties properties = new Properties();
	
	public PropertiesLoader() {
		initProperty();
	}
	
	public PropertiesLoader(String file) {
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
	
	private void initProperty() {
		properties.setProperty("seed", "30006");
		properties.setProperty("watchingTime", "5000");
		properties.setProperty("players.0", "smart");
		properties.setProperty("player.1", "smart");
		properties.setProperty("player.2", "simple");
		properties.setProperty("player.3", "simple");
	}
}
