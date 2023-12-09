import java.util.*;
import javax.json.*;
import java.io.StringReader;

public class Hand {
    private ArrayList<Card> cards;

    /**
     * Constructs a hand of cards as an ArrayList.
     */
    public Hand() { cards = new ArrayList<Card>();}

    /**
     * Adds a card to hand.
     * @param card the card to be added to hand.
     */
    public void addCard(Card card){
        cards.add(card);
    }

    /**
     * Removes card from hand.
     * @param card the card to be removed from hand.
     */
    public void removeCard(Card card){
        cards.remove(card);
    }

    /**
     * Checks if hand contains a card.
     * @param card the card to be checked.
     * @return True if card is in hand, otherwise False.
     */
    public boolean contains(Card card) {
        return cards.contains(card);
    }

    /**
     * Returns the size of hand.
     * @return number of cards in hand.
     */
    public int handSize() {
        return cards.size();
    }

    /**
     * Clears hand of all cards.
     */
    public void clear() {
        cards.clear();
    }

    /**
     * Checks if hand is empty.
     * @return True if number of cards is 0, False otherwise.
     */
    public boolean handEmpty() {

        return cards.isEmpty();
    }

    /**
     * Returns cards in hand.
     * @return cards, the arraylist of cards.
     */
    public ArrayList<Card> getCards(){
        return cards;
    }

    //no longer used, except in test
    /*
    public List<Card> getPlayableCards(Card currentCard, Card.Colour chosenColor) {
        List<Card> playableCards = new ArrayList<>();

        for (Card card : cards) {
            if (isCardPlayable(card, currentCard, chosenColor)) {
                playableCards.add(card);
            }
        }

        return playableCards;
    }
    */

    /**
     * Return a string rendering of this Hand. See Card::toString() for
     * notes about how individual cards are rendered.
     */
    public String toString(boolean side) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            str.append(cards.get(i).toString(side));
            if (i < cards.size() - 1) {
                str.append(", ");
            }
        }
        return str.toString();
    }

    public JsonObject saveAttributesToJson() {
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();

        for (Card card : cards) {
            jsonArrayBuilder.add(card.saveAttributesToJson());
        }

        return Json.createObjectBuilder()
                .add("cards", jsonArrayBuilder)
                .build();
    }

}
