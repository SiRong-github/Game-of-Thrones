package thrones.game.player;

import java.util.List;
import java.util.Optional;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import thrones.game.GameOfThrones;
import thrones.game.card.Suit;
import thrones.game.utility.GoTCardPilePair;
import thrones.game.utility.GoTPiles;
import thrones.game.utility.GoTUtilities;

public class GoTSimplePlayer extends GoTPlayer {

	public GoTSimplePlayer(Hand hand) {
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
	public GoTCardPilePair getCorrectCardPile(GameOfThrones got, GoTPiles gotPiles, int player){
		return super.playCorrectCardPile(got, gotPiles, player);
	}

	protected Optional<Card> aiSuit(List<Card> shortListCards, boolean isCharacter) {
		if (shortListCards.isEmpty() || !isCharacter && GoTUtilities.getRandom().nextInt(3) == 0) {
			selected = Optional.empty();
		} else {
			selected = Optional.of(shortListCards.get(GoTUtilities.getRandom().nextInt(shortListCards.size())));
		}
		return selected;
	}

	// Override
	public GoTCardPilePair strategy(GoTCardPilePair cardPile, int player){
		Optional<Card> card = cardPile.getCard();
		int pileIndex = cardPile.getPileIndex();
		int ownPile = player % 2;
		int otherPile = (player + 1) % 2;
		Suit suit = (Suit) card.get().getSuit();
		if ((suit.isMagic() && pileIndex == ownPile) || (!suit.isMagic() && pileIndex == otherPile)) {
			cardPile = new GoTCardPilePair(Optional.empty(), 0);
		}
		return cardPile;
	}

}
