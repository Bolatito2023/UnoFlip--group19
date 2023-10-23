import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.awt.desktop.SystemEventListener;

import static org.junit.Assert.*;

public class UnoPlayerTest {
    UnoPlayer player1;
    Deck testDeck;

    @Before
    public void setUp(){
        testDeck = new Deck();
        testDeck.giveDeck();
        testDeck.shuffle();
        player1 = new UnoPlayer("Player 1", testDeck);
    }

    @After
    public void tearDown(){
    }

    @Test
    public void playCard() {
        setUp();

        Card c0 = player1.getHand().getCards().get(0);
        assertTrue(player1.getHand().contains(c0));
        assertFalse(testDeck.getDiscardedCards().contains(c0));

        player1.playCard(c0);
        assertFalse(player1.getHand().contains(c0));
        assertTrue(testDeck.getDiscardedCards().contains(c0));
    }

    @Test
    public void drawCard() {
        setUp();

        Card cYellowOne = new Card(Card.Colour.YELLOW, Card.Number.ONE);
        assertFalse(player1.getHand().contains(cYellowOne));

        player1.drawCard(cYellowOne);
        assertTrue(player1.getHand().contains(cYellowOne));
    }

    @Test
    public void getHand() {
        setUp();

        assertEquals(player1.getHand().handSize(), 7);
    }

    @Test
    public void hasUno() {
        setUp();

        assertFalse(player1.hasUno());
        assertEquals(player1.getHand().handSize(), 7);
        for(int k=0; k<6; k++){
            Card cPlay;
            cPlay = player1.getHand().getCards().get(0);
            player1.playCard(cPlay);
        }

        assertTrue(player1.hasUno());
        player1.playCard(player1.getHand().getCards().get(0));
        assertFalse(player1.hasUno());
    }

    @Test
    public void emptyHand() {
        setUp();

        assertFalse(player1.emptyHand());

        assertEquals(player1.getHand().handSize(), 7);
        for(int k=0; k<7; k++){
            Card cPlay;
            cPlay = player1.getHand().getCards().get(0);
            player1.playCard(cPlay);
        }
        assertEquals(player1.getHand().handSize(), 0);
        assertTrue(player1.emptyHand());
    }

    @Test
    public void resetHand() {
        setUp();

        assertFalse(player1.emptyHand());
        player1.resetHand();
        assertTrue(player1.emptyHand());
    }

    @Test
    public void getPlayerName() {
        setUp();

        assertEquals(player1.getPlayerName(), "Player 1");
    }
}