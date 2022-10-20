package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.awt.Font;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import thrones.game.card.*;
import thrones.game.player.GoTPlayer;
import thrones.game.player.GoTSimplePlayerFactory;
import thrones.game.player.GoTTeam;
import thrones.game.utility.GoTData;
import thrones.game.utility.GoTPiles;
import thrones.game.utility.GoTPropertiesLoader;
import thrones.game.utility.GoTScore;
import thrones.game.utility.GoTUtilities;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
	private GoTPropertiesLoader properties;
	private GoTScore gotScore;
	private GoTPiles gotPiles;
    
    // static public int seed;
    // static Random random;

    // return random Card from Hand

    private void dealingOut(Hand[] hands, int nbPlayers, int nbCardsPerPlayer) {
        Hand pack = GoTData.deck.toHand(false);
        assert pack.getNumberOfCards() == 52 : " Starting pack is not 52 cards.";
        // Remove 4 Aces
        List<Card> aceCards = pack.getCardsWithRank(Rank.ACE);
        for (Card card : aceCards) {
            card.removeFromHand(false);
        }
        assert pack.getNumberOfCards() == 48 : " Pack without aces is not 48 cards.";
        // Give each player 3 heart cards
        for (int i = 0; i < nbPlayers; i++) {
            for (int j = 0; j < 3; j++) {
                List<Card> heartCards = pack.getCardsWithSuit(Suit.HEARTS);
                int x = GoTUtilities.getRandom().nextInt(heartCards.size());
                Card randomCard = heartCards.get(x);
                randomCard.removeFromHand(false);
                hands[i].insert(randomCard, false);
            }
        }
        assert pack.getNumberOfCards() == 36 : " Pack without aces and hearts is not 36 cards.";
        // Give each player 9 of the remaining cards
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j = 0; j < nbPlayers; j++) {
                assert !pack.isEmpty() : " Pack has prematurely run out of cards.";
                Card dealt = GoTUtilities.randomCard(pack);
                dealt.removeFromHand(false);
                hands[j].insert(dealt, false);
            }
        }
        for (int j = 0; j < nbPlayers; j++) {
            assert hands[j].getNumberOfCards() == 12 : " Hand does not have twelve cards.";
        }
    }


    // private final int watchingTime = 5000;
    private Hand[] hands;
    // private Hand[] piles;
    
    private int nextStartingPlayer = GoTUtilities.getRandom().nextInt(GoTData.nbPlayers);

    // boolean[] humanPlayers = { true, false, false, false};
    boolean[] humanPlayers = { false, false, false, false};

    private Optional<Card> selected;

    
    private void setupGame() {
        hands = new Hand[GoTData.nbPlayers];
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            hands[i] = new Hand(GoTData.deck);
        }
        dealingOut(hands, GoTData.nbPlayers, GoTData.nbStartCards);

        for (int i = 0; i < GoTData.nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + GoTUtilities.canonical(hands[i]));
        }

        for (final Hand currentHand : hands) {
            // Set up human player for interaction
            currentHand.addCardListener(new CardAdapter() {
                public void leftDoubleClicked(Card card) {
                    selected = Optional.of(card);
                    currentHand.setTouchEnabled(false);
                }
                public void rightClicked(Card card) {
                    selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
                    currentHand.setTouchEnabled(false);
                }
            });
        }
        
        // Create player and give them hand of cards
        ArrayList<GoTPlayer> players = new ArrayList<>();
        for (int i = 0; i < GoTData.nbPlayers; i++) {
        	players.add(GoTSimplePlayerFactory.getInstance().getPlayer(properties.getPlayerType(i)));
        }
        // Create teams and send players to them
        ArrayList<GoTTeam> teams = new ArrayList<>();
        teams.add(new GoTTeam());
        teams.add(new GoTTeam());
        int currentTeam = 0;
        for (int i = 0; i < GoTData.nbPlayers; i++) {
        	currentTeam = GoTData.nbPlayers%teams.size();
        	teams.get(currentTeam).addPlayer(players.get(i));
        }
        // Create round manager and send teams to it
        
        // graphics
        RowLayout[] layouts = new RowLayout[GoTData.nbPlayers];
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            layouts[i] = new RowLayout(GoTData.handLocations[i], GoTData.handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(this, layouts[i]);
            hands[i].draw();
        }
        // End graphics
    }

    private void pickACorrectSuit(int playerIndex, boolean isCharacter) {
        Hand currentHand = hands[playerIndex];
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < currentHand.getCardList().size(); i++) {
            Card card = currentHand.getCardList().get(i);
            Suit suit = (Suit) card.getSuit();
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
        if (shortListCards.isEmpty() || !isCharacter && GoTUtilities.getRandom().nextInt(3) == 0) {
            selected = Optional.empty();
        } else {
            selected = Optional.of(shortListCards.get(GoTUtilities.getRandom().nextInt(shortListCards.size())));
        }
    }

    private void waitForCorrectSuit(int playerIndex, boolean isCharacter) {
        if (hands[playerIndex].isEmpty()) {
            selected = Optional.empty();
        } else {
            selected = null;
            hands[playerIndex].setTouchEnabled(true);
            do {
                if (selected == null) {
                    delay(100);
                    continue;
                }
                Suit suit = selected.isPresent() ? (Suit) selected.get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    // if (suit != null && suit.isCharacter() == isCharacter) {
                    break;
                } else {
                    selected = null;
                    hands[playerIndex].setTouchEnabled(true);
                }
                delay(100);
            } while (true);
        }
    }
    
    private int getPlayerIndex(int index) {
        return index % GoTData.nbPlayers;
    }

    private void executeAPlay() {
        gotPiles.resetPile();

        nextStartingPlayer = getPlayerIndex(nextStartingPlayer);
        if (hands[nextStartingPlayer].getNumberOfCardsWithSuit(Suit.HEARTS) == 0)
            nextStartingPlayer = getPlayerIndex(nextStartingPlayer + 1);
        assert hands[nextStartingPlayer].getNumberOfCardsWithSuit(Suit.HEARTS) != 0 : " Starting player has no hearts.";

        // 1: play the first 2 hearts
        for (int i = 0; i < 2; i++) {
            int playerIndex = getPlayerIndex(nextStartingPlayer + i);
            setStatusText("Player " + playerIndex + " select a Heart card to play");
            if (humanPlayers[playerIndex]) {
                waitForCorrectSuit(playerIndex, true);
            } else {
                pickACorrectSuit(playerIndex, true);
            }

            int pileIndex = playerIndex % 2;
            assert selected.isPresent() : " Pass returned on selection of character.";
            System.out.println("Player " + playerIndex + " plays " + GoTUtilities.canonical(selected.get()) + " on pile " + pileIndex);
            selected.get().setVerso(false);
            selected.get().transfer(gotPiles.getPile(pileIndex), true); // transfer to pile (includes graphic effect)
            gotPiles.updatePileRanks();
        }

        // 2: play the remaining nbPlayers * nbRounds - 2
        int remainingTurns = GoTData.nbPlayers * GoTData.nbRounds - 2;
        int nextPlayer = nextStartingPlayer + 2;

        while(remainingTurns > 0) {
            nextPlayer = getPlayerIndex(nextPlayer);
            setStatusText("Player" + nextPlayer + " select a non-Heart card to play.");
            if (humanPlayers[nextPlayer]) {
                waitForCorrectSuit(nextPlayer, false);
            } else {
                pickACorrectSuit(nextPlayer, false);
            }

            if (selected.isPresent()) {
                setStatusText("Selected: " + GoTUtilities.canonical(selected.get()) + ". Player" + nextPlayer + " select a pile to play the card.");
                if (humanPlayers[nextPlayer]) {
                    gotPiles.waitForPileSelection();
                } else {
                    gotPiles.selectRandomPile();
                }
                System.out.println("Player " + nextPlayer + " plays " + GoTUtilities.canonical(selected.get()) + " on pile " + gotPiles.getSelectedPileIndex());
                selected.get().setVerso(false);
                selected.get().transfer(gotPiles.getSelectedPile(), true); // transfer to pile (includes graphic effect)
                gotPiles.updatePileRanks();
            } else {
                setStatusText("Pass.");
            }
            nextPlayer++;
            remainingTurns--;
        }

        // 3: calculate winning & update scores for players
        gotPiles.updatePileRanks();
        int[] pile0Ranks = gotPiles.calculatePileRanks(0);
        int[] pile1Ranks = gotPiles.calculatePileRanks(1);
        System.out.println("piles[0]: " + GoTUtilities.canonical(gotPiles.getPile(0)));
        System.out.println("piles[0] is " + "Attack: " + pile0Ranks[GoTData.ATTACK_RANK_INDEX] + " - Defence: " + pile0Ranks[GoTData.DEFENCE_RANK_INDEX]);
        System.out.println("piles[1]: " + GoTUtilities.canonical(gotPiles.getPile(1)));
        System.out.println("piles[1] is " + "Attack: " + pile1Ranks[GoTData.ATTACK_RANK_INDEX] + " - Defence: " + pile1Ranks[GoTData.DEFENCE_RANK_INDEX]);
        Rank pile0CharacterRank = (Rank) gotPiles.getPile(0).getCardList().get(0).getRank();
        Rank pile1CharacterRank = (Rank) gotPiles.getPile(1).getCardList().get(0).getRank();
        String character0Result;
        String character1Result;

        if (pile0Ranks[GoTData.ATTACK_RANK_INDEX] > pile1Ranks[GoTData.DEFENCE_RANK_INDEX]) {
//            scores[0] += pile1CharacterRank.getRankValue();
//            scores[2] += pile1CharacterRank.getRankValue();
            gotScore.addScore(0, pile1CharacterRank.getRankValue());
            gotScore.addScore(2, pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 succeeded.";
        } else {
//            scores[1] += pile1CharacterRank.getRankValue();
//            scores[3] += pile1CharacterRank.getRankValue();
            gotScore.addScore(1, pile1CharacterRank.getRankValue());
            gotScore.addScore(3, pile1CharacterRank.getRankValue());
            character0Result = "Character 0 attack on character 1 failed.";
        }

        if (pile1Ranks[GoTData.ATTACK_RANK_INDEX] > pile0Ranks[GoTData.DEFENCE_RANK_INDEX]) {
//            scores[1] += pile0CharacterRank.getRankValue();
//            scores[3] += pile0CharacterRank.getRankValue();
            gotScore.addScore(1, pile0CharacterRank.getRankValue());
            gotScore.addScore(3, pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack on character 0 succeeded.";
        } else {
//            scores[0] += pile0CharacterRank.getRankValue();
//            scores[2] += pile0CharacterRank.getRankValue();
            gotScore.addScore(0, pile0CharacterRank.getRankValue());
            gotScore.addScore(2, pile0CharacterRank.getRankValue());
            character1Result = "Character 1 attack character 0 failed.";
        }
        gotScore.updateScores();
        System.out.println(character0Result);
        System.out.println(character1Result);
        setStatusText(character0Result + " " + character1Result);

        // 5: discarded all cards on the piles
        nextStartingPlayer += 1;
        delay(GoTData.watchingTime);
    }

    public GameOfThrones(GoTPropertiesLoader properties) {
    	// super(prop.getWidth(), prop.getHeight(), prop.getStatusHeight());
        super(700, 700, 30);
        
        this.properties = properties;

        setTitle("Game of Thrones (V" + GoTData.version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        
        this.gotPiles = new GoTPiles(this);
        
        this.gotScore = new GoTScore(this, gotPiles);
        gotScore.initScore();

        setupGame();
        for (int i = 0; i < GoTData.nbPlays; i++) {
            executeAPlay();
            gotScore.updateScores();
        }

        gotScore.showGameResult();

        refresh();
    }

    public static void main(String[] args) {
        // System.out.println("Working Directory = " + System.getProperty("user.dir"));
        // final Properties properties = new Properties();
        // properties.setProperty("watchingTime", "5000");
        /*
        if (args == null || args.length == 0) {
            //  properties = PropertiesLoader.loadPropertiesFile("cribbage.properties");
        } else {
            //  properties = PropertiesLoader.loadPropertiesFile(args[0]);
        }

        String seedProp = properties.getProperty("seed");  //Seed property
        if (seedProp != null) { // Use property seed
			  seed = Integer.parseInt(seedProp);
        } else { // and no property
			  seed = new Random().nextInt(); // so randomise
        }
        */
    	// GoTPropertiesLoader properties = new GoTPropertiesLoader("properties/got.properties");
        // int seed = properties.getSeed();
        // System.out.println("Seed = " + seed);
        // GoTUtilities.initializeRandom(seed);
        // GoTData.watchingTime = ((Number)Integer.parseInt(properties.getProperty("watchingTime"))).intValue();
        //GameOfThrones.random = new Random(seed);
        
        new GameOfThrones(new GoTPropertiesLoader("properties/got.properties"));
    }

}
