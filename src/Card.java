/**
 * A (playing) card is a rank and a suit.
 *
 * @version 1.0
 */
public class Card {
    public enum Colour {BLUE, GREEN, RED, YELLOW,NONE}

    public enum CardType {NUMBER, DRAW_ONE, REVERSE, SKIP, FLIP, WILD, WILD_DRAW_TWO}

    public enum Number {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE}

    //private int number;

    private Colour colour;
    private CardType cardT;
    private Number num;

    /**
     * Constructs a card with a colour and type.
     * @param colour card colour.
     * @param card card type.
     */
    public Card(Colour colour, CardType card ) {
        this.colour = colour;
        this.cardT = card;
        //this.number = -1; //for non number cards
    }

    /**
     * Constructs a card with a colour and number.
     * @param colour card colour.
     * @param number card type.
     */
    public Card(Colour colour, Number number) {
        this.colour = colour;
        this.cardT = CardType.NUMBER;
        this.num = number;
    }

    /**
     * Returns a string representation of enum Number.
     * @return string representation of Number.
     */
    public String getNumberAsWord() {
        switch (num) {
            case ONE:
                return "ONE";
            case TWO:
                return "TWO";
            case THREE:
                return "THREE";
            case FOUR:
                return "FOUR";
            case FIVE:
                return "FIVE";
            case SIX:
                return "SIX";
            case SEVEN:
                return "SEVEN";
            case EIGHT:
                return "EIGHT";
            case NINE:
                return "NINE";
            default:
                return "";
        }
    }

    /**
     * Returns a string representation of the Uno card.
     * @return string representation of the card colour, and type. If type is NUMBER, return a string
     * representation of the number.
     */
    public String toString() {
        String str = "";

        switch (colour) {
            case BLUE:
                str+="BLUE ";
                break;
            case GREEN:
                str+="GREEN ";
                break;
            case RED:
                str+="RED ";
                break;
            case YELLOW:
                str+="YELLOW ";
                break;
            case NONE:
                str+="";
                break;
            default:throw new IllegalArgumentException("No such Colour");
        }

        switch (cardT) {

            case NUMBER:
                str += getNumberAsWord();
                break;
            case DRAW_ONE:
                str+= "+1";
                break;
            case REVERSE:
                str+= "REVERSE";
                break;
            case SKIP:
                str+= "SKIP";
                break;
            case FLIP:
                str+= "FLIP";
                break;
            case WILD:
                str+= "WILD";
                break;
            case WILD_DRAW_TWO:
                str+= "WILD DRAW 2";
                break;
            default:throw new IllegalArgumentException("No such CardType");
                //break;

        }
        return str;
    }

    /**
     * Returns the card colour.
     * @return card's colour.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Returns the card type.
     * @return card's type.
     */
    public CardType getCardType() {
        return cardT;
    }

    /**
     * Returns the card number.
     * @return card's number.
     */
    public Number getNumber(){
        return num;
    }

    /**
     * Sets the card colour.
     * @param colour the card's colour.
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

}







