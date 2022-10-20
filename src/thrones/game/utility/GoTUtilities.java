package thrones.game.utility;

import java.util.Random;
import java.util.stream.Collectors;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.card.Rank;
import thrones.game.card.Suit;

public class GoTUtilities {
    private static Random random = new Random(0);
	
	public static String canonical(Suit s) {
    	return s.toString().substring(0, 1);
    }
    
    public static String canonical(Card c) { 
    	return canonical((Rank) c.getRank()) + canonical((Suit)c.getSuit()); 
    }

    public static String canonical(Hand h) {
        return "[" + h.getCardList().stream().map(GoTUtilities::canonical).collect(Collectors.joining(",")) + "]";
    }
    
    public static String canonical(Rank r) {
    	switch (r) {
        case ACE: case KING: case QUEEN: case JACK: case TEN:
            return r.toString().substring(0, 1);
        default:
            return String.valueOf(r.getRankValue());
    	}
    }
    
    
    public static void initializeRandom(int seed) {
    	random = new Random(seed);
    }
    
    public static Random getRandom() {
    	return random;
    }
    
    public static Card randomCard(Hand hand) {
        assert !hand.isEmpty() : " random card from empty hand.";
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }
}
