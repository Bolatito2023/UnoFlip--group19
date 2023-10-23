import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class UnoFlipTest {
    Deck testDeck;
    List<UnoPlayer> players;
    UnoFlip testGame;

    @Before
    public void setUp(){
        testDeck = new Deck();
        players = new ArrayList<>();

        testDeck.giveDeck();
        testDeck.shuffle();

        UnoPlayer player1 = new UnoPlayer("Player 1", testDeck);
        UnoPlayer player2 = new UnoPlayer("Player 2", testDeck);
        players.add(player1);
        players.add(player2);

        testGame = new UnoFlip(players, testDeck);
    }

    @After
    public void tearDown(){
    }

    @Test
    public void reverseDirection() {
        setUp();

        assertEquals(testGame.getDirection(), UnoFlip.Direction.FORWARD);
        testGame.reverseDirection();
        assertEquals(testGame.getDirection(), UnoFlip.Direction.BACKWARD);
    }

}