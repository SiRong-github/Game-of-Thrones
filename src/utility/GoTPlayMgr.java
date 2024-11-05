//Xiaojiang
package utility;

import java.util.ArrayList;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import card.GoTDisposePile;
import card.Suit;
import ch.aplu.jcardgame.Hand;
import player.GoTPlayer;

public class GoTPlayMgr {
	private int currentPlay = 0;
	private GoTPiles gotPiles;
	private int nextStartingPlayer;
	private ArrayList<GoTPlayer> players;
	private GameOfThrones got;
	private GoTDisposePile disposePile;
	private GoTScore gotScore;
    private GoTPlayer player;
    private String character0Result;
    private String character1Result;
	
	public GoTPlayMgr(GameOfThrones got, GoTPiles gotPiles, GoTDisposePile disposePile, ArrayList<GoTPlayer> players, GoTScore gotScore) {
		this.got = got;
		this.gotPiles = gotPiles;
		this.players = players;
		this.disposePile = disposePile;
		this.gotScore = gotScore;
		
		nextStartingPlayer = GoTUtilities.getRandom().nextInt(GoTData.nbPlayers);
	}

    /**
     * Checks if the game has ended
     * @return true if game has ended
     */
	public boolean isGameEnd() {
		return this.currentPlay >= GoTData.nbPlays;
	}

    /**
     * Set starting player
     * @return
     */
    private void setStartingPlayerIndex() {
        player = this.players.get(nextStartingPlayer);
        //Change starting player if current player has no hearts
        if (player.getHand().getNumberOfCardsWithSuit(Suit.HEARTS) == 0) {
            nextStartingPlayer = GoTUtilities.getPlayerIndex(nextStartingPlayer + 1);
            player = this.players.get(nextStartingPlayer);
        }
        assert player.getHand().getNumberOfCardsWithSuit(Suit.HEARTS) != 0 : " Starting player has no hearts.";
    }

    /**
     * Display card moving towards target hand in GUI
     * @param card
     * @param targetHand
     */
    private void displayCard(Card card, Hand targetHand) {
        card.setVerso(false); // displays card face (front) instead of card cover
        card.transfer(targetHand, true); // transfer to pile (includes graphic effect)
    }

    /**
     * Update team and dispose piles
     * @param card
     */
    private void updatePiles(Card card) {
        gotPiles.updatePileRanks();
        disposePile.addPlayed(card);
    }

    /**
     * Plays card on GUI
     * @param playerIndex
     * @param pileIndex
     * @param card
     */
    private void playCard(int playerIndex, int pileIndex, Card card) {
        System.out.println("Player " + playerIndex + " plays " + GoTUtilities.canonical(card) + " on pile " + pileIndex);

        //Display card on GUI
        displayCard(card, gotPiles.getPile(pileIndex));

        //Decorate character
        gotPiles.decorateCharacter(pileIndex, card);

        //Update team and dispose piles
        updatePiles(card);
    }

    /**
     * Chooses 2 players from different teams to play heart card
     */
    private void playHearts() {
        for (int i = 0; i < 2; i++) {

            //Current player
            int playerIndex = GoTUtilities.getPlayerIndex(nextStartingPlayer + i);
            got.setStatusText("Player " + playerIndex + " select a Heart card to play");
            player = this.players.get(playerIndex);

            //Let player play heart card
            Optional<Card> selected = player.getCorrectSuit(true, 3);
            assert selected.isPresent() : " Pass returned on selection of character.";

            //Play card on pile
            System.out.println("selected " + selected);
            playCard(playerIndex, playerIndex % 2, selected.get());
        }
    }

