package thrones.game.player;

import java.util.ArrayList;

public class GoTTeam {
	private ArrayList<GoTPlayer> players = new ArrayList<>();
	public GoTTeam() {
		
	}
	
	public void addPlayer(GoTPlayer player) {
		if (!players.contains(player)) {
			players.add(player);
		}
	}
	
	public void removePlayer(GoTPlayer player) {
		if (players.contains(player)) {
			players.remove(player);
		}
	}
	
}
