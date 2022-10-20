package thrones.game.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Hand;
import thrones.game.card.Suit;
import thrones.game.utility.GoTUtilities;

public abstract class GoTPlayer {
	protected Hand hand;
	protected Optional<Card> selected;
	
	public GoTPlayer(Hand hand) {
		this.hand = hand;
		
		// Set up listener
        hand.addCardListener(new CardAdapter() {
            public void leftDoubleClicked(Card card) {
                selected = Optional.of(card);
                hand.setTouchEnabled(false);
            }
            public void rightClicked(Card card) {
                selected = Optional.empty(); // Don't care which card we right-clicked for player to pass
                hand.setTouchEnabled(false);
            }
        });
	}
	
    public void pickACorrectSuit(boolean isCharacter) {
        // Hand currentHand = hands[playerIndex];
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
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

    public void waitForCorrectSuit(boolean isCharacter) {
        if (hand.isEmpty()) {
            selected = Optional.empty();
        } else {
            selected = null;
            hand.setTouchEnabled(true);
            do {
                if (selected == null) {
                    CardGame.delay(100);
                    continue;
                }
                Suit suit = selected.isPresent() ? (Suit) selected.get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    // if (suit != null && suit.isCharacter() == isCharacter) {
                    break;
                } else {
                    selected = null;
                    hand.setTouchEnabled(true);
                }
                CardGame.delay(100);
            } while (true);
        }
    }
    
    public Optional<Card> getSelectedCard(){
    	return selected;
    }
    
    public Hand getHand() {
    	return this.hand;
    }
}
