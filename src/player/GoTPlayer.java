//Shanaia

package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Hand;
import utility.*;
import card.Suit;

public abstract class GoTPlayer {
	protected Hand hand;
    protected int player;
	protected Optional<Card> card;
    protected int pile;
	
	public GoTPlayer(Hand hand, int player) {
		this.hand = hand;
        this.player = player;
	}

    public abstract Optional<Card> getCorrectSuit(boolean isCharacter, int turn);
    protected abstract Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter);
    public abstract GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn);

    public Optional<Card> getSelectedCard(){
        return card;
    }

    public Hand getHand() {
        return this.hand;
    }

    protected Optional<Card> getRandomCard(List<Card> shortListCards) {
        return Optional.of(shortListCards.get(GoTUtilities.getRandom().nextInt(shortListCards.size())));
    }

    public Hand selectPile(GoTPiles piles) {
        piles.selectRandomPile();
        pile = piles.getSelectedPileIndex();
        return piles.getSelectedPile();
    }

    protected Optional<Card> waitForCorrectSuit(boolean isCharacter) {
        if (hand.isEmpty()) {
            card = Optional.empty();
        } else {
            card = null;
            hand.setTouchEnabled(true);
            do {
                if (card == null) {
                    CardGame.delay(100);
                    continue;
                }
                Suit suit = card.isPresent() ? (Suit) card.get().getSuit() : null;
                if (isCharacter && suit != null && suit.isCharacter() ||         // If we want character, can't pass and suit must be right
                        !isCharacter && (suit == null || !suit.isCharacter())) { // If we don't want character, can pass or suit must not be character
                    break;
                } else {
                    try {
                        heartRule(isCharacter, suit);
                    } catch (BrokeRuleException bre){
                        System.out.println(bre.getMessage());
                    }
                    card = null;
                    hand.setTouchEnabled(true);
                }
                CardGame.delay(100);
            } while (true);
        }
        return card;
    }

    /***
     * Determines if the player can't play magic
     * @param turn
     * @return true if can play a non magic card (1st effect card)
     */
    protected boolean canPlayMagic(int turn) {
        return turn < GoTData.maxTurns - GoTData.pileSize;
    }


    protected Optional<Card> pickACorrectSuit(boolean isCharacter, int turn) {
        List<Card> shortListCards = new ArrayList<>();
        for (Card c: hand.getCardList()) {
            if (isCharacter) { //only heart cards
                if (((Suit) c.getSuit()).isCharacter()) {
                    shortListCards.add(c);
                }
            } else {
                if (((Suit) c.getSuit()).isCharacter() || //only effect cards
                        ((Suit) c.getSuit()).isMagic() && !canPlayMagic(turn)) { //exclude magic cards if first effect
                    continue;
                }
                shortListCards.add(c);
            }
        }

        if (shortListCards.isEmpty()) {
            card = Optional.empty();
        } else {
            card = aiSuit(shortListCards, isCharacter);
        }

        return card;
    }

    public GoTCardPilePair playCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn) {
        card = getCorrectSuit(false, turn);
        if (card.isPresent()) {
            got.setStatusText("Selected: " + GoTUtilities.canonical(card.get()) + ". Player" +
                    player + " select a pile to play the card.");
            strategy();
            System.out.println("card "+ card);

            if (card.isPresent()) {
                selectPile(gotPiles);
                Suit suit = (Suit) card.get().getSuit();
                Suit prev = (Suit) gotPiles.getSelectedPile().getLast().getSuit();

                if (prev.isCharacter() && suit.isMagic()) {
                    card = Optional.empty();
                }

                if (card.isEmpty()) {
                    suit = null;
                }

                try {
                    diamondRule(suit, prev);
                } catch (BrokeRuleException bre) {
                    System.out.println(bre.getMessage());
                }
            }

        } else {
            card = Optional.empty();
        }
        return new GoTCardPilePair(card, pile);
    }

    public GoTCardPilePair waitForCorrectCardPile(GameOfThrones got, GoTPiles gotPiles) {
        Optional<Card> selected;
        GoTCardPilePair cardPile;
        do {
            selected = getCorrectSuit(false, 3);
            if (selected.isPresent()) {
                got.setStatusText("Selected: " + GoTUtilities.canonical(selected.get()) + ". Player" +
                        player + " select a pile to play the card.");
                selectPile(gotPiles);
                Suit suit = (Suit) selected.get().getSuit();
                Suit prev = (Suit) gotPiles.getSelectedPile().getLast().getSuit();
                try {
                    diamondRule(suit, prev);
                    cardPile = new GoTCardPilePair(selected, gotPiles.getSelectedPileIndex());
                    break;
                } catch (BrokeRuleException bre) {
                    System.out.println(bre.getMessage());
                }
            } else {
                cardPile = new GoTCardPilePair(Optional.empty(), 0);
                break;
            }
            got.setStatusText("Player" + player + " select a non-Heart card to play.");
        } while (true);
        return cardPile;
    }

    public Optional<Card> strategy(){
        return card;
    }

    //TODO: Fix throw, ensure that the program stops
    protected static void heartRule(boolean isCharacter, Suit suit) throws BrokeRuleException {
        if (suit != null && !isCharacter && suit.isCharacter()) {
            throw new BrokeRuleException("Heart (character) can only be played if you take one of the first 2 moves. " +
                    "Please try again.");
        }
    }

    public static void diamondRule(Suit suit, Suit prev) throws BrokeRuleException {
        if (prev != null && suit != null && prev.isCharacter() && suit.isMagic()) {
            throw new BrokeRuleException("Diamond can't be played directly on heart. Please try again.");
        }
    }
}
