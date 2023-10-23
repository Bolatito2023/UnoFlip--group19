import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;

import static org.junit.Assert.*;

public class DeckTest {

    private Deck testDeck;

    @Before
    public void setUp(){
        testDeck = new Deck();
    }

    @After
    public void tearDown(){

    }

    @Test
    public void giveDeck() {
        setUp();
        assertEquals(testDeck.getDeck().size(), 0);
        testDeck.giveDeck();
        assertEquals(testDeck.getDeck().size(), 112);
    }

    @Test
    public void giveDeck_checkCardColourCount() {
        setUp();

        testDeck.giveDeck();
        int numRed = 0, numBlue = 0, numGreen = 0, numYellow = 0, numWild = 0, numWildD2 = 0;
        for(Card k : testDeck.getDeck()){
            if (k.getColour() == Card.Colour.RED) {
                numRed++;
            } else if (k.getColour() == Card.Colour.BLUE) {
                numBlue++;
            } else if (k.getColour() == Card.Colour.GREEN) {
                numGreen++;
            } else if (k.getColour() == Card.Colour.YELLOW) {
                numYellow++;
            } else {
                if (k.getCardType() == Card.CardType.WILD) {
                    numWild++;
                } else if (k.getCardType() == Card.CardType.WILD_DRAW_TWO) {
                    numWildD2++;
                }
            }
        }
        assertEquals(numRed, 26);
        assertEquals(numBlue, 26);
        assertEquals(numGreen, 26);
        assertEquals(numYellow, 26);
        assertEquals(numWild, 4);
        assertEquals(numWildD2, 4);
    }

    @Test
    public void shuffle() {
        setUp();

        testDeck.giveDeck();
        ArrayList<Card> testDeckCopy = (ArrayList<Card>) testDeck.getDeck().clone();

        assertEquals(testDeck.getDeck(), testDeckCopy);
        testDeck.shuffle();
        assertNotEquals(testDeck.getDeck(), testDeckCopy);
    }

    @Test
    public void isEmpty() {
        setUp();

        assertTrue(testDeck.isEmpty());

        testDeck.giveDeck();

        int deckSize = testDeck.getDeck().size();
        for (int k=0; k < deckSize; k++) {
            testDeck.draw();
        }

        assertTrue(testDeck.isEmpty());
    }

    @Test
    public void draw() {
        setUp();

        testDeck.giveDeck();
        assertEquals(testDeck.getDeck().size(), 112);

        testDeck.draw();
        assertEquals(testDeck.getDeck().size(), 111);

        int deckSize = testDeck.getDeck().size();
        for (int k=0; k < deckSize; k++) {
            testDeck.draw();
        }

        //draw from empty deck
        assertNull(testDeck.draw());
    }

    @Test
    public void discard() {
        setUp();

        testDeck.giveDeck();

        assertEquals(testDeck.getDiscardedCards().size(), 0);

        Card card1 = testDeck.draw();
        Card card2 = testDeck.draw();

        testDeck.discard(card1);
        assertEquals(testDeck.getDiscardedCards().size(), 1);
        testDeck.discard(card2);
        assertEquals(testDeck.getDiscardedCards().size(), 2);

        assertEquals(testDeck.getDiscardedCards().get(0), card1);
        assertEquals(testDeck.getDiscardedCards().get(1), card2);

    }

    @Test
    public void reAdd() {
        setUp();

        assertEquals(testDeck.getDiscardedCards().size(), 0);
        assertEquals(testDeck.getDeck().size(), 0);

        Card cBlueSeven = new Card(Card.Colour.BLUE, Card.Number.SEVEN);
        Card cGreenSix = new Card(Card.Colour.GREEN, Card.Number.SIX);
        Card cGreenReverse = new Card(Card.Colour.GREEN, Card.CardType.REVERSE);
        testDeck.discard(cBlueSeven);
        testDeck.discard(cGreenSix);
        testDeck.discard(cGreenReverse);
        assertEquals(testDeck.getDiscardedCards().get(0), cBlueSeven);
        assertEquals(testDeck.getDiscardedCards().get(1), cGreenSix);
        assertEquals(testDeck.getDiscardedCards().get(2), cGreenReverse);

        assertEquals(testDeck.getDiscardedCards().size(), 3);
        assertEquals(testDeck.getDeck().size(), 0);

        testDeck.reAdd();

        //cards will not be in order since they get shuffled in reAdd()
        assertTrue(testDeck.getDeck().contains(cBlueSeven));
        assertTrue(testDeck.getDeck().contains(cGreenSix));
        assertTrue(testDeck.getDeck().contains(cGreenReverse));

        assertEquals(testDeck.getDiscardedCards().size(), 0);
        assertEquals(testDeck.getDeck().size(), 3);

    }
}