package thrones.game.card;

import ch.aplu.jcardgame.Card;

public class GotBaseCharacter extends GoTCharacter {

    public GotBaseCharacter(Card card) {
        setCard(card);
    }

    @Override
    public int[] computeAttackAndDefend() {
        int effect = ((Rank) card.getRank()).getRankValue();
        return new int[]{effect, effect};
    }

    @Override
    public int computeBaseCharacterRank() {
        return ((Rank) card.getRank()).getRankValue();
    }
}
