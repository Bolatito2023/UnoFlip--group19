public class UnoPlayer {
    private String playerName;
    private Hand hand;
    private Deck deck;


    /**
     * Constructor for player class
     * @param name gives name of player
     * @param deck gives the deck for players.
     */
    public UnoPlayer(String name,Deck deck) {
        this.playerName = name;
        this.hand = new Hand();
        this.deck = deck;
        // Draw 7 cards from the deck and add them to the player's hand
        for (int i = 0; i < 7; i++) {
            hand.addCard(deck.draw());
        }

    }

    /**
     * Method playCard allows player to play card, removes it and adds to top of discard pile.
     */
    public void playCard(Card card){
        if (hand.contains(card)){
            hand.removeCard(card);
            deck.discard(card);
        }

    }

    /**
     * Method drawCard draws a card from disacrd pile for player
     * @param card card that was drawn
     */
    public void drawCard(Card card){

        hand.addCard(card);
    }

    /**
     * Method getHand gets players hand
     * @return Hand
     */
    public Hand getHand(){

        return hand;
    }

    /**
     * Method hasUno checks if player has one card left.
     * @return boolean
     */
    public boolean hasUno(){
        if(hand.handSize()!=1){
            return false;
        }
        return true;
    }

    /**
     * Method emptyHand checks if players hand is empty.
     * @return boolean
     */
    public boolean emptyHand(){

        return hand.handEmpty();
    }

    /**
     * Method resetHand resets players hand starting of new game
     */
    public void resetHand() {
        hand.clear();
    }

    /**
     * Method getPlayerName gets the name of the player.
     * @return String
     *
     */
    public String getPlayerName(){
        return playerName;
    }


}
