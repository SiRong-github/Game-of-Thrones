package thrones.game.utility;

import java.awt.Color;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.TextActor;
import thrones.game.CharacterFactory;
import thrones.game.GameOfThrones;
import thrones.game.card.GoTCharacter;
import thrones.game.card.Rank;

public class GoTPiles {

    private int selectedPileIndex = GoTData.NON_SELECTION_VALUE;
    private Actor[] pileTextActors = { null, null };
    
    private Hand[] piles;

    private GameOfThrones got;

    private GoTCharacter[] correspondingCharacters;
    private CharacterFactory characterFactory;
    
    public GoTPiles(GameOfThrones got) {
    	this.got = got;
        this.characterFactory = CharacterFactory.getCharacterFactory();
    }
    
    public void waitForPileSelection() {
        selectedPileIndex = GoTData.NON_SELECTION_VALUE;
        for (Hand pile : piles) {
            pile.setTouchEnabled(true);
        }
        while(selectedPileIndex == GoTData.NON_SELECTION_VALUE) {
            CardGame.delay(100);
        }
        for (Hand pile : piles) {
            pile.setTouchEnabled(false);
        }
    }

    public int[] calculatePileRanks(int pileIndex) {
        Hand currentPile = piles[pileIndex];
        /*int i = currentPile.isEmpty() ? 0 : ((Rank) currentPile.get(0).getRank()).getRankValue();
        return new int[] { i, i };*/
        if (currentPile.isEmpty()) {
            return new int[] {0, 0};
        }
        else {
            return correspondingCharacters[pileIndex].computeAttackAndDefend();
        }
    }

    public void updatePileRankState(int pileIndex, int attackRank, int defenceRank) {
        TextActor currentPile = (TextActor) pileTextActors[pileIndex];
        got.removeActor(currentPile);
        String text = GoTData.playerTeams[pileIndex] + " Attack: " + attackRank + " - Defence: " + defenceRank;
        pileTextActors[pileIndex] = new TextActor(text, Color.WHITE, got.bgColor, GoTData.smallFont);
        got.addActor(pileTextActors[pileIndex], GoTData.pileStatusLocations[pileIndex]);
    }

    public void updatePileRanks() {
        for (int j = 0; j < piles.length; j++) {
            int[] ranks = calculatePileRanks(j);
            updatePileRankState(j, ranks[GoTData.ATTACK_RANK_INDEX], ranks[GoTData.DEFENCE_RANK_INDEX]);
        }
    }
    
    public void resetPile() {
        if (piles != null) {
            for (Hand pile : piles) {
                pile.removeAll(true);
            }
        }
        piles = new Hand[GoTData.NUM_TEAMS];
        correspondingCharacters = new GoTCharacter[GoTData.NUM_TEAMS];
        for (int i = 0; i < GoTData.NUM_TEAMS; i++) {
            piles[i] = new Hand(GoTData.deck);
            piles[i].setView(got, new RowLayout(GoTData.pileLocations[i], 8 * GoTData.pileWidth));
            piles[i].draw();
            final Hand currentPile = piles[i];
            final int pileIndex = i;
            piles[i].addCardListener(new CardAdapter() {
                public void leftClicked(Card card) {
                    selectedPileIndex = pileIndex;
                    currentPile.setTouchEnabled(false);
                }
            });
        }

        updatePileRanks();
    }

    public void decorateCharacter(int pileIndex, Card card) {
        correspondingCharacters[pileIndex] = characterFactory.decorateCharacter(correspondingCharacters[pileIndex], card);
    }

    public void getInitialPileCard(int pileIndex) {
        correspondingCharacters[pileIndex].computeBaseCharacterRank();
    }
    
    public void selectRandomPile() {
        selectedPileIndex = GoTUtilities.getRandom().nextInt(2);
    }
    
    public Hand getSelectedPile() {
    	if (selectedPileIndex >= 0 && selectedPileIndex <= piles.length) {
    		return piles[selectedPileIndex];
    	}
    	return null;
    }
    
    public int getSelectedPileIndex() {
    	return selectedPileIndex;
    }
    
    public Hand getPile(int index) {
    	if (index >= 0 && index < piles.length) {
    		return piles[index];
    	}
    	return null;
    }
    
    public void updatePileText(int pileIndex, TextActor text) {
    	if (pileIndex >=0 && pileIndex < pileTextActors.length) {
    		pileTextActors[pileIndex] = text;
    		got.addActor(pileTextActors[pileIndex], GoTData.pileStatusLocations[pileIndex]);
    	}
    }
}
