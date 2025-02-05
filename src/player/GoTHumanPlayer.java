package player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.Hand;
import utility.GameOfThrones;
import utility.GoTCardPilePair;
import utility.GoTPiles;

public class GoTHumanPlayer extends GoTPlayer {

	public GoTHumanPlayer(Hand hand, int player) {
		super(hand, player);
		// TODO Auto-generated constructor stub

		// Set up listener
		hand.addCardListener(new CardAdapter() {
			public void leftDoubleClicked(Card card) {
				GoTHumanPlayer.this.card = Optional.of(card);
				hand.setTouchEnabled(false);
			}
			public void rightClicked(Card card) {
				GoTHumanPlayer.this.card = Optional.empty(); // Don't care which card we right-clicked for player to pass
				hand.setTouchEnabled(false);
			}
		});
	}
	
	@Override
	public Optional<Card> getCorrectSuit(boolean isCharacter, int turn) {
		return super.waitForCorrectSuit(isCharacter);
	}
	
	@Override
	public Hand selectPile(GoTPiles piles) {
		piles.waitForPileSelection();
		return piles.getSelectedPile();
	}

	@Override
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int turn){
		return super.waitForCorrectCardPile(got, gotPiles);
	}

	@Override
	public Optional<Card> strategy() {
		return this.card;
	}

	@Override
	protected Optional<Card> pickACorrectSuit(boolean isCharacter, int turn) {
		return Optional.empty();
	}

	@Override
	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter){
		return Optional.empty();
	}

}
