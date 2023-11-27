import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HandTest {
    private Hand hand;

    @Before
    public void setUp() {
        hand = new Hand();
    }

    @Test
    public void addCard() {
        Card card = new Card(Card.Colour.BLUE, Card.CardType.SKIP);
        hand.addCard(card);
        assertTrue(hand.contains(card));
    }

    @Test
    public void removeCard() {
        Card card = new Card(Card.Colour.GREEN, Card.CardType.DRAW_ONE);
        hand.addCard(card);
        hand.removeCard(card);
        assertFalse(hand.contains(card));
    }

    @Test
    public void handSize() {
        assertEquals(0, hand.handSize());
        Card card1 = new Card(Card.Colour.RED, Card.CardType.REVERSE);
        Card card2 = new Card(Card.Colour.YELLOW, Card.Number.FIVE);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(2, hand.handSize());
    }

    @Test
    public void handEmpty() {
        assertTrue(hand.handEmpty());
        Card card = new Card(Card.Colour.BLUE, Card.CardType.FLIP);
        hand.addCard(card);
        assertFalse(hand.handEmpty());
    }

    @Test
    public void getCards() {
        Card card1 = new Card(Card.Colour.GREEN, Card.Number.SEVEN);
        Card card2 = new Card(Card.Colour.RED, Card.CardType.WILD);
        hand.addCard(card1);
        hand.addCard(card2);
        assertEquals(2, hand.getCards().size());
        assertTrue(hand.getCards().contains(card1));
        assertTrue(hand.getCards().contains(card2));
    }

}