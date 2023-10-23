/**
 * A (playing) card is a rank and a suit.
 *
 * @version 0.0
 */
public class Card {
    public enum Colour {BLUE, GREEN, RED, YELLOW,NONE}

    public enum CardType {NUMBER, DRAW_ONE, REVERSE, SKIP, FLIP, WILD, WILD_DRAW_TWO}

    public enum Number {ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE}

    //private int number;

    private Colour colour;
    private CardType cardT;
    private Number num;
    public Card(Colour color, CardType card ) {
        this.colour = color;
        this.cardT = card;
        //this.number = -1; //for non number cards
    }
    public Card(Colour color, Number number) {
        this.colour = color;
        this.cardT = CardType.NUMBER;
        this.num = number;
    }
    public Card(Colour color, CardType card, Number number) {
        this.colour = color;
        this.cardT = card;
        this.num = number;
    }
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
    public Colour getColour() {

        return colour;
    }

    public CardType getCardType() {

        return cardT;
    }
    public Number getNumber(){

        return num;
    }
    public void setColour(Colour colour) {

        this.colour = colour;
    }
    public int Scoring(){
        int total =0;
        if (cardT == CardType.SKIP || cardT == CardType.REVERSE || cardT == CardType.FLIP) {
            total+= 20;
        }
        if (cardT == CardType.WILD_DRAW_TWO ) {
            total+= 50;
        }
        if (num == Number.ONE) {
            total+= 1;
        }
        if (num == Number.TWO) {
            total+= 2;
        }
        if (num == Number.THREE) {
            total+= 3;
        }
        if (num == Number.FOUR) {
            total+= 4;
        }
        if (num == Number.FIVE) {
            total+= 5;
        }
        if (num == Number.SIX) {
            total+= 6;
        }
        if (num == Number.SEVEN) {
            total+= 7;
        }
        if (num == Number.EIGHT) {
            total+= 8;
        }
        if (num == Number.NINE) {
            total+= 9;
        }
        if (cardT == CardType.WILD) {
            total+= 40;
        }
        if (cardT == CardType.DRAW_ONE) {
            total+= 10;
        }
        //System.out.println("Illegal card!!");
        //return -100;
        return total;
    }


}







