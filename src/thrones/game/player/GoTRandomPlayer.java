package thrones.game.player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.utility.GoTCardPilePair;
import thrones.game.utility.GoTPiles;
import thrones.game.utility.GoTUtilities;

public class GoTRandomPlayer extends GoTPlayer {

	public GoTRandomPlayer(Hand hand, int player) {
		super(hand, player);
		// TODO Auto-generated constructor stub
	}
	
	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.pickACorrectSuit(isCharacter, turn);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}

	// Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn){
		return super.playCorrectCardPile(got, gotPiles, turn);
	}

	public GoTCardPilePair strategy(GoTCardPilePair cardPile){
		return cardPile;
	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter, int turn){
		if (shortListCards.isEmpty() || !isCharacter && GoTUtilities.getRandom().nextInt(3) == 0) {
			selected = Optional.empty();
		} else {
			selected = Optional.of(shortListCards.get(GoTUtilities.getRandom().nextInt(shortListCards.size())));
		}
		return selected;
	}

}
