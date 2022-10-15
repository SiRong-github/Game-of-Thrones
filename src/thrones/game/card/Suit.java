package thrones.game.card;

public enum Suit {
    SPADES(GoTSuit.DEFENCE),
    HEARTS(GoTSuit.CHARACTER),
    DIAMONDS(GoTSuit.MAGIC),
    CLUBS(GoTSuit.ATTACK);
    Suit(GoTSuit gotsuit) {
        this.gotsuit = gotsuit;
    }
    private final GoTSuit gotsuit;

    public boolean isDefence(){ return gotsuit == GoTSuit.DEFENCE; }

    public boolean isAttack(){ return gotsuit == GoTSuit.ATTACK; }

    public boolean isCharacter(){ return gotsuit == GoTSuit.CHARACTER; }

    public boolean isMagic(){ return gotsuit == GoTSuit.MAGIC; }
}
