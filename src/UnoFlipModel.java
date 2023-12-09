import javax.swing.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import javax.json.*;
import java.util.ArrayList;
import java.util.List;

public class UnoFlipModel {
    boolean direction = true;

    public enum Side {LIGHT, DARK}

    private List<UnoPlayer> players;
    private Card currentCard;
    public Deck deck;
    private boolean side; //true for light side, false for dark side
    private int currentPlayerIndex;
    private int roundNumber;
    UnoFlipModelViewFrame view;
    private UnoPlayer currentPlayer;
    private Card lastTopCard;
    private Card lastSelectedCard;

    /**
     * Constructs an UnoFlip game model.
     * @param numPlayers the number of players in game.
     * @param deck the game's card deck.
     */
    public UnoFlipModel(int numPlayers, int numAIPlayers, Deck deck) {
        players = new ArrayList<>();
        initializePlayers(numPlayers, numAIPlayers, deck);
        direction = true;
        side = true;
        currentPlayerIndex = 0;
        this.deck = deck;
        currentCard = deck.draw();
        roundNumber = 1;
    }

    /**
     * Resets the initial card in play when game is replayed.
     */
    public void resetTopCard(){
        currentCard = deck.draw(); //Old top card put back in deck with reAdd() call in resetPlayerHands()
        currentPlayerIndex = 0;
    }

    public List<UnoPlayer> getPlayers(){
        return players;
    }

    public void addView(UnoFlipModelViewFrame view) {
        this.view = view;
    }

    /**
     * Initializes the players of the game and allows user to enter their names.
     * @param numPlayers The number of players in the game.
     */
    private void initializePlayers(int numPlayers, int numAIPlayers, Deck deck) {

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = JOptionPane.showInputDialog(null,
                    "Enter Player " + i + "'s name",
                    "Name:",
                    JOptionPane.QUESTION_MESSAGE);
            UnoPlayer player = new UnoPlayer(playerName, deck);
            players.add(player);
        }

