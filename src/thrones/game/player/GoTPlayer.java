package thrones.game.player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardGame;
import ch.aplu.jcardgame.Hand;
import thrones.game.BrokeRuleException;
import thrones.game.GameOfThrones;
import thrones.game.card.Suit;
import thrones.game.utility.GoTCardPilePair;
import thrones.game.utility.GoTPiles;
import thrones.game.utility.GoTUtilities;

public abstract class GoTPlayer {
	protected Hand hand;
    protected int player;
	protected Optional<Card> selected;
	
	public GoTPlayer(Hand hand, int player) {
		this.hand = hand;
        this.player = player;
	}

    public abstract Hand selectPile(GoTPiles piles);
    public abstract Optional<Card> getCorrectSuit(boolean isCharacter, int turn);
    protected abstract Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter, int turn);
    public abstract GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn);
    public abstract GoTCardPilePair strategy(GoTCardPilePair cardPile);

    public Optional<Card> getSelectedCard(){
        return selected;
    }

    public Hand getHand() {
        return this.hand;
    }

    protected Optional<Card> waitForCorrectSuit(boolean isCharacter) {
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
                    break;
                } else {
                    try {
                        heartRule(isCharacter, suit);
                    } catch (BrokeRuleException bre){
                        System.out.println(bre.getMessage());
                    }
                    selected = null;
                    hand.setTouchEnabled(true);
                }
                CardGame.delay(100);
            } while (true);
        }
        return selected;
    }

    protected Optional<Card> pickACorrectSuit(boolean isCharacter, int turn) {
        // Hand currentHand = hands[playerIndex];
        List<Card> shortListCards = new ArrayList<>();
        for (int i = 0; i < hand.getCardList().size(); i++) {
            Card card = hand.getCardList().get(i);
            Suit suit = (Suit) card.getSuit();
            if (suit.isCharacter() == isCharacter) {
                shortListCards.add(card);
            }
        }
        return aiSuit(shortListCards, isCharacter, turn);
    }

    public GoTCardPilePair playCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn) {
        Optional<Card> selected;
        GoTCardPilePair cardPile;
        selected = getCorrectSuit(false, turn);
        System.out.println("selected " + selected);
        if (selected.isPresent()) {
            got.setStatusText("Selected: " + GoTUtilities.canonical(selected.get()) + ". Player" + player + " select a pile to play the card.");
            selectPile(gotPiles);
            Suit suit = (Suit) selected.get().getSuit();
            Suit prev = (Suit) gotPiles.getSelectedPile().getLast().getSuit();
            System.out.println("Pile: " + gotPiles.getSelectedPile());
            System.out.println("Prev: " + prev);
            if (prev.isCharacter() && suit.isMagic()) {
                cardPile = new GoTCardPilePair(Optional.empty(), 0);
            } else {
                cardPile = new GoTCardPilePair(selected, gotPiles.getSelectedPileIndex());
                cardPile = strategy(cardPile);
            }
        } else {
            cardPile = new GoTCardPilePair(Optional.empty(), 0);
        }
        System.out.println("card " + cardPile.getCard() + " index " + cardPile.getPileIndex());
        return cardPile;
    }

    public GoTCardPilePair waitForCorrectCardPile(GameOfThrones got, GoTPiles gotPiles) {
        Optional<Card> selected;
        GoTCardPilePair cardPile;
        do {
            selected = getCorrectSuit(false, 3);
            if (selected.isPresent()) {
                got.setStatusText("Selected: " + GoTUtilities.canonical(selected.get()) + ". Player" + player + " select a pile to play the card.");
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

    protected static void heartRule(boolean isCharacter, Suit suit) throws BrokeRuleException {
        if (!isCharacter && suit.isCharacter()) {
            throw new BrokeRuleException("Heart (character) can only be played if you take one of the first 2 moves. " +
                    "Please try again.");
        }
    }

    public static void diamondRule(Suit suit, Suit prev) throws BrokeRuleException {
        if (prev.isCharacter() && suit.isMagic()) {
            throw new BrokeRuleException("Diamond can't be played directly on heart. Please try again.");
        }
    }
}
