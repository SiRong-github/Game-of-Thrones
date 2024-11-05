//Xiaojiang
package utility;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import java.util.*;

import card.*;
import player.GoTPlayer;
import player.GoTPlayerFactory;
import player.GoTPlayerType;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
	private GoTPropertiesLoader properties;
    private GoTPlayMgr gotPlayMgr;
    private GoTScore gotScore;
    private GoTPiles gotPiles;
    private GoTDisposePile disposePile;
    private ArrayList<GoTPlayer> players;
    private Hand[] hands;

    public GameOfThrones(GoTPropertiesLoader properties) {
        super(GoTData.windowWidth, GoTData.windowHeight, GoTData.statusHeight);
        this.properties = properties;
        hands = new Hand[GoTData.nbPlayers];
        gotPiles = new GoTPiles(this);
        gotScore = new GoTScore(this, gotPiles);
        disposePile = new GoTDisposePile();
        players = new ArrayList<>();

        setTitle("Game of Thrones (V" + GoTData.version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");

        setupGame();

        while (!gotPlayMgr.isGameEnd()) {
            gotPlayMgr.nextPlay();
        }

    }

    /**
     * Removes ace cards from a given hand
     * @param pack
     */
    private void removeAces(Hand pack) {
        List<Card> aces = pack.getCardsWithRank(Rank.ACE);
        for (Card a : aces) {
            a.removeFromHand(false);
        }
    }

    /**
     * Deals 3 heart cards for each player
     * @param hands
     * @param pack
     */
    private void dealHearts(Hand[] hands, Hand pack) {
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            for (int j = 0; j < 3; j++) {
                List<Card> heartCards = pack.getCardsWithSuit(Suit.HEARTS);
                int x = GoTUtilities.getRandom().nextInt(heartCards.size());
                Card randomCard = heartCards.get(x);
                randomCard.removeFromHand(false);
                hands[i].insert(randomCard, false);
            }
        }
    }

    /**
     * Deals 9 effect cards for each player
     * @param hands
     * @param pack
     */
    private void dealEffects(Hand[] hands, Hand pack) {
        for (int i = 0; i < GoTData.nbStartCards; i++) {
            for (int j = 0; j < GoTData.nbPlayers; j++) {
                assert !pack.isEmpty() : " Pack has prematurely run out of cards.";
                Card dealt = GoTUtilities.randomCard(pack);
                dealt.removeFromHand(false);
                hands[j].insert(dealt, false);
            }
        }
    }

    /**
     * Deals out cards for each player
     * @param hands
     */
    private void dealingOut(Hand[] hands) {
        Hand pack = GoTData.deck.toHand(false);
        assert pack.getNumberOfCards() == 52 : " Starting pack is not 52 cards.";

        // Remove 4 Aces
        removeAces(pack);
        assert pack.getNumberOfCards() == 48 : " Pack without aces is not 48 cards.";

        // Give each player 3 heart cards
        dealHearts(hands, pack);
        assert pack.getNumberOfCards() == 36 : " Pack without aces and hearts is not 36 cards.";

        // Give each player 9 of the remaining cards
        dealEffects(hands, pack);
        for (int j = 0; j < GoTData.nbPlayers; j++) {
            assert hands[j].getNumberOfCards() == 12 : " Hand does not have twelve cards.";
        }
    }

    /**
     * Initialise hand for each player
     */
    private void initHands() {
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            hands[i] = new Hand(GoTData.deck);
        }
    }

    /**
     * Sort cards in hand based on suit priority for each player
     */
    private void sortHands() {
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + GoTUtilities.canonical(hands[i]));
        }
    }

    /**
     * Add players to the game
     */
    private void addPlayers() {
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            players.add(GoTPlayerFactory.getInstance()
                    .getPlayer(properties.getPlayerType(i), hands[i], gotScore, disposePile, i % 2));
            if (properties.getPlayerType(i) == GoTPlayerType.GOT_SMART) {
                disposePile.addObserver((GoTObserver) players.get(i));
            }
            System.out.println(properties.getPlayerType(i));
        }
    }

    /**
     * Display layout of the game
     */
    private void displayLayout() {
        RowLayout[] layouts = new RowLayout[GoTData.nbPlayers];
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            layouts[i] = new RowLayout(GoTData.handLocations[i], GoTData.handWidth);
            layouts[i].setRotationAngle(90 * i);
            hands[i].setView(this, layouts[i]);
            hands[i].draw();
        }
    }

    /**
     * Set up Game of Thrones
     */
    private void setupGame() {
        //Initialise scores and hands
        gotScore.initScore();
        initHands();

        //Deal out and sort cards in hands for each player
        dealingOut(hands);
        sortHands();

        // Create player and give them hand of cards
        addPlayers();

        // graphics
        displayLayout();

        // End graphics
        gotPlayMgr = new GoTPlayMgr(this, gotPiles, disposePile, players, gotScore);
    }

    /**
     * Main method
     * @param args
     */
    public static void main(String[] args) {
        try {
            new GameOfThrones(new GoTPropertiesLoader(args[0]));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("No inputted property file.");
        }
    }

}
