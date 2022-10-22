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

	public GoTRandomPlayer(Hand hand) {
		super(hand);
		// TODO Auto-generated constructor stub
	}
	
	public Optional<Card> getCorrectSuit(boolean isCharacter) {
		return super.pickACorrectSuit(isCharacter);
	}

	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.selectRandomPile();
		return piles.getSelectedPile();
	}

	// Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int nextPlayer){
		return super.playCorrectCardPile(got, gotPiles, nextPlayer);
	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter) {
		if (shortListCards.isEmpty() || !isCharacter && GoTUtilities.getRandom().nextInt(3) == 0) {
			selected = Optional.empty();
		} else {
			selected = Optional.of(shortListCards.get(GoTUtilities.getRandom().nextInt(shortListCards.size())));
		}
		return selected;
	}

	public GoTCardPilePair strategy(GoTCardPilePair cardPile, int playerNum){
		return cardPile;
	}

}
