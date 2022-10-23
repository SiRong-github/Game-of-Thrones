//Xiaojiang
package thrones.game.utility;

import java.util.ArrayList;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import thrones.game.GameOfThrones;
import thrones.game.card.GoTDisposePile;
import thrones.game.card.Suit;
import thrones.game.player.GoTPlayer;

public class GoTPlayMgr {
	private int currentPlay = 0;
	private GoTPiles gotPiles;
	private int nextStartingPlayer;
	private ArrayList<GoTPlayer> players;
	private GameOfThrones got;
	private GoTDisposePile disposePile;
	private GoTScore gotScore;
	
	public GoTPlayMgr(GameOfThrones got, GoTPiles gotPiles, GoTDisposePile disposePile, ArrayList<GoTPlayer> players, GoTScore gotScore) {
		this.got = got;
		this.gotPiles = gotPiles;
		this.players = players;
		this.disposePile = disposePile;
		this.gotScore = gotScore;
		
		nextStartingPlayer = GoTUtilities.getRandom().nextInt(GoTData.nbPlayers);
	}
	
	public boolean isGameEnd() {
		return this.currentPlay >= GoTData.nbPlays;
	}
	
    public void nextPlay() {
        currentPlay++;
    	
    	gotPiles.resetPile();

        nextStartingPlayer = GoTUtilities.getPlayerIndex(nextStartingPlayer);
        GoTPlayer player = this.players.get(nextStartingPlayer);
        if (player.getHand().getNumberOfCardsWithSuit(Suit.HEARTS) == 0) {
        	nextStartingPlayer = GoTUtilities.getPlayerIndex(nextStartingPlayer + 1);
        	player = this.players.get(nextStartingPlayer);
        }
        assert player.getHand().getNumberOfCardsWithSuit(Suit.HEARTS) != 0 : " Starting player has no hearts.";

        // 1: play the first 2 hearts
        for (int i = 0; i < 2; i++) {
            int playerIndex = GoTUtilities.getPlayerIndex(nextStartingPlayer + i);
            got.setStatusText("Player " + playerIndex + " select a Heart card to play");
            player = this.players.get(playerIndex);
            Optional<Card> selected = player.getCorrectSuit(true, 3);
            // Optional<Card> selected = player.getSelectedCard();

            int pileIndex = playerIndex % 2;
            assert selected.isPresent() : " Pass returned on selection of character.";
            System.out.println("Player " + playerIndex + " plays " + GoTUtilities.canonical(selected.get()) + " on pile " + pileIndex);
            selected.get().setVerso(false);
            selected.get().transfer(gotPiles.getPile(pileIndex), true); // transfer to pile (includes graphic effect)

            gotPiles.decorateCharacter(pileIndex, selected.get());

            gotPiles.updatePileRanks();
            disposePile.addPlayed(selected.get());

        }

        // 2: play the remaining nbPlayers * nbRounds - 2
        int remainingTurns = GoTData.nbPlayers * GoTData.nbRounds - 2;
        int nextPlayer = nextStartingPlayer + 2;

        while(remainingTurns > 0) {
            nextPlayer = GoTUtilities.getPlayerIndex(nextPlayer);
            got.setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");
            player = this.players.get(nextPlayer);
            GoTCardPilePair cardPile = player.getCorrectCardPile(got, gotPiles, remainingTurns);
            Optional<Card> selected = cardPile.getCard();
            
            if (selected.isPresent()) {
                got.setStatusText("Selected: " + GoTUtilities.canonical(selected.get()) + ". Player" + nextPlayer + " select a pile to play the card.");
                int pileIndex = cardPile.getPileIndex();

                System.out.println("Player " + nextPlayer + " plays " + GoTUtilities.canonical(selected.get()) + " on pile " + pileIndex);

                selected.get().setVerso(false);
                selected.get().transfer(gotPiles.getSelectedPile(), true); // transfer to pile (includes graphic effect)

                gotPiles.decorateCharacter(pileIndex, selected.get());

                gotPiles.updatePileRanks();
                disposePile.addPlayed(selected.get());
            } else {
                System.out.println("Player " + nextPlayer + " Pass");
                got.setStatusText("Pass.");
            }
            nextPlayer++;
            remainingTurns--;
        }

        /*********************************************/
        // TODO: Refactor the whole block underneath
        // 3: calculate winning & update scores for players
        gotPiles.updatePileRanks();
        int[] pile0Ranks = gotPiles.calculatePileRanks(0);
        int[] pile1Ranks = gotPiles.calculatePileRanks(1);
        System.out.println("piles[0]: " + GoTUtilities.canonical(gotPiles.getPile(0)));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[GoTData.ATTACK_RANK_INDEX] + " - Defence: " + pile0Ranks[GoTData.DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + GoTUtilities.canonical(gotPiles.getPile(1)));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[GoTData.ATTACK_RANK_INDEX] + " - Defence: " + pile1Ranks[GoTData.DEFENCE_RANK_INDEX]);
        int pile0CharacterRank = gotPiles.getInitialPileCard(0);
        int pile1CharacterRank = gotPiles.getInitialPileCard(1);
        String character0Result;
        String character1Result;

        if (pile0Ranks[GoTData.ATTACK_RANK_INDEX] > pile1Ranks[GoTData.DEFENCE_RANK_INDEX]) {
            gotScore.addScore(0, pile1CharacterRank);
            gotScore.addScore(2, pile1CharacterRank);
            character0Result = "Character 0 attack on character 1 succeeded.";
        } else {
            gotScore.addScore(1, pile1CharacterRank);
            gotScore.addScore(3, pile1CharacterRank);
            character0Result = "Character 0 attack on character 1 failed.";
        }

        if (pile1Ranks[GoTData.ATTACK_RANK_INDEX] > pile0Ranks[GoTData.DEFENCE_RANK_INDEX]) {
            gotScore.addScore(1, pile0CharacterRank);
            gotScore.addScore(3, pile0CharacterRank);
            character1Result = "Character 1 attack on character 0 succeeded.";
        } else {
            gotScore.addScore(0, pile0CharacterRank);
            gotScore.addScore(2, pile0CharacterRank);
            character1Result = "Character 1 attack character 0 failed.";
        }
        gotScore.updateScores();
        System.out.println(character0Result);
        System.out.println(character1Result);
        got.setStatusText(character0Result + " " + character1Result);

        // 5: discarded all cards on the piles
        nextStartingPlayer += 1;
        CardGame.delay(GoTData.watchingTime);
        
        gotScore.updateScores();
        
        if (isGameEnd()) {
        	gotScore.showGameResult();
        }
    }
}
