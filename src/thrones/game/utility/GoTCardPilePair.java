package thrones.game.utility;

import ch.aplu.jcardgame.Card;

import java.util.Optional;

public class GoTCardPilePair {
    protected Optional<Card> card;
    protected int pileIndex;

    public GoTCardPilePair(Optional<Card> card, int pileIndex) {
        this.card = card;
        this.pileIndex = pileIndex;
    }

    public Optional<Card> getCard() {
        return this.card;
    }

    public int getPileIndex() {
        return this.pileIndex;
    }
}
