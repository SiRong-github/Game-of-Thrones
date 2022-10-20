package thrones.game.utility;

import java.awt.Color;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.GameOfThrones;

public class GoTScore {
	
    private Actor[] scoreActors = {null, null, null, null};
    private int[] scores;

    
    private int nbPlayers;
    private GameOfThrones got;
    
    public GoTScore(GameOfThrones got, int nbPlayers) {
    	this.nbPlayers = nbPlayers;
    	this.scores = new int[nbPlayers];
    	this.got = got;
    }

    private void initScore() {
        for (int i = 0; i < nbPlayers; i++) {
             scores[i] = 0;
            String text = "P" + i + "-0";
            scoreActors[i] = new TextActor(text, Color.WHITE, got.bgColor, got.bigFont);
            got.addActor(scoreActors[i], GoTData.scoreLocations[i]);
        }

        String text = "Attack: 0 - Defence: 0";
        for (int i = 0; i < pileTextActors.length; i++) {
            pileTextActors[i] = new TextActor(text, Color.WHITE, got.bgColor, got.smallFont);
            addActor(pileTextActors[i], GoTData.pileStatusLocations[i]);
        }
    }

    private void updateScore(int player) {
        got.removeActor(scoreActors[player]);
        String text = "P" + player + "-" + scores[player];
        scoreActors[player] = new TextActor(text, Color.WHITE, got.bgColor, got.bigFont);
        addActor(scoreActors[player], scoreLocations[player]);
    }

    private void updateScores() {
        for (int i = 0; i < nbPlayers; i++) {
            updateScore(i);
        }
        System.out.println(playerTeams[0] + " score = " + scores[0] + "; " + playerTeams[1] + " score = " + scores[1]);
    }
}
