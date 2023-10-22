import java.util.*;

public class Deck {
    private ArrayList<Card> deck;
    private ArrayList<Card> discardedCards;

    /**
     * The number of non_zero cards .
     */
    public static final int NUMBER_OF_REG_CARDS_EACH = 2;

    /**
     * The number of "special" cards (aka "action cards") of each suit in
     * the deck. These include, for instance, Skips,Draw 1 and Reverses,flip.
     */
    public static final int NUMBER_OF_DUP_SPECIAL_CARDS = 2;

    /**
     * The number of (standard, non-Draw-2) wild cards in the deck.
     */
    public static final int NUMBER_OF_WILD_CARDS = 4;

    /**
     * The number of Wild-Draw-2 in the deck.
     */
    public static final int NUMBER_OF_WILD_D2_CARDS = 4;


    public Deck(){
        deck = new ArrayList<Card>();
        discardedCards = new ArrayList<Card>();
    }


    public void giveDeck(){
        deck.clear();

        Card.CardType[] cards = {Card.CardType.SKIP, Card.CardType.REVERSE, Card.CardType.DRAW_ONE, Card.CardType.FLIP,};
        Card.Colour[] colors = {Card.Colour.BLUE, Card.Colour.GREEN, Card.Colour.RED, Card.Colour.YELLOW};
        Card.Number[] num = {Card.Number.ONE, Card.Number.TWO, Card.Number.THREE, Card.Number.FOUR,Card.Number.FIVE,Card.Number.SIX,Card.Number.SEVEN,Card.Number.EIGHT, Card.Number.NINE};

        for (int j = 0; j < NUMBER_OF_REG_CARDS_EACH; j++) {
            for (Card.Colour color : colors) {
                for (Card.Number number : num) {
                    deck.add(new Card(color, number));
                }
            }
        }

        for (int j = 0; j < NUMBER_OF_DUP_SPECIAL_CARDS; j++) {
            for (Card.CardType card : cards) {
                for (Card.Colour color : colors) {
                    deck.add(new Card(color, card));
                }
            }
        }
        for (int i=0; i<NUMBER_OF_WILD_CARDS; i++) {
            deck.add(new Card(Card.Colour.NONE,Card.CardType.WILD ));
        }
        for (int i=0; i<NUMBER_OF_WILD_D2_CARDS; i++) {
            deck.add(new Card(Card.Colour.NONE,Card.CardType.WILD_DRAW_TWO ));
        }
    }
    public void shuffle(){

        Collections.shuffle(deck);
    }

    public ArrayList<Card> getDeck(){
        return deck;
    }

    public ArrayList<Card> getDiscardedCards(){
        return discardedCards;
    }
    public boolean isEmpty() {
        return deck.isEmpty();
    }

    /**
     * Returns the top card of the Deck, and removes it.
     */
    //throws Exception maybe
    public Card draw()  {
        if (deck.isEmpty()) {
            System.out.println("The deck is empty");
        }
        return deck.remove(0);
    }
    /**
     * Discard the Card object passed so that it is out of play until a
     * remix operation.
     */
    public void discard(Card c) {
        discardedCards.add(c);
    }

    /**
     * re shuffle all the cards into the deck by adding all previously discarded cards back into
     * the deck, and shuffling in case the deck pile has finished and game is not over.
     */
    public void reAdd() {
        deck.addAll(discardedCards);
        discardedCards.clear();
        shuffle();
    }


}