        for (int i = 1; i <= numAIPlayers; i++) {
            String playerName = "AI " + i;
            UnoPlayerAI playerAI = new UnoPlayerAI(playerName, deck, this);
            players.add(playerAI);
        }
    }

    /**
     * Clears each player's hand and deals new cards to them
     */
    public void resetPlayerHands() {
        for(UnoPlayer p : players) {
            int numCards = p.getHand().handSize();
            for (int i=(numCards-1); i>=0; i--) {
                p.clearHand(i);
            }
        }
        deck.reAdd();
        for(UnoPlayer p : players) {
            p.dealCards();
        }
    }

    /**
     * Checks if the card can be played.
     * @param card The card to be checked.
     * @return True if the card can be played; otherwise, false.
     */
    public boolean isValidUnoPlay(Card card) {
        boolean isvalid;
        if (side) {
            if (card.getColour() == Card.Colour.NONE){
                isvalid = card.getColour() == currentCard.getColour() || card.getCardType() == currentCard.getCardType();
            }
            else {
                isvalid = card.getColour() == currentCard.getColour() || card.getNumber() == currentCard.getNumber();
            }
        }
        else {
            if (card.getDarkColour() == Card.DarkColour.NONE){
                isvalid = card.getDarkColour() == currentCard.getDarkColour() || card.getCardDarkType() == currentCard.getCardDarkType();
            }
            else {
                isvalid = card.getDarkColour() == currentCard.getDarkColour() || card.getNumber() == currentCard.getNumber();
            }
        }
        return isvalid;
    }

    /**
     * Calculates the score for the winning player.
     * @param winner player that won.
     */
    private void calculateScoreForwinner(UnoPlayer winner) {
        int finalScore = 0;

        // Iterate through the remaining players' hands
        for (UnoPlayer opponent : players) {
            if (opponent != winner) {
                for (Card card : opponent.getHand().getCards()) {
                    Card.CardType cardT = card.getCardType();
                    Card.Number num = card.getNumber();

                    if (cardT == Card.CardType.SKIP || cardT == Card.CardType.REVERSE || cardT == Card.CardType.FLIP) {
                        finalScore+= 20;
                    }
                    if (cardT == Card.CardType.WILD_DRAW_TWO ) {
                        finalScore+= 50;
                    }
                    if (num == Card.Number.ONE) {
                        finalScore+= 1;
                    }
                    if (num == Card.Number.TWO) {
                        finalScore+= 2;
                    }
                    if (num == Card.Number.THREE) {
                        finalScore+= 3;
                    }
                    if (num == Card.Number.FOUR) {
                        finalScore+= 4;
                    }
                    if (num == Card.Number.FIVE) {
                        finalScore+= 5;
                    }
                    if (num == Card.Number.SIX) {
                        finalScore+= 6;
                    }
                    if (num == Card.Number.SEVEN) {
                        finalScore+= 7;
                    }
                    if (num == Card.Number.EIGHT) {
                        finalScore+= 8;
                    }
                    if (num == Card.Number.NINE) {
                        finalScore+= 9;
                    }
                    if (cardT == Card.CardType.WILD) {
                        finalScore+= 40;
                    }
                    if (cardT == Card.CardType.DRAW_ONE) {
                        finalScore+= 10;
                    }
                }
            }
        }

        winner.updateScore(finalScore);
        System.out.println("Player " + winner.getPlayerName() + " scored " + finalScore + " points and won!");
    }

    /**
     * Handles drawing a card from the deck.
     *
     * @param currentPlayer The current player who is drawing a card.
     */
    protected void handleDrawCard(UnoPlayer currentPlayer) {
        Card drawnCard = deck.draw();
        currentPlayer.drawCard(drawnCard);
        view.updateDrawCardMessagePanel("Player " + currentPlayer.getPlayerName() + " drew card: ", drawnCard);
        view.nextPlayerButton(true);
        view.drawCardButton(false);

        view.update();
        view.cardButtons(false);
    }

    /**
     * Handles Uno status and reminds the player to say Uno if they are at one card.
     *
     * @param scanner the input scanner.
     * @param currentPlayer the current player.
     */
    private void handleUnoStatus(Scanner scanner, UnoPlayer currentPlayer) {
        if (currentPlayer.hasUno() && !currentPlayer.hasRemindedUno()) {
            System.out.println("Player " + currentPlayer.getPlayerName() + ", you have Uno! Don't forget to say it.");
            currentPlayer.setRemindedUno(true);
        }
        if (currentPlayer.getHand().handSize() == 1) {
            System.out.println("Type 'UNO' to say it: ");
            String unoInput = scanner.next().trim().toUpperCase();
            if (unoInput.equals("UNO")) {
                currentPlayer.sayUno();
            }
        }
    }

    /**
     * Handles the outcome when a player wins or fails to say Uno.
     *
     * @param currentPlayer The current player.
     */
    private void handleWinOrPenalty(UnoPlayer currentPlayer) {
        if (currentPlayer.getUnoCalled()) {
            System.out.println("Player " + currentPlayer.getPlayerName() + " wins!");
            calculateScoreForwinner(currentPlayer);
        } else {
            System.out.println("Player " + currentPlayer.getPlayerName() + " did not say Uno and draws 2 cards.");
            currentPlayer.drawCard(deck.draw());
            currentPlayer.drawCard(deck.draw());
            currentPlayer.setRemindedUno(false);
        }
    }

    /**
     * Handles playing a Wild card and allows the player to choose the color.
     *
     * @param colour        The input for the colour chosen.
     * @param selectedCard  The Wild card to play.
     */
    public void handleWildCard(Card.Colour colour, Card selectedCard) {
        playCard(selectedCard);
        currentCard = new Card(colour, Card.CardType.WILD);

        view.updateMessages(colour + " has been chosen.");
        view.update();
    }

    /**
     * Handles playing a Flip card by switching the cards' sides.
     * @param selectedCard  The Flip card to play.
     */
    public void handleFlipCard(Card selectedCard) {
        playCard(selectedCard);
        side = !side;

        String flipMessage;
        if (side) { flipMessage = "Light";}
        else { flipMessage = "Dark";}
        view.updateMessages("Cards are now flipped to " + flipMessage);
        view.update();
    }

    /**
     * Handles playing a Skip Everyone card by allowing the current player to play again.
     * @param selectedCard  The Skip Everyone card to play.
     */
    public void handleSkipEveryoneCard(Card selectedCard) {
        playCard(selectedCard);
        view.updateMessages("All players are skipped.");
        view.update();
    }

    /**
     * Handles playing a Draw Five card by forcing the next player to draw five cards and skips their turn.
     * @param selectedCard  The Draw Five card to play.
     * @param direction     The direction of play.
     */
    public void handleDrawFive(Card selectedCard, boolean direction) {
        playCard(selectedCard);

        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        UnoPlayer nextPlayer = players.get(currentPlayerIndex);
        for(int i = 0; i < 5; ++i) {
            nextPlayer.drawCard(deck.draw());
        }
        view.updateMessages("Player " + nextPlayer.getPlayerName() + " must draw 5 cards.");
        view.update();
    }

    /**
     * Handles playing a Wild Draw Colour card by forcing the next player to draw until
     * they draw a card of the selected colour and skips their turn.
     * @param colour        The corresponding light colour selected for the dark Wild Draw Colour card.
     * @param selectedCard  The Wild Draw Colour card to play.
     * @param direction     The direction of play.
     */
    public void handleWildDrawColourCard(Card.Colour colour, Card selectedCard, boolean direction) {
        playCard(selectedCard);
        currentCard = new Card(colour, Card.CardType.WILD_DRAW_TWO);

        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        UnoPlayer nextPlayer = players.get(currentPlayerIndex);
        Card drawnCard;
        do {
            drawnCard = deck.draw();
            nextPlayer.drawCard(drawnCard);
        }
        while(currentCard.getDarkColour() != drawnCard.getDarkColour());
        view.updateMessages(currentCard.getDarkColour() + " has been chosen. Player " + nextPlayer.getPlayerName() + " has to draw cards until they get a matching colour.");

        view.update();
    }

    /**
     * Handles playing a Draw One card by forcing the next player to draw a card and skips their turn.
     * @param selectedCard  The Draw One card to play.
     * @param direction     The direction of play.
     */
    public void handleDrawOne(Card selectedCard, boolean direction) {
        playCard(selectedCard);

        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        UnoPlayer nextPlayer = players.get(currentPlayerIndex);
        nextPlayer.drawCard(deck.draw());

        view.updateMessages("Player " + nextPlayer.getPlayerName() + " must draw a card.");
        view.update();
    }
    /**
     * Handles playing a Skip card. If the card matches the color of the top card,
     * it skips the next player's turn.
     *
     * @param selectedCard  The Skip card to play.
     * @param direction     The direction of play.
     */
    public void handleSkipCard(Card selectedCard, boolean direction) {
        playCard(selectedCard);
        if (selectedCard.getColour() == currentCard.getColour() || currentCard.getCardType() == Card.CardType.SKIP) {
            playCard(selectedCard);
            currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();

            view.update();
        } else {
            view.updateMessages("Invalid play. The card must match the color of the top card.");
        }
    }

    /**
     * Handles playing a Reverse card. If the card matches the color of the top card,
     * it reverses the direction of play.
     *
     * @param selectedCard  The Reverse card to play.
     * @param direction     The direction of play.
     */
    public void handleReverseCard(Card selectedCard, boolean direction) {
        if (selectedCard.getColour() == currentCard.getColour() || currentCard.getCardType() == Card.CardType.REVERSE) {
            playCard(selectedCard);
            this.direction = !direction;

            view.update();
        } else {
            view.updateMessages("Invalid play. The card must match the color of the top card.");

        }
    }

    /**
     * Handles playing a Wild Draw Two Cards. If the card matches the color of the top card,
     * it forces the next player to draw two cards and skips their turn.
     *
     * @param colour        The input for the colour chosen.
     * @param selectedCard  The Wild Draw Two Cards to play.
     * @param direction     The direction of play.
     */
    public void handleWildDrawTwoCards(Card.Colour colour, Card selectedCard, boolean direction) {
        playCard(selectedCard);
        currentCard = new Card(colour, Card.CardType.WILD_DRAW_TWO);

        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        UnoPlayer nextPlayer = players.get(currentPlayerIndex);
        nextPlayer.drawCard(deck.draw());
        nextPlayer.drawCard(deck.draw());
        view.updateMessages(colour + " has been chosen. Player " + nextPlayer.getPlayerName() + " must draw 2 cards.");

        view.update();
    }

    /**
     * Handles a valid card play that matches the color or value of the top card.
     *
     * @param selectedCard  The card to play.
     */
    public void handleValidPlay(Card selectedCard) {
        playCard(selectedCard);
        view.updateDrawCardMessagePanel("Player " + currentPlayer.getPlayerName() + " plays: ", selectedCard);

        view.update();
    }

    public Card getLastSelected(){
        return lastSelectedCard;
    }
    public void playCard(Card selectedCard) {
        lastSelectedCard = selectedCard;
        currentPlayer.playCard(selectedCard);
        lastTopCard = currentCard;
        currentCard = selectedCard;
        view.setUndoMenuItem(true);
        view.setRedoMenuItem(false);

        view.nextPlayerButton(true);
        view.drawCardButton(false);
        view.update();
        view.cardButtons(false);
    }

    public void handleUndo(){
        view.updateMessages("Player " + currentPlayer.getPlayerName() + " undoes turn.");
        lastSelectedCard = currentCard;
        currentCard = lastTopCard;
        currentPlayer.undoPlay();
        view.setUndoMenuItem(false);
        view.setRedoMenuItem(true);
        view.update();
    }

    public void handleRedo(){
        view.updateMessages("Player " + currentPlayer.getPlayerName() + " redoes turn.");
        currentCard = lastSelectedCard;
        currentPlayer.redoPlay();
        view.setUndoMenuItem(true);
        view.setRedoMenuItem(false);
        view.nextPlayerButton(true);
        view.drawCardButton(false);

        view.update();
        view.cardButtons(false);
    }

    /**
     * Returns the current player.
     * @return currentPlayer the current player.
     */
    public UnoPlayer getCurrentPlayer() {
        currentPlayer = players.get(currentPlayerIndex);
        return currentPlayer;
    }

    /**
     * Returns the next player.
     * @return currentPlayer the next player.
     */
    public UnoPlayer getNextCurrentPlayer() {
        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        view.update();

        if (players.get(currentPlayerIndex) instanceof UnoPlayerAI){
            JOptionPane.showMessageDialog(null, "Bot is playing");
            view.nextPlayerButton(false);
            view.drawCardButton(false);
            view.cardButtons(false);

            //((UnoPlayerAI) currentPlayer).playRandomValidCard();
            ((UnoPlayerAI) players.get(currentPlayerIndex)).handleBotDecision();
            JOptionPane.showMessageDialog(null, "Bot finished playing");
        }

        view.nextPlayerButton(true);
        view.drawCardButton(false);
        view.update();
        return currentPlayer;
    }

    /**
     * Returns the current Card.
     * @return currentCard the current card in play.
     */
    public Card getTopCard() {
        return currentCard;
    }

    /**
     * Returns the direction of play.
     * @return True if forwards, False if backwards.
     */
    public boolean getDirection() {
        return direction;
    }

    public boolean getSide() {
        return side;
    }
    public void saveGameStateToJson(String fileName) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            JsonObject jsonObject = serializeGameState();
            writer.println(jsonObject);
        }
    }
    public void restoreGameStateFromJson(String fileName) throws IOException {
        try {
            FileReader fileReader = new FileReader(fileName);
            JsonReader jsonReader = Json.createReader(fileReader);

            JsonObject jsonObject = jsonReader.readObject();
            deserializeGameState(jsonObject);

            jsonReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private JsonObject serializeGameState() {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();

        jsonBuilder.add("direction", direction);
        jsonBuilder.add("side", side);
        jsonBuilder.add("currentPlayerIndex", currentPlayerIndex);
        jsonBuilder.add("roundNumber", roundNumber);

        JsonObject currentCardObject = serializeCard(currentCard);
        jsonBuilder.add("currentCard", currentCardObject);

        JsonArrayBuilder playersArrayBuilder = Json.createArrayBuilder();
        for (UnoPlayer player : players) {
            JsonObject playerObject = serializePlayer(player);
            playersArrayBuilder.add(playerObject);
        }
        jsonBuilder.add("players", playersArrayBuilder);

        return jsonBuilder.build();
    }

    private void deserializeGameState(JsonObject jsonObject) {
        direction = jsonObject.getBoolean("direction");
        side = jsonObject.getBoolean("side");
        currentPlayerIndex = jsonObject.getInt("currentPlayerIndex");
        roundNumber = jsonObject.getInt("roundNumber");

        // Deserialize the current card
        JsonObject currentCardObject = jsonObject.getJsonObject("currentCard");
        currentCard = deserializeCard(currentCardObject);

        // Deserialize players
        JsonArray playersArray = jsonObject.getJsonArray("players");
        players = new ArrayList<>();
        for (JsonValue playerValue : playersArray) {
            JsonObject playerObject = (JsonObject) playerValue;
            UnoPlayer player = deserializePlayer(playerObject);
            players.add(player);
        }
    }

    private JsonObject serializeCard(Card card) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("colour", card.getColour().name());
        jsonBuilder.add("darkColour", card.getDarkColour().name());
        jsonBuilder.add("number", card.getNumber().name());
        jsonBuilder.add("cardType", card.getCardType().name());
        jsonBuilder.add("cardDarkType", card.getCardDarkType().name());
        return jsonBuilder.build();
    }

    /**
     * Deserializes a Card object from a JSON object.
     *
     * @param jsonObject The JSON object representing the Card.
     * @return The deserialized Card object.
     */
    private Card deserializeCard(JsonObject jsonObject) {
        Card.Colour colour = Card.Colour.valueOf(jsonObject.getString("colour"));
        Card.DarkColour darkColour = Card.DarkColour.valueOf(jsonObject.getString("darkColour"));
        Card.Number number = Card.Number.valueOf(jsonObject.getString("number"));
        Card.CardType cardType = Card.CardType.valueOf(jsonObject.getString("cardType"));
        Card.DarkCardType cardDarkType = Card.DarkCardType.valueOf(jsonObject.getString("cardDarkType"));
        return new Card(colour, darkColour, number, cardType, cardDarkType);
    }

    /**
     * Serializes an UnoPlayer object to a JSON object.
     *
     * @param player The UnoPlayer object to serialize.
     * @return The JSON object representing the UnoPlayer.
     */
    private JsonObject serializePlayer(UnoPlayer player) {
        JsonObjectBuilder jsonBuilder = Json.createObjectBuilder();
        jsonBuilder.add("playerName", player.getPlayerName());

        JsonArrayBuilder handArrayBuilder = Json.createArrayBuilder();
        for (Card card : player.getHand().getCards()) {
            JsonObject cardObject = serializeCard(card);
            handArrayBuilder.add(cardObject);
        }
        jsonBuilder.add("hand", handArrayBuilder);

        return jsonBuilder.build();
    }

    /**
     * Deserializes an UnoPlayer object from a JSON object.
     *
     * @param jsonObject The JSON object representing the UnoPlayer.
     * @return The deserialized UnoPlayer object.
     */
    private UnoPlayer deserializePlayer(JsonObject jsonObject) {
        String playerName = jsonObject.getString("playerName");
        UnoPlayer player = new UnoPlayer(playerName, new Deck());


        JsonArray handArray = jsonObject.getJsonArray("hand");
        for (JsonValue cardValue : handArray) {
            JsonObject cardObject = (JsonObject) cardValue;
            Card card = deserializeCard(cardObject);
            player.getHand().addCard(card);
        }

        return player;}
}