package thrones.game.card;

import ch.aplu.jcardgame.Card;

public class CharacterFactory {
    private static CharacterFactory factory = null;

    private CharacterFactory() {

    }

    public static CharacterFactory getCharacterFactory() {
        if (factory == null) {
            factory = new CharacterFactory();
        }
        return factory;
    }

    public GoTCharacter decorateCharacter(GoTCharacter decoratee, Card card) {
        GoTCharacter decorated = null;

        if (card.getSuit() == Suit.HEARTS) {
            decorated = new GotBaseCharacter(card);
        }
        else if (card.getSuit() == Suit.SPADES) {
            decorated = new GoTDefend(decoratee, card);
        }
        else if (card.getSuit() == Suit.CLUBS) {
            decorated = new GoTAttack(decoratee, card);
        }
        else if (card.getSuit() == Suit.DIAMONDS) {
            decorated = new GoTMagic(decoratee, card);
        }


        return decorated;
    }
}
