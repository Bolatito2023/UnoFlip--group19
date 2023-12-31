import javax.swing.*;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class UnoPlayerAI extends UnoPlayer {
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

    @Override
    public void playCard(Card card){
        if (hand.contains(card)){
            hand.removeCard(card);
            deck.discard(card);
        }
    }

    private String checkHand() {
        return getHand().toString(model.getSide());
    }

    public String askChatGPT() {
        StringBuilder str = new StringBuilder();
        str.append("You are playing Uno. Here is the list of cards in your hand: ");
        str.append(checkHand());
        str.append(". The card currently in play is a " + model.getTopCard().toString(model.getSide()));
        str.append(". What card do you play? Give only the name of the card as your response, or say DRAW CARD if you cannot play");
        System.out.println(str);
        //System.out.println(gptBot.ask(str.toString()));
        return gptBot.ask(str.toString());

    }

    public String askChatGPTWildColour() {
        StringBuilder str = new StringBuilder();
        str.append("You are playing Uno. Here is the list of cards in your hand: ");
        str.append(checkHand());
        str.append(". The card currently in play is a " + model.getTopCard().toString(model.getSide()));
        str.append(". Suppose you played your WILD card. What colour would you pick? Your have the following options: RED, GREEN, YELLOW, BLUE. " +
                "Give only the name of the colour as your response");
        System.out.println(str);
        //System.out.println(gptBot.ask(str.toString()));
        return gptBot.ask(str.toString());
    }

    public Card.Colour getBotColour() {
        String colourString = askChatGPTWildColour().toLowerCase();
        switch (colourString) {
            case "red":
                return Card.Colour.RED;
            case "yellow":
                return Card.Colour.YELLOW;
            case "green":
                return Card.Colour.GREEN;
            case "blue":
                return Card.Colour.BLUE;
            default:
                return Card.Colour.NONE;
        }
    }

    public void handleBotDecision() {
        System.out.println(checkHand());
        String botDecision = askChatGPT();
        System.out.println("ChatGPT Response: " + botDecision);

        JOptionPane.showMessageDialog(null, "AI wants to play: " + botDecision);

        if (botDecision == "DRAW CARD") {
            model.handleDrawCard(this);
        } else {
            for (Card c : getHand().getCards()) {
                if (c.toString(model.getSide()).equals(botDecision)) {
                    JOptionPane.showMessageDialog(null, "Card in Hand: " + c.toString(model.getSide()));

                    if (model.getSide()) {
                        if (c.getCardType() == Card.CardType.WILD) {
                            model.handleWildCard(getBotColour(), c);
                            break;
                        }
                        if (c.getCardType() == Card.CardType.WILD_DRAW_TWO) {
                            model.handleWildDrawTwoCards(getBotColour(), c, model.getDirection());
                            break;
                        }
                        if (c.getCardType() == Card.CardType.SKIP && c.getColour() == model.getTopCard().getColour()) {
                            model.handleSkipCard(c, model.getDirection());
                            break;
                        }
                        if (c.getCardType() == Card.CardType.REVERSE && c.getColour() == model.getTopCard().getColour()) {
                            model.handleReverseCard(c, model.getDirection());
                            break;
                        }
                        if (c.getCardType() == Card.CardType.FLIP && c.getColour() == model.getTopCard().getColour()) {
                            model.handleFlipCard(c);
                            break;
                        }
                        if (model.isValidUnoPlay(c)) {
                            model.handleValidPlay(c);
                            break;
                        }
                    } else {
                        if (c.getCardDarkType() == Card.DarkCardType.SKIP_EVERYONE && c.getDarkColour() == model.getTopCard().getDarkColour()) {
                            model.handleSkipEveryoneCard(c);
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.DRAW_FIVE) {
                            model.handleDrawFive(c, model.getDirection());
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.FLIP && c.getDarkColour() == model.getTopCard().getDarkColour()) {
                            model.handleFlipCard(c);
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.REVERSE && c.getDarkColour() == model.getTopCard().getDarkColour()) {
                            model.handleReverseCard(c, model.getDirection());
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.WILD) {
                            model.handleWildCard(chooseDarkColour(), c);
                            break;
                        }
                        if (c.getCardDarkType() == Card.DarkCardType.WILD_DRAW_COLOUR) {
                            model.handleWildDrawColourCard(chooseDarkColour(), c, model.getDirection());
                            break;
                        }
                        if (model.isValidUnoPlay(c)) {
                            model.handleValidPlay(c);
                            break;
                        }
                    }
                }
            }
        }
        model.getNextCurrentPlayer();
    }

    /**
     * Returns the corresponding light colour of the dark colour
     * a player chooses after playing a dark wild card.
     *
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
    public JsonObject saveAttributesToJson() {
        JsonArrayBuilder handArrayBuilder = Json.createArrayBuilder();

        // Serialize hand
        for (Card card : getHand().getCards()) {
            handArrayBuilder.add(card.saveAttributesToJson());
        }

        return Json.createObjectBuilder()
                .add("playerName", getPlayerName())

                .add("unoCalled", getUnoCalled())
                .add("remindedUno", hasRemindedUno())
                .add("hand", handArrayBuilder)
                .build();
    }
}