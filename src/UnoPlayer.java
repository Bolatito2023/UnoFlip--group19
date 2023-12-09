import java.util.ArrayList;
import java.util.List;

public class UnoPlayer {
    private String playerName;
    protected Hand hand;
    protected Deck deck;
    private int score;              // Player's score
    private boolean unoCalled;      // Indicates whether Uno has been called by the player
    private boolean remindedUno;    // Indicates whether Uno reminder has been displayed
    private Card wantToPlay = null;

    /**
     * Constructs new player UnoPlayer.
     * @param name the name of the player.
     * @param deck the game's deck of cards.
     */
    public UnoPlayer(String name, Deck deck) {
        this.playerName = name;
        this.hand = new Hand();
        this.score = 0;
        this.deck = deck;
        dealCards();         // Draw 7 cards from the deck and add them to the player's hand
        this.unoCalled = false;  // Uno is initially not called
        this.remindedUno = false; // Reminder is initially not displayed
    }

    public void dealCards() {
        for (int i = 0; i < 7; i++) {
            hand.addCard(this.deck.draw());
        }
    }
    /**
     * Plays a card from the player's hand.
     * @param card the card to be played
     */
    public void playCard(Card card){
        if (hand.contains(card)){
            hand.removeCard(card);
            wantToPlay = card;
        }
    }

    public void confirmPlay(){
        deck.discard(wantToPlay);
    }

    public void undoPlay(){
        hand.addCard(wantToPlay);
    }

    public void redoPlay(){
        playCard(wantToPlay);
    }

    /**
     * Adds a card to the player's hand.
     * @param card the card added to the player's hand.
     */
    public void drawCard(Card card){
        hand.addCard(card);
    }

    /**
     * Returns player's hand.
     * @return hand Arraylist of cards in player's hand.
     */
    public Hand getHand(){
        return hand;
    }

    /**
     * Checks if player has Uno.
     * @return True if the player has exactly one card, False otherwise.
     */
    public boolean hasUno(){
        if(hand.handSize()!=1){
            return false;
        }
        return true;
    }

    /**
     * Checks if the player's hand is empty.
     * @return True if the player has no cards in hand, otherwise False.
     */
    public boolean emptyHand(){

        return hand.handEmpty();
    }

    /**
     * Clear's cards in player's hand.
     */
    public void clearHand(int cardIndex) {
        Card c = hand.getCards().get(cardIndex);
        //System.out.println(c.toString(true));
        hand.removeCard(c);
        deck.discard(c);
    }

    /**
     * Returns the player's name.
     * @return playerName the player's name.
     */
    public String getPlayerName(){
        return playerName;
    }

    public boolean getUnoCalled() {
        return unoCalled;
    }
    public void setUnoCalled(boolean unoCalled) {
        this.unoCalled = unoCalled;
    }
    public void callUno() {
        unoCalled = true;
    }
    public void updateScore(int newScore) { score = newScore;}
    public boolean hasRemindedUno() {
        return remindedUno;
    }
    public void setRemindedUno(boolean remindedUno) {
        this.remindedUno = remindedUno;
    }

    /**
     * Prints whether the player says UNO at the right time.
     */
    public void sayUno() {
        if (hasUno()) {
            System.out.println(getPlayerName() + " says UNO!");
            callUno();
        } else {
            System.out.println(getPlayerName() + " tried to say UNO, but they have more than one card left.");
        }
    }
}
