import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    private Card cReg;
    private Card cSpecialColour;
    private Card cSpecialWild;
    private Card cSpecialWildDrawTwo;

    //private Card cNumberedWildCard; //shouldn't be able to create this card


    @Before
    public void setUp() {
        cReg = new Card(Card.Colour.RED,Card.Number.SEVEN);
        cSpecialColour = new Card(Card.Colour.BLUE,Card.CardType.SKIP);
        cSpecialWild = new Card(Card.Colour.NONE,Card.CardType.WILD);
        cSpecialWildDrawTwo = new Card(Card.Colour.NONE,Card.CardType.WILD_DRAW_TWO);
        //cNumberedWildCard = new Card(Card.Colour.NONE,Card.CardType.WILD,Card.Number.THREE);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void toString_RegularCard() {
        setUp();
        assertEquals(cReg.toString(), "RED SEVEN");

    }

    @Test
    public void toString_SpecialColourCard() {
        setUp();
        assertEquals(cSpecialColour.toString(), "BLUE SKIP");
    }

    @Test
    public void toString_WildCard() {
        setUp();
        assertEquals(cSpecialWild.toString(), "WILD");

    }

    @Test
    public void toString_WildDrawTwo_Card() {
        setUp();
        assertEquals(cSpecialWildDrawTwo.toString(), "WILD DRAW 2");
    }

    @Test
    public void get_RegularCardFields(){
        setUp();

        assertEquals(cReg.getColour(), Card.Colour.RED);
        assertEquals(cReg.getCardType(), Card.CardType.NUMBER);
        assertEquals(cReg.getNumber(), Card.Number.SEVEN);
    }

    @Test
    public void get_SpecialColourCardFields(){
        setUp();

        assertEquals(cSpecialColour.getColour(), Card.Colour.BLUE);
        assertEquals(cSpecialColour.getCardType(), Card.CardType.SKIP);
        assertNull(cSpecialWild.getNumber());
    }

    @Test
    public void get_WildCardFields(){
        setUp();

        assertEquals(cSpecialWild.getColour(), Card.Colour.NONE);
        assertEquals(cSpecialWild.getCardType(), Card.CardType.WILD);
        assertNull(cSpecialWild.getNumber());
    }

    @Test
    public void set_RegularCardColour_Green(){
        setUp();

        assertEquals(cReg.getColour(), Card.Colour.RED);

        cReg.setColour(Card.Colour.GREEN);
        assertEquals(cReg.getColour(), Card.Colour.GREEN);
    }

    @Test
    public void set_SpecialColourCard_Green(){
        setUp();

        assertEquals(cSpecialColour.getColour(), Card.Colour.BLUE);

        cSpecialColour.setColour(Card.Colour.GREEN);
        assertEquals(cSpecialColour.getColour(), Card.Colour.GREEN);
    }

    /*
    @Test
    public void set_WildCardColour_Green(){
        setUp();

        assertEquals(cSpecialWild.getColour(), Card.Colour.NONE);

        cSpecialWild.setColour(Card.Colour.GREEN);
        assertNotEquals(cSpecialWild.getColour(), Card.Colour.GREEN);
    }

     */

    @Test
    public void scoring_RegularCard(){
        setUp();

        assertEquals(cReg.Scoring(), 7);
    }

    @Test
    public void scoring_SpecialColourCard(){
        setUp();

        assertEquals(cSpecialColour.Scoring(), 20);
    }

    @Test
    public void scoring_WildCard(){
        setUp();

        assertEquals(cSpecialWild.Scoring(), 40);
    }

    @Test
    public void scoring_WildDrawTwoCard(){
        setUp();

        assertEquals(cSpecialWildDrawTwo.Scoring(), 50);
    }


}
