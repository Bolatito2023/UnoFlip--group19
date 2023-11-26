import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnoPlayerAI extends UnoPlayer{
    UnoFlipModel model;

    /**
     * Constructs new player UnoPlayer.
     *
     * @param name the name of the player.
     * @param deck the game's deck of cards.
     */
    public UnoPlayerAI(String name, Deck deck, UnoFlipModel gameModel) {
        super(name, deck);
        this.model = gameModel;
    }


    public void playRandomValidCard() {
        List<Card> validCards = getValidCards(model.getTopCard(), model.getSide());

        // If there are valid cards, randomly choose one to play
        if (!validCards.isEmpty()) {
            Random random = new Random();
            int index = random.nextInt(validCards.size());
            Card selectedCard = validCards.get(index);
            System.out.println(getPlayerName() + " plays: " + selectedCard.toString(model.getSide()));
            model.handleValidPlay(this, selectedCard);
        } else {
            // If no valid cards, draw a card from the deck
            model.handleDrawCard(this);
        }
    }

    private List<Card> getValidCards(Card topCard, boolean side) {
        List<Card> validCards = new ArrayList<>();

        for (Card card : getHand().getCards()) {
            if (model.isValidUnoPlay(card)) {
                validCards.add(card);
            }
        }
        return validCards;
    }
    /*
    private void handleCard(Card c){
        if (c.getCardType() == Card.CardType.WILD) {
            model.handleWildCard(chooseColour(), model.getCurrentPlayer(), c);
        }
        if (c.getCardType() == Card.CardType.WILD_DRAW_TWO) {
            model.handleWildDrawTwoCards(chooseColour(), model.getCurrentPlayer(), c, model.getDirection());
        }
        if (c.getCardType() == Card.CardType.SKIP && c.getColour() == model.getTopCard().getColour()) {
            model.handleSkipCard(currentPlayer, c, model.getDirection());
        }
        if (c.getCardType() == Card.CardType.REVERSE && c.getColour() == model.getTopCard().getColour()) {
            model.handleReverseCard(model.getCurrentPlayer(), c, model.getDirection());
        }
        if (c.getCardType() == Card.CardType.FLIP && c.getColour() == model.getTopCard().getColour()) {
            model.handleFlipCard(model.getCurrentPlayer(), c);
        }
        if (model.isValidUnoPlay(c)) {
            model.handleValidPlay(model.getCurrentPlayer(), c);
        }
        else {
            gameView.updateMessages("This card cannot be played!");
            gameView.drawCardButton(true);
        }
        else {
        if (c.getCardDarkType() == Card.DarkCardType.SKIP_EVERYONE && c.getDarkColour() == model.getTopCard().getDarkColour()) {
        model.handleSkipEveryoneCard(currentPlayer, c);
        break;
        }
        if (c.getCardDarkType() == Card.DarkCardType.DRAW_FIVE) {
        model.handleDrawFive(currentPlayer, c, model.getDirection());
        break;
        }
        if (c.getCardDarkType() == Card.DarkCardType.FLIP && c.getDarkColour() == model.getTopCard().getDarkColour()) {
        model.handleFlipCard(currentPlayer, c);
        break;
        }
        if (c.getCardDarkType() == Card.DarkCardType.REVERSE && c.getDarkColour() == model.getTopCard().getDarkColour()) {
        model.handleReverseCard(currentPlayer, c, model.getDirection());
        break;
        }
        if (c.getCardDarkType() == Card.DarkCardType.WILD) {
        model.handleWildCard(chooseDarkColour(), currentPlayer, c);
        break;
        }
        if (c.getCardDarkType() == Card.DarkCardType.WILD_DRAW_COLOUR) {
        model.handleWildDrawColourCard(chooseDarkColour(), currentPlayer, c, model.getDirection());
        break;
        }
        if (model.isValidUnoPlay(c)) {
        model.handleValidPlay(currentPlayer, c);
        break;
        } else {
        gameView.updateMessages("Invalid play");
        gameView.drawCardButton(true);
        }
        }
    }
    */
}
