import java.util.ArrayList;
import java.util.List;

public class UnoPlayer {
    private String playerName;
    private Hand hand;
    private Deck deck;


    //Constructor
    public UnoPlayer(String name,Deck deck) {
        this.playerName = name;
        this.hand = new Hand();
        this.deck = deck;
        // Draw 7 cards from the deck and add them to the player's hand
        for (int i = 0; i < 7; i++) {
            hand.addCard(deck.draw());
        }

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


}
