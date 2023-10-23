/**
 * @author Ohiorenua Ajayi-Isuku
 * @version 1.0
 * @since 2023-10-21
 * A class Deck. Represents the Deck in the Unoflip game.
 *
 * 
 */
import java.util.*;

public class Deck {
    private ArrayList<Card> deck = new ArrayList<Card>();
    private ArrayList<Card> discardedCards = new ArrayList<Card>();

    /**
     * The number of non_zero cards .
     */
    public static final int NUMBER_OF_REG_CARDS_EACH = 2;



    /**
     * The number of action cards of each type in
     * the deck. These include, Skips,Draw 1, Reverses,flip.
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


    /**
     * Constructor for deck class.
     */
    public Deck(){
         giveDeck();
         shuffle();

    }

    /**
     * Method giveDeck Creates a deck of cards.
     */

    public void giveDeck(){
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

    /**
     * Method shuffle shuffles the deck of cards.
     */
    public void shuffle(){

        Collections.shuffle(deck);
    }

    /**
     * Method isEmpty checks if a deck of card is empty.
     * @return boolen
     */
    public boolean isEmpty() {
        return deck.isEmpty();
    }


    /**
     * Method draw draws a card from the deck
     * @return Card
     */
    public Card draw()  {
        if (deck.isEmpty()) {
            System.out.println("The deck is empty");
        }
        return deck.remove(0);
    }
    /**
     * Method discard add all cards played to discard pile.
     */
    public void discard(Card c) {
        discardedCards.add(c);
    }

    /**
     * Method reAdd adds all the cards from discard pile and reshuffles to give deck.
     */
    public void reAdd() {
        deck.addAll(discardedCards);
        discardedCards.clear();
        shuffle();
    }


}
