import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class HandTest {
    private Hand testHand;
    Card cBlueFive;
    Card cYellowReverse;
    Card cWild;

    @Before
    public void setUp() {
        testHand = new Hand();
        cBlueFive = new Card(Card.Colour.BLUE, Card.Number.FIVE);
        cYellowReverse = new Card(Card.Colour.YELLOW, Card.CardType.REVERSE);
        cWild = new Card(Card.Colour.NONE, Card.CardType.WILD);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void handEmpty() {
        setUp();
        assertTrue(testHand.handEmpty());

        testHand.addCard(cBlueFive);
        assertFalse(testHand.handEmpty());
        assertTrue(testHand.contains(cBlueFive));

        testHand.removeCard(cBlueFive);
        assertTrue(testHand.handEmpty());
    }

    @Test
    public void addCard() {
        setUp();

        assertFalse(testHand.contains(cBlueFive));
        testHand.addCard(cBlueFive);
        assertTrue(testHand.contains(cBlueFive));
    }

    @Test
    public void remove_cardNotInHand() {
        setUp();

        testHand.addCard(cWild);
        assertTrue(testHand.contains(cWild));
        assertFalse(testHand.contains(cBlueFive));

        testHand.removeCard(cBlueFive);
        assertTrue(testHand.contains(cWild));
        assertFalse(testHand.contains(cBlueFive));

    }

    @Test
    public void remove_sameCard() {
        setUp();

        assertTrue(testHand.handEmpty());

        testHand.addCard(cBlueFive);
        testHand.addCard(cBlueFive);
        assertTrue(testHand.contains(cBlueFive));
        assertEquals(testHand.handSize(), 2);

        testHand.removeCard(cBlueFive);
        assertTrue(testHand.contains(cBlueFive));
        assertEquals(testHand.handSize(), 1);
    }


    @Test
    public void handSize() {
        setUp();

        assertEquals(testHand.handSize(), 0);

        testHand.addCard(cBlueFive);
        assertEquals(testHand.handSize(), 1);
        testHand.addCard(cWild);
        assertEquals(testHand.handSize(), 2);

        testHand.removeCard(cBlueFive);
        assertEquals(testHand.handSize(), 1);
        testHand.addCard(cYellowReverse);
        assertEquals(testHand.handSize(), 2);
        testHand.addCard(cYellowReverse);
        assertEquals(testHand.handSize(), 3);

        testHand.clear();
        assertEquals(testHand.handSize(), 0);
    }

    @Test
    public void clear() {
        setUp();

        testHand.addCard(cBlueFive);
        testHand.addCard(cYellowReverse);
        assertFalse(testHand.handEmpty());

        testHand.clear();
        assertTrue(testHand.handEmpty());
    }


    @Test
    public void getCards() {
        setUp();

        testHand.addCard(cBlueFive);
        testHand.addCard(cYellowReverse);
        testHand.addCard(cWild);
        testHand.addCard(cYellowReverse);

        for(int k=0; k < testHand.handSize(); k++){
            switch(k){
                case(0):
                    Card c0 = testHand.getCards().get(k);
                    assertEquals(c0.getColour(), Card.Colour.BLUE);
                    assertEquals(c0.getCardType(), Card.CardType.NUMBER);
                    assertEquals(c0.getNumber(), Card.Number.FIVE);
                    break;
                case (1):
                case (3):
                    Card c1 = testHand.getCards().get(k);
                    assertEquals(c1.getColour(), Card.Colour.YELLOW);
                    assertEquals(c1.getCardType(), Card.CardType.REVERSE);
                    assertNull(c1.getNumber());
                    break;
                case (2):
                    Card c2 = testHand.getCards().get(k);
                    assertEquals(c2.getColour(), Card.Colour.NONE);
                    assertEquals(c2.getCardType(), Card.CardType.WILD);
                    assertNull(c2.getNumber());
                    break;
            }
        }
    }

    /*
    @Test
    public void getPlayableCards() {
        setUp();

        testHand.addCard(cBlueFive);
        testHand.addCard(cYellowReverse);
        testHand.addCard(cWild);
        Card cGreenThree = new Card(Card.Colour.GREEN, Card.Number.THREE);
        Card cYellowThree = new Card(Card.Colour.YELLOW, Card.Number.THREE);
        Card cRedNine = new Card(Card.Colour.RED, Card.Number.NINE);
        Card cRedEight = new Card(Card.Colour.RED, Card.Number.EIGHT);
        testHand.addCard(cGreenThree);
        testHand.addCard(cYellowThree);
        testHand.addCard(cRedNine);
        testHand.addCard(cRedEight);

        List<Card> list1;
        list1 = testHand.getPlayableCards(new Card(Card.Colour.GREEN, Card.Number.FOUR), null);
        assertTrue(list1.contains(cWild));
        assertTrue(list1.contains(cGreenThree));

        List<Card> list2;
        list2 = testHand.getPlayableCards(new Card(Card.Colour.NONE, Card.CardType.WILD_DRAW_TWO), Card.Colour.RED);
        assertTrue(list2.contains(cWild));
        assertTrue(list2.contains(cRedNine));
        assertTrue(list2.contains(cRedEight));

        List<Card> list3;
        list3 = testHand.getPlayableCards(new Card(Card.Colour.BLUE, Card.Number.THREE), null);
        assertTrue(list3.contains(cWild));
        assertTrue(list3.contains(cBlueFive));
        assertTrue(list3.contains(cGreenThree));
        assertTrue(list3.contains(cYellowThree));
    }

     */
}