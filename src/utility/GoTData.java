package utility;

import java.awt.Font;

import ch.aplu.jcardgame.Deck;
import ch.aplu.jgamegrid.Location;
import card.Rank;
import card.Suit;

public class GoTData {
	public static final int windowWidth = 700;
	public static final int windowHeight = 700;
	public static final int statusHeight = 30;
	
    public static final String version = "1.0";
    public static final int nbPlayers = 4;
    public static final int nbStartCards = 9;
	public static final int nbPlays = 6;
	public static final int nbRounds = 3;
    public static final int handWidth = 400;
    public static final int pileWidth = 40;
    public static final int pileSize = 2;
    
    public static int watchingTime = 5000;
    
    public static final int NON_SELECTION_VALUE = -1;
    public static final int UNDEFINED_INDEX = -1;
    public static final int ATTACK_RANK_INDEX = 0;
    public static final int DEFENCE_RANK_INDEX = 1;
    public static final int NUM_TEAMS = 2;
    
    public static Font bigFont = new Font("Arial", Font.BOLD, 36);
    public static Font smallFont = new Font("Arial", Font.PLAIN, 10);
    
    public static Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
    public static final String[] playerTeams = { "[Players 0 & 2]", "[Players 1 & 3]"};
    public static boolean[] humanPlayers = {false, false, false, false};
    
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
