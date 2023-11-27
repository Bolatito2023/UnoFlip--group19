import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;



import static org.junit.Assert.*;

public class DeckTest {
    private Deck deck;

    @Before
    public void setUp() throws Exception {
        deck = new Deck();
    }


    @Test
    public void shuffle() {
        deck.giveDeck();
        ArrayList<Card> originalDeck = new ArrayList<>(deck.getDeck());
        deck.shuffle();
        ArrayList<Card> shuffledDeck = deck.getDeck();
        assertNotEquals(originalDeck, shuffledDeck); // Assuming shuffle produces a different order
    }


    @Test
    public void getDeck() {
        deck.giveDeck();
        ArrayList<Card> deckCards = deck.getDeck();
        assertNotNull(deckCards);
        assertEquals(0, deck.getDiscardedCards().size()); // Deck should not contain discarded cards
    }

    @Test
    public void getDiscardedCards() {
        assertNotNull(deck.getDiscardedCards());
        assertEquals(0, deck.getDiscardedCards().size());
    }

    @Test
    public void isEmpty() {
        assertTrue(deck.isEmpty());
        deck.giveDeck();
        assertFalse(deck.isEmpty());
    }

    @Test
    public void draw() {
        deck.giveDeck();
        int originalSize = deck.getDeck().size();
        Card drawnCard = deck.draw();
        assertNotNull(drawnCard);
        assertEquals(originalSize - 1, deck.getDeck().size());

    }
    @Test
    public void drawFromEmptyDeck() {
        deck.giveDeck();
        while (!deck.isEmpty()) {
            deck.draw();
        }
        Card drawnCard = deck.draw();
        assertNull(drawnCard);
    }

    @Test
    public void discard() {
        Card card = new Card(Card.Colour.BLUE, Card.CardType.SKIP);
        deck.discard(card);
        assertTrue(deck.getDiscardedCards().contains(card));
    }


}