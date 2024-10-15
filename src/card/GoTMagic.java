package card;

import ch.aplu.jcardgame.Card;
import utility.GoTData;

public class GoTMagic extends GoTCharacterDecorator{

	public GoTMagic(GoTCharacter decoratee,  Card card) {
		super(decoratee, card);
	}

	public int[] computeAttackAndDefend() {
		int attack = 0, defend = 0;
		int effect = ((Rank) card.getRank()).getRankValue();

		if (decoratee.getCard().getRank() == card.getRank()) {
			effect = ((Rank) card.getRank()).getRankValue() * 2;
		}

		if ((Suit) decoratee.getCard().getSuit() == Suit.CLUBS) {
			attack = decoratee.computeAttackAndDefend()[GoTData.ATTACK_RANK_INDEX] - effect;
			defend = decoratee.computeAttackAndDefend()[GoTData.DEFENCE_RANK_INDEX];
		}

		else if ((Suit) decoratee.getCard().getSuit() == Suit.SPADES) {
			attack = decoratee.computeAttackAndDefend()[GoTData.ATTACK_RANK_INDEX];
			defend = decoratee.computeAttackAndDefend()[GoTData.DEFENCE_RANK_INDEX] - effect;
		}

		else if ((Suit) decoratee.getCard().getSuit() == Suit.DIAMONDS) {
			GoTCharacterDecorator decoratedCharacter = (GoTCharacterDecorator) decoratee;

			while (decoratedCharacter.getCard().getSuit() == Suit.DIAMONDS) {
				// The card below a diamond effect cannot be a heart
				decoratedCharacter = (GoTCharacterDecorator) decoratedCharacter.getDecoratee();
			}

			if ((Suit) decoratedCharacter.getCard().getSuit() == Suit.CLUBS) {
				attack = decoratee.computeAttackAndDefend()[GoTData.ATTACK_RANK_INDEX] - effect;
				defend = decoratee.computeAttackAndDefend()[GoTData.DEFENCE_RANK_INDEX];
			}

			else if ((Suit) decoratedCharacter.getCard().getSuit() == Suit.SPADES) {
				attack = decoratee.computeAttackAndDefend()[GoTData.ATTACK_RANK_INDEX];
				defend = decoratee.computeAttackAndDefend()[GoTData.DEFENCE_RANK_INDEX] - effect;
			}
		}

		attack = (attack < 0) ? 0: attack;
		defend = (defend < 0) ? 0: defend;

		return new int[]{attack, defend};
	}
}
