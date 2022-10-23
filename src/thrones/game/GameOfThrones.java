package thrones.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;

import java.util.*;

import org.w3c.dom.CharacterData;
import thrones.game.card.*;
import thrones.game.player.GoTPlayer;
import thrones.game.player.GoTPlayerType;
import thrones.game.player.GoTSimplePlayerFactory;
import thrones.game.player.GoTTeam;
import thrones.game.utility.*;

@SuppressWarnings("serial")
public class GameOfThrones extends CardGame {
	private GoTPropertiesLoader properties;
	private GoTScore gotScore;
	private GoTPiles gotPiles;
	private GoTDisposePile disposePile;
    private ArrayList<GoTPlayer> players;
    private GoTPlayMgr gotPlayMgr;

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

    private void setupGame() {        
    	Hand[] hands = new Hand[GoTData.nbPlayers];
        this.gotPiles = new GoTPiles(this);
        this.gotScore = new GoTScore(this, gotPiles);
        gotScore.initScore();
        this.disposePile = new GoTDisposePile();
        hands = new Hand[GoTData.nbPlayers];
        for (int i = 0; i < GoTData.nbPlayers; i++) {
            hands[i] = new Hand(GoTData.deck);
        }
        dealingOut(hands, GoTData.nbPlayers, GoTData.nbStartCards);

        for (int i = 0; i < GoTData.nbPlayers; i++) {
            hands[i].sort(Hand.SortType.SUITPRIORITY, true);
            System.out.println("hands[" + i + "]: " + GoTUtilities.canonical(hands[i]));
        }
        
        // Create player and give them hand of cards
        this.players = new ArrayList<>();
        for (int i = 0; i < GoTData.nbPlayers; i++) {
        	players.add(GoTSimplePlayerFactory.getInstance()
        			.getPlayer(properties.getPlayerType(i), hands[i], gotScore, disposePile, i%2));
            if (properties.getPlayerType(i) == GoTPlayerType.GOT_SMART) {
                disposePile.addObserver((GoTObserver) players.get(i));
            }
            System.out.println(properties.getPlayerType(i));
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
        gotPlayMgr = new GoTPlayMgr(this, gotPiles, disposePile, players, gotScore);
    }

    public GameOfThrones(GoTPropertiesLoader properties) {
    	// super(prop.getWidth(), prop.getHeight(), prop.getStatusHeight());
        super(GoTData.windowWidth, GoTData.windowHeight, GoTData.statusHeight);
        
        this.properties = properties;

        setTitle("Game of Thrones (V" + GoTData.version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
        setStatusText("Initializing...");
        
        setupGame();
        
        while (!gotPlayMgr.isGameEnd()) {
        	gotPlayMgr.nextPlay();
        }

    }

    public static void main(String[] args) {
        new GameOfThrones(new GoTPropertiesLoader("properties/got.properties"));
    }

}
