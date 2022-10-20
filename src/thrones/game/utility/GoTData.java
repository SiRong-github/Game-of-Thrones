package thrones.game.utility;

import ch.aplu.jgamegrid.Location;

public class GoTData {
    public static final String version = "1.0";
    public static final int nbPlayers = 4;
    public static final int nbStartCards = 9;
	public static final int nbPlays = 6;
	public static final int nbRounds = 3;
    public static final int handWidth = 400;
    public static final int pileWidth = 40;
    
    public static final Location[] handLocations = {
            new Location(350, 625),
            new Location(75, 350),
            new Location(350, 75),
            new Location(625, 350)
    };


    public static final Location[] pileLocations = {
            new Location(350, 280),
            new Location(350, 430)
    };
    public static final Location[] pileStatusLocations = {
            new Location(250, 200),
            new Location(250, 520)
    };
    public static final Location[] scoreLocations = {
            new Location(575, 675),
            new Location(25, 575),
            new Location(25, 25),
            new Location(575, 125)
    };
}