    /**
     * Play effect cards
     */
    private void playEffects() {
        int remainingTurns = GoTData.nbPlayers * GoTData.nbRounds - 2;
        int nextPlayer = nextStartingPlayer + 2;

        while(remainingTurns > 0) {

            //Curr player
            nextPlayer = GoTUtilities.getPlayerIndex(nextPlayer);
            got.setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");
            player = this.players.get(nextPlayer);

            //Get pile and selected card of curr player
            GoTCardPilePair cardPile = player.getCorrectCardPile(got, gotPiles, remainingTurns);
            Optional<Card> selected = cardPile.getCard();

            //Play card if it exists, else pass
            if (selected.isPresent()) {
                got.setStatusText("Selected: " + GoTUtilities.canonical(selected.get()) + ". Player" + nextPlayer + " select a pile to play the card.");
                playCard(nextPlayer, cardPile.getPileIndex(), selected.get());
            } else {
                System.out.println("Player " + nextPlayer + " Pass");
                got.setStatusText("Pass.");
            }
            nextPlayer++;
            remainingTurns--;
        }
    }

    /**
     * Update score of first team
     * @param pile0Ranks
     * @param pile1Ranks
     * @param pile1CharacterRank
     * @return string stating whether attack has succeeded
     */
    private String team0Result(int[] pile0Ranks, int[] pile1Ranks, int pile1CharacterRank) {
        if (pile0Ranks[GoTData.ATTACK_RANK_INDEX] > pile1Ranks[GoTData.DEFENCE_RANK_INDEX]) {
            gotScore.addScore(0, pile1CharacterRank);
            gotScore.addScore(2, pile1CharacterRank);
            return "Character 0 attack on character 1 succeeded.";
        } else {
            gotScore.addScore(1, pile1CharacterRank);
            gotScore.addScore(3, pile1CharacterRank);
            return "Character 0 attack on character 1 failed.";
        }
    }

    /**
     * Update score of second team
     * @param pile0Ranks
     * @param pile1Ranks
     * @param pile0CharacterRank
     * @return string stating whether attack has succeeded
     */
    private String team1Result(int[] pile0Ranks, int[] pile1Ranks, int pile0CharacterRank) {
        if (pile1Ranks[GoTData.ATTACK_RANK_INDEX] > pile0Ranks[GoTData.DEFENCE_RANK_INDEX]) {
            gotScore.addScore(1, pile0CharacterRank);
            gotScore.addScore(3, pile0CharacterRank);
            return "Character 1 attack on character 0 succeeded.";
        } else {
            gotScore.addScore(0, pile0CharacterRank);
            gotScore.addScore(2, pile0CharacterRank);
            return "Character 1 attack character 0 failed.";
        }
    }

    /**
     * Play round
     */
    public void nextPlay() {
        //Update number of rounds
        currentPlay++;

        //Reset pile for new game
    	gotPiles.resetPile();

        //Set starting player index
        nextStartingPlayer = GoTUtilities.getPlayerIndex(nextStartingPlayer);
        setStartingPlayerIndex();

        // 1: play the first 2 hearts
        playHearts();

        // 2: play the remaining nbPlayers * nbRounds - 2
        playEffects();

        // 3: calculate winning & update scores for players
        gotPiles.updatePileRanks();
        int[] pile0Ranks = gotPiles.calculatePileRanks(0);
        int[] pile1Ranks = gotPiles.calculatePileRanks(1);
        System.out.println("piles[0]: " + GoTUtilities.canonical(gotPiles.getPile(0)));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[GoTData.ATTACK_RANK_INDEX] + " - Defence: " + pile0Ranks[GoTData.DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + GoTUtilities.canonical(gotPiles.getPile(1)));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[GoTData.ATTACK_RANK_INDEX] + " - Defence: " + pile1Ranks[GoTData.DEFENCE_RANK_INDEX]);

        //Update scores of the 2 teams
        int pile0CharacterRank = gotPiles.getInitialPileCard(0);
        int pile1CharacterRank = gotPiles.getInitialPileCard(1);
        character0Result = team0Result(pile0Ranks, pile1Ranks, pile1CharacterRank);
        character1Result = team1Result(pile0Ranks, pile1Ranks, pile0CharacterRank);

        //Update scores of the 2 teams in GUI
        gotScore.updateScores();
        System.out.println(character0Result);
        System.out.println(character1Result);
        got.setStatusText(character0Result + " " + character1Result);

        // 5: discarded all cards on the piles
        nextStartingPlayer++;
        CardGame.delay(GoTData.watchingTime);
        
        gotScore.updateScores();
        
        if (isGameEnd()) {
        	gotScore.showGameResult();
        }
    }
}
