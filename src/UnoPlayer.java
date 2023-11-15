import java.util.ArrayList;
import java.util.List;

public class UnoPlayer {
    private String playerName;
    private Hand hand;
    private Deck deck;
    private int score;              // Player's score
    private boolean unoCalled;      // Indicates whether Uno has been called by the player
    private boolean remindedUno;    // Indicates whether Uno reminder has been displayed


    //Constructor
    public UnoPlayer(String name, Deck deck) {
        this.playerName = name;
        this.hand = new Hand();
        this.score = 0;
        this.deck = deck;
        // Draw 7 cards from the deck and add them to the player's hand
        for (int i = 0; i < 7; i++) {
            hand.addCard(this.deck.draw());
        }
        this.unoCalled = false;  // Uno is initially not called
        this.remindedUno = false; // Reminder is initially not displayed
    }

    public void playCard(Card card){
        if (hand.contains(card)){
            hand.removeCard(card);
            deck.discard(card);
        }

    }
    public void drawCard(Card card){
        hand.addCard(card);
    }


    public Hand getHand(){
        return hand;
    }

    public boolean hasUno(){
        if(hand.handSize()!=1){
            return false;
        }
        return true;
    }
    public boolean emptyHand(){

        return hand.handEmpty();
    }

    //reset player hands when starting game
    public void resetHand() {
        hand.clear();
    }

    public String getPlayerName(){
        return playerName;
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
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
    public void sayUno() {
        if (hasUno()) {
            System.out.println(getPlayerName() + " says UNO!");
            callUno();
        } else {
            System.out.println(getPlayerName() + " tried to say UNO, but they have more than one card left.");
        }
    }
}
