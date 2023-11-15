import java.util.*;

public class Hand {
    private ArrayList<Card> cards;


    public Hand() { cards = new ArrayList<Card>();}


    public void addCard(Card card){
        cards.add(card);
    }
    public void removeCard(Card card){
        cards.remove(card);
    }

    public boolean contains(Card card) {
        return cards.contains(card);
    }


    public int handSize() {
        return cards.size();
    }
    //To clear hands when restarting game

    public void clear() {
        cards.clear();
    }
    public boolean handEmpty() {

        return cards.isEmpty();
    }
    public ArrayList<Card> getCards(){
        return cards;
    }


    public List<Card> getPlayableCards(Card currentCard, Card.Colour chosenColor) {
        List<Card> playableCards = new ArrayList<>();

        for (Card card : cards) {
            if (isCardPlayable(card, currentCard, chosenColor)) {
                playableCards.add(card);
            }
        }

        return playableCards;
    }

    private boolean isCardPlayable(Card card, Card currentCard, Card.Colour chosenColor) {
        // Check if the card is Wild or Wild Draw Two
        if (card.getCardType() == Card.CardType.WILD || card.getCardType() == Card.CardType.WILD_DRAW_TWO) {
            return true;
        }

        if (currentCard.getCardType() == Card.CardType.REVERSE) {
            // Check if the card matches the color of the Reverse card or if it's another Reverse card
            return (card.getCardType() == Card.CardType.REVERSE || currentCard.getColour() == card.getColour());
        }

        if (currentCard.getCardType() == Card.CardType.SKIP) {
            // Check if the card matches the current card's color or the chosen color of a Wild card
            return (card.getCardType() == Card.CardType.SKIP || currentCard.getColour() == card.getColour() || card.getColour() == chosenColor);
        }

        if (currentCard.getCardType() == Card.CardType.FLIP) {
            // Check if the card matches the current card's color or the chosen color of a Wild card
            return (card.getCardType() == Card.CardType.FLIP || currentCard.getColour() == card.getColour() || card.getColour() == chosenColor);
        }

        // Check if there is a Wild card on the discard pile and the card matches the chosen color
        if (currentCard.getCardType() == Card.CardType.DRAW_ONE) {
            return (card.getCardType() == Card.CardType.DRAW_ONE || currentCard.getColour() == card.getColour() || card.getColour() == chosenColor);
        }
        if (currentCard.getCardType() == Card.CardType.WILD_DRAW_TWO) {
            return (card.getCardType() == Card.CardType.WILD_DRAW_TWO || currentCard.getColour() == card.getColour() || card.getColour() == chosenColor);
        }

        if (currentCard.getCardType() == Card.CardType.WILD) {
            return (card.getCardType() == Card.CardType.WILD || currentCard.getColour() == card.getColour() || card.getColour() == chosenColor );
        }

        // Check if the card matches the current color, the chosen color, or if it's a Wild card
        if(card.getColour() == currentCard.getColour() || card.getColour() == chosenColor|| card.getNumber() == currentCard.getNumber()){
            return true;
        }
        // Check if the card's number matches the current card's number
        if(card.getNumber() == currentCard.getNumber()){
            return true;
        }
        return false;
    }


    /**
     * Return a string rendering of this Hand. See Card::toString() for
     * notes about how individual cards are rendered.
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            str.append(cards.get(i).toString());
            if (i < cards.size() - 1) {
                str.append(", ");
            }
        }
        return str.toString();
    }
}
