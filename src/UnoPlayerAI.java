import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UnoPlayerAI extends UnoPlayer{
    UnoFlipModel model;
    ChatGPT gptBot = new ChatGPT();

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

    private String checkHand() {
        return getHand().toString(model.getSide());
    }

    public String askChatGPT(){
        StringBuilder str = new StringBuilder();
        str.append("You are playing Uno. Here is the list of cards in your hand: ");
        str.append(checkHand());
        str.append(". The card currently in play is a " + model.getTopCard().toString(model.getSide()));
        str.append(". What card do you play? Give only the name of the card as your response, or say DRAW CARD if you cannot play");
        //System.out.println(str);
        //System.out.println(gptBot.ask(str.toString()));
        return gptBot.ask(str.toString());

    }

    public String askChatGPTWildColour(){
        StringBuilder str = new StringBuilder();
        str.append("You are playing Uno. Here is the list of cards in your hand: ");
        str.append(checkHand());
        str.append(". The card currently in play is a " + model.getTopCard().toString(model.getSide()));
        str.append(". Suppose you played your WILD card. What colour would you pick? Your have the following options: RED, GREEN, YELLOW, BLUE. " +
                "Give only the name of the colour as your response");
        //System.out.println(str);
        //System.out.println(gptBot.ask(str.toString()));
        return gptBot.ask(str.toString());
    }

    public Card.Colour getBotColour(){
        String colourString = askChatGPTWildColour().toLowerCase();
        switch (colourString) {
            case "red": return Card.Colour.RED;
            case "yellow": return Card.Colour.YELLOW;
            case "green": return Card.Colour.GREEN;
            case "blue": return Card.Colour.BLUE;
            default: return Card.Colour.NONE;
        }
    }

    public void handleBotDecision(){
        System.out.println(getHand().getCards());
        String botDecision = askChatGPT();
        if (botDecision == "DRAW CARD"){
            model.handleDrawCard(this);
        }
        else {
            for (Card c : getHand().getCards()) {
                if (c.toString(model.getSide()) == askChatGPT()) {
                    if (model.getSide()) {
                        if (c.getCardType() == Card.CardType.WILD) {
                            model.handleWildCard(getBotColour(), this, c);
                            break;
                        }
                        if (c.getCardType() == Card.CardType.WILD_DRAW_TWO) {
                            model.handleWildDrawTwoCards(getBotColour(), this, c, model.getDirection());
                            break;
                        }
                        if (c.getCardType() == Card.CardType.SKIP && c.getColour() == model.getTopCard().getColour()) {
                            model.handleSkipCard(this, c, model.getDirection());
                            break;
                        }
                        if (c.getCardType() == Card.CardType.REVERSE && c.getColour() == model.getTopCard().getColour()) {
                            model.handleReverseCard(this, c, model.getDirection());
                            break;
                        }
                        if (c.getCardType() == Card.CardType.FLIP && c.getColour() == model.getTopCard().getColour()) {
                            model.handleFlipCard(this, c);
                            break;
                        }
                        if (model.isValidUnoPlay(c)) {
                            model.handleValidPlay(this, c);
                            break;
                        }
                    }
                    else {
                        if (c.getCardDarkType() == Card.DarkCardType.SKIP_EVERYONE && c.getDarkColour() == model.getTopCard().getDarkColour()) {
                            model.handleSkipEveryoneCard(this, c);
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.DRAW_FIVE) {
                            model.handleDrawFive(this, c, model.getDirection());
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.FLIP && c.getDarkColour() == model.getTopCard().getDarkColour()) {
                            model.handleFlipCard(this, c);
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.REVERSE && c.getDarkColour() == model.getTopCard().getDarkColour()) {
                            model.handleReverseCard(this, c, model.getDirection());
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.WILD) {
                            model.handleWildCard(chooseDarkColour(), this, c);
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.WILD_DRAW_COLOUR) {
                            model.handleWildDrawColourCard(chooseDarkColour(), this, c, model.getDirection());
                            break;
                        }
                        if (model.isValidUnoPlay(c)) {
                            model.handleValidPlay(this, c);
                            break;
                        }
                    }
                }
            }
        }
    }
    /**
     * Returns the corresponding light colour of the dark colour
     * a player chooses after playing a dark wild card.
     * @return the corresponding light colour of the dark colour the current player chooses.
     */
    private static Card.Colour chooseDarkColour() {
        Card.DarkColour[] possibleDarkColours = Card.DarkColour.values();
        String[] darkColourNames = new String[possibleDarkColours.length];

        for (int i = 0; i < possibleDarkColours.length; i++) {
            darkColourNames[i] = possibleDarkColours[i].name();
        }

        String chosenColourName = null;

        while (true) {
            chosenColourName = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose a colour:",
                    "Colour Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    darkColourNames,
                    darkColourNames[0]);

            // Check if a valid colour was chosen
            if (chosenColourName != null) {
                try {
                    Card.Colour chosenCard = Card.Colour.NONE;
                    switch (Card.DarkColour.valueOf(chosenColourName)) {
                        case ORANGE:
                            chosenCard = Card.Colour.RED;
                        case TEAL:
                            chosenCard = Card.Colour.YELLOW;
                        case PINK:
                            chosenCard = Card.Colour.GREEN;
                        case PURPLE:
                            chosenCard = Card.Colour.BLUE;
                    }
                    return chosenCard;
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid selection. Please choose a colour.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "You must choose a colour.");
            }
        }
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
