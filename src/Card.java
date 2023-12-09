import netscape.javascript.JSObject;
import javax.json.Json;
import javax.json.JsonObject;

/**
 * A (playing) card is a rank and a suit.
 *
 * @version 1.0
 */
public class Card {
    public enum Colour {BLUE, GREEN, RED, YELLOW, NONE}

    public enum DarkColour {ORANGE, PINK, PURPLE, TEAL, NONE}
    public enum CardType {NUMBER, DRAW_ONE, REVERSE, SKIP, FLIP, WILD, WILD_DRAW_TWO}

    public enum DarkCardType {NUMBER, DRAW_FIVE, REVERSE, SKIP_EVERYONE, FLIP, WILD, WILD_DRAW_COLOUR}

    public enum Number {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, NULL}

    private Colour colour;
    private DarkColour darkColour;
    private CardType cardT;
    private DarkCardType darkCardT;
    private Number num;

    private boolean cardSide = true; //true for light side, false for dark side. Might not be used

    /**
     * Constructs a card with a colour and type.
     * @param colour card colour.
     * @param card card type.
     */
    public Card(Colour colour, CardType card) {
        this.colour = colour;
        this.cardT = card;
        this.num = Number.NULL;
        this.darkColour = initializeDarkColour(colour);
        this.darkCardT = initializeDarkType(card);
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
        this.darkColour = initializeDarkColour(colour);
        this.darkCardT = DarkCardType.NUMBER;
    }

    public Card(Colour colour, DarkColour darkColour, Number number, CardType cardType, DarkCardType darkCardType) {
        this.colour = colour;
        this.darkColour = darkColour;
        this.num = number;
        this.cardT = cardType;
        this.darkCardT = darkCardType;
    }

    private DarkColour initializeDarkColour(Colour colour) {
        switch (colour) {
            case RED: return DarkColour.ORANGE;
            case YELLOW: return DarkColour.TEAL;
            case GREEN: return DarkColour.PINK;
            case BLUE: return DarkColour.PURPLE;
            default: return DarkColour.NONE;
        }
    }

    private DarkCardType initializeDarkType(CardType card) {
        switch (card) {
            case WILD: return DarkCardType.WILD;
            case WILD_DRAW_TWO: return DarkCardType.WILD_DRAW_COLOUR;
            case DRAW_ONE: return DarkCardType.DRAW_FIVE;
            case FLIP: return DarkCardType.FLIP;
            case REVERSE: return DarkCardType.REVERSE;
            case SKIP: return DarkCardType.SKIP_EVERYONE;
            default: return DarkCardType.NUMBER;
        }
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
    public String toString(boolean side) {
        String str = "";
        if (side) {
            switch (colour) {
                case BLUE:
                    str += "BLUE ";
                    break;
                case GREEN:
                    str += "GREEN ";
                    break;
                case RED:
                    str += "RED ";
                    break;
                case YELLOW:
                    str += "YELLOW ";
                    break;
                case NONE:
                    str += "";
                    break;
                default:
                    throw new IllegalArgumentException("No such Colour");
            }
        }
        else {
            switch (darkColour) {
                case ORANGE:
                    str += "ORANGE ";
                    break;
                case TEAL:
                    str += "TEAL ";
                    break;
                case PINK:
                    str += "PINK ";
                    break;
                case PURPLE:
                    str += "PURPLE ";
                    break;
                case NONE:
                    str += "";
                    break;
                default:
                    throw new IllegalArgumentException("No such Colour");
            }
        }

        if (side) {
            switch (cardT) {
                case NUMBER:
                    str += getNumberAsWord();
                    break;
                case DRAW_ONE:
                    str += "DRAW_ONE";
                    break;
                case REVERSE:
                    str += "REVERSE";
                    break;
                case SKIP:
                    str += "SKIP";
                    break;
                case FLIP:
                    str += "FLIP";
                    break;
                case WILD:
                    str += "WILD";
                    break;
                case WILD_DRAW_TWO:
                    str += "WILD_DRAW_TWO";
                    break;
                default:
                    throw new IllegalArgumentException("No such CardType");
                    //break;
            }
        }
        else {
            switch (darkCardT) {
                case NUMBER:
                    str += getNumberAsWord();
                    break;
                case DRAW_FIVE:
                    str += "DRAW_FIVE";
                    break;
                case REVERSE:
                    str += "REVERSE";
                    break;
                case SKIP_EVERYONE:
                    str += "SKIP_EVERYONE";
                    break;
                case FLIP:
                    str += "FLIP";
                    break;
                case WILD:
                    str += "WILD";
                    break;
                case WILD_DRAW_COLOUR:
                    str += "WILD_DRAW_COLOUR";
                    break;
                default:
                    throw new IllegalArgumentException("No such CardType");
                    //break;
            }
        }
        return str;
    }

    /**
     * Returns the card colour.
     * @return colour the card's colour.
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Returns the card dark colour.
     * @return darkColor the card's dark colour.
     */
    public DarkColour getDarkColour() {
        return darkColour;
    }

    /**
     * Returns the card type.
     * @return cardT the card's type.
     */
    public CardType getCardType() {
        return cardT;
    }

    /**
     * Returns the card dark type.
     * @return darkCardT the card's dark type.
     */
    public DarkCardType getCardDarkType() {
        return darkCardT;
    }

    /**
     * Returns the card number.
     * @return card's number.
     */
    public Number getNumber(){
        return num;
    }

    /**
     * Returns the card side as a String representation.
     * @return String representation of the current card side.
     */
    public String getSide() {
        if (cardSide) { return "LIGHT";}
        else { return "DARK";}
    }

    /**
     * Sets the current card side.
     * @param side the side that the card is to be set.
     */
    public void setCardSide(boolean side) {
        this.cardSide = side;
    }

    /**
     * Reverses the card's side.
     */
    public void flipCardSide() {
        cardSide = !cardSide;
    }
    /**
     * Sets the card colour.
     * @param colour the card's colour.
     */
    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public JsonObject saveAttributesToJson(){
        JsonObject jsObject = Json.createObjectBuilder()
                .add("colour", colour.toString())
                .add("darkCardT", darkCardT.toString())
                .add("num", num.toString())
                .build();
        return jsObject;
    }

}







