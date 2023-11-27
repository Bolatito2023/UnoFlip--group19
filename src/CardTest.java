
import static org.junit.Assert.*;

public class CardTest {
    private Card lightCard;
    private Card darkCard;

    @org.junit.Before
    public void setUp() throws Exception {
        lightCard = new Card(Card.Colour.BLUE, Card.CardType.WILD);
        darkCard = new Card(Card.Colour.RED, Card.CardType.DRAW_ONE);
        darkCard.setCardSide(false);
    }

    @org.junit.Test
    public void getColour() {
    }

    @org.junit.Test
    public void getDarkColour() {
        Card card = new Card(Card.Colour.BLUE, Card.CardType.WILD_DRAW_TWO);
        assertEquals("PURPLE WILD_DRAW_COLOUR", card.toString(false));
    }

    @org.junit.Test
    public void getCardType() {
        Card wildCard = new Card(Card.Colour.BLUE, Card.CardType.WILD);
        assertEquals(Card.CardType.WILD, wildCard.getCardType());

        Card numberCard = new Card(Card.Colour.GREEN, Card.Number.THREE);
        assertEquals(Card.CardType.NUMBER, numberCard.getCardType());

    }

    @org.junit.Test
    public void getCardDarkType() {
        Card card = new Card(Card.Colour.NONE, Card.Number.ONE);
        assertEquals(Card.DarkCardType.NUMBER, card.getCardDarkType());
        // Add more assertions as needed
    }

    @org.junit.Test
    public void getNumber() {
        assertEquals("ONE", darkCard.getNumberAsWord());
    }

    @org.junit.Test
    public void getSide() {
        Card card = new Card(Card.Colour.GREEN, Card.CardType.DRAW_ONE);
        assertTrue(card.getSide().equals("LIGHT"));
        card.setCardSide(false);
        assertTrue(card.getSide().equals("DARK"));
    }

    @org.junit.Test
    public void flipCardSide() {
        Card card = new Card(Card.Colour.YELLOW, Card.CardType.REVERSE);
        assertTrue(card.getSide().equals("LIGHT"));
        card.flipCardSide();
        assertTrue(card.getSide().equals("DARK"));
    }
}