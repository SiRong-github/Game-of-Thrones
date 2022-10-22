package thrones.game.player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.card.Suit;
import thrones.game.utility.GoTCardPilePair;
import thrones.game.utility.GoTPiles;
import thrones.game.utility.GoTUtilities;

public class GoTHumanPlayer extends GoTPlayer {

	public GoTHumanPlayer(Hand hand) {
		super(hand);
		// TODO Auto-generated constructor stub

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
	
	// Override
	public Optional<Card> getCorrectSuit(boolean isCharacter) {
		return super.waitForCorrectSuit(isCharacter);
	}
	
	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.waitForPileSelection();
		return piles.getSelectedPile();
	}

	// Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int nextPlayer){
		return super.waitForCorrectCardPile(got, gotPiles, nextPlayer);
	}

	// Override
	public GoTCardPilePair strategy(GoTCardPilePair cardPile, int playerNum){
		return cardPile;
	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter) {
		return Optional.empty();
	}

}
