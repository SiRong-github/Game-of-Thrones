package thrones.game.card;

public enum Rank {
    // Reverse order of rank importance (see rankGreater() below)
    // Order of cards is tied to card images
    ACE(1), KING(10), QUEEN(10), JACK(10), TEN(10), NINE(9), EIGHT(8), SEVEN(7), SIX(6), FIVE(5), FOUR(4), THREE(3), TWO(2);
    Rank(int rankValue) {
        this.rankValue = rankValue;
    }
    private final int rankValue;
    public int getRankValue() {
        return rankValue;
    }
    
    public static String canonical(Rank r) {
    	switch (r) {
        case ACE: case KING: case QUEEN: case JACK: case TEN:
            return r.toString().substring(0, 1);
        default:
            return String.valueOf(r.getRankValue());
    	}
    }
}
