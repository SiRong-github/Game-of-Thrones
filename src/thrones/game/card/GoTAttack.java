package thrones.game.card;

import ch.aplu.jcardgame.Card;
import thrones.game.utility.GoTData;

public class GoTAttack extends GoTCharacterDecorator{

	public GoTAttack(GoTCharacter decoratee,  Card card) {
		super(decoratee, card);
	}

	public int[] computeAttackAndDefend() {
		int effect = ((Rank) card.getRank()).getRankValue();

		if (decoratee.getCard().getRank() == card.getRank()) {
			effect = ((Rank) card.getRank()).getRankValue() * 2;
		}

		int attack = effect + decoratee.computeAttackAndDefend()[GoTData.ATTACK_RANK_INDEX];
		int defend = decoratee.computeAttackAndDefend()[GoTData.DEFENCE_RANK_INDEX];

		return new int[]{attack, defend};
	}
}
