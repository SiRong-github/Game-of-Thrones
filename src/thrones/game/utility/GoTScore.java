package thrones.game.utility;

import java.awt.Color;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.GameOfThrones;

public class GoTScore {
	
    private Actor[] scoreActors = {null, null, null, null};
    private int[] scores = new int[GoTData.nbPlayers];

    private GameOfThrones got;
    private GoTPiles gotPiles;
    
    public GoTScore(GameOfThrones got, GoTPiles gotPiles) {
    	this.gotPiles = gotPiles;
    	this.got = got;
    }

    public void initScore() {
        for (int i = 0; i < GoTData.nbPlayers; i++) {
             scores[i] = 0;
            String text = "P" + i + "-0";
            scoreActors[i] = new TextActor(text, Color.WHITE, got.bgColor, GoTData.bigFont);
            got.addActor(scoreActors[i], GoTData.scoreLocations[i]);
        }

        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < GoTData.pileSize; i++) {
            gotPiles.updatePileText(i, new TextActor(text, Color.WHITE, got.bgColor, GoTData.smallFont));
        }
    }

    public void updateScore(int player) {
        got.removeActor(scoreActors[player]);
        String text = "P" + player + "-" + scores[player];
        scoreActors[player] = new TextActor(text, Color.WHITE, got.bgColor, GoTData.bigFont);
        got.addActor(scoreActors[player], GoTData.scoreLocations[player]);
    }

    public void updateScores() {
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            updateScore(i);
        }
        System.out.println(GoTData.playerTeams[0] + " score = " + scores[0] + "; " + GoTData.playerTeams[1] + " score = " + scores[1]);
    }
    
    public void showGameResult() {
    	String text;
        if (scores[0] > scores[1]) {
            text = "Players 0 and 2 won.";
        } else if (scores[0] == scores[1]) {
            text = "All players drew.";
        } else {
            text = "Players 1 and 3 won.";
        }
        System.out.println("Result: " + text);
        got.setStatusText(text);
    }
    
    public void addScore(int playerIndex, int score) {
    	if (playerIndex >= 0 && playerIndex < scores.length) {
    		scores[playerIndex] += score;
    	}
    }
}
