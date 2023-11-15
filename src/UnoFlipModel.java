import java.util.*;
public class UnoFlipModel {
    boolean direction = true;

    public enum Side {LIGHT, DARK}

    private List<UnoPlayer> players;
    private Card currentCard;
    private Deck deck;
    private Side side;
    private int currentPlayerIndex;
    private int roundNumber;
    UnoFlipModelViewFrame view;
    private UnoPlayer currentPlayer;

    public UnoFlipModel(int numPlayers, Deck deck) {
        players = new ArrayList<>();
        initializePlayers(numPlayers, deck);
        direction = true;
        side = Side.LIGHT;
        currentPlayerIndex = 0;
        this.deck = deck;
        currentCard = deck.draw();
        roundNumber = 1;
    }

    public void addView(UnoFlipModelViewFrame view) {
        this.view = view;
    }

    /**
     * Initializes the players by collecting their names from the user.
     *
     * @param numPlayers The number of players in the game.
     */
    private void initializePlayers(int numPlayers, Deck deck) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = "Player " + i;
            UnoPlayer player = new UnoPlayer(playerName, deck);
            players.add(player);
        }
    }

    /**
     * Displays the current player's hand.
     *
     * @param currentPlayer The current player.
     */
    private void displayCurrentPlayerHand(UnoPlayer currentPlayer) {
        System.out.print("Player " + currentPlayer.getPlayerName() + "'s cards: \n");
        List<Card> hand = currentPlayer.getHand().getCards();
        for (int j = 0; j < hand.size(); j++) {
            Card card = hand.get(j);
            System.out.print((j + 1) + "- " + card.toString());
            if (j < hand.size() - 1) {
                System.out.print(",\n");
            }
        }
        System.out.println();
    }

    /**
     * Checks if a card is a valid play in the Uno game.
     *
     * @param card The card to check.
     * @return True if the card can be played; otherwise, false.
     */
    public boolean isValidUnoPlay(Card card) {
        System.out.println("is valid reached");
        return card.getColour() == currentCard.getColour() || card.getNumber() == currentCard.getNumber();
    }

    /**
     * Calculates the score for the winning player based on opponents' cards.
     *
     * @param winner The player who won the round.
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
     * Starts the Uno game and manages the game flow.
     */
    public void play() {
        boolean gameRunning = true;
        boolean go_next = true;
        Scanner scanner = new Scanner(System.in);

        while (gameRunning) {
            UnoPlayer currentPlayer = players.get(currentPlayerIndex);
            displayGameStatus(currentPlayer);

            int cardIndex = getPlayerInput(scanner, currentPlayer);

            if (cardIndex == 0) {
                handleDrawCard(currentPlayer);
                go_next = true;
            } else {
            }

            handleUnoStatus(scanner, currentPlayer);

            if (currentPlayer.getHand().handSize() == 0) {
                handleWinOrPenalty(currentPlayer);
                gameRunning = currentPlayer.getHand().handSize() != 0;
            }

            if (go_next) {
                currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
            }
        }
    }

    /**
     * Displays the game status, including the top card and the current player's hand.
     *
     * @param currentPlayer The current player.
     */
    private void displayGameStatus(UnoPlayer currentPlayer) {
        System.out.println("Top Card: " + currentCard.toString());
        System.out.println("Player " + currentPlayer.getPlayerName() + "'s turn");
        displayCurrentPlayerHand(currentPlayer);
    }

    /**
     * Gets the player's input for selecting a card to play or drawing a card.
     *
     * @param scanner       The input scanner.
     * @param currentPlayer The current player.
     * @return The index of the selected card or 0 to draw a card.
     */
    private int getPlayerInput(Scanner scanner, UnoPlayer currentPlayer) {
        int cardIndex;
        while (true) {
            System.out.println("Enter card index to play (1 to " + currentPlayer.getHand().handSize() + ") or 0 to draw a card:");
            if (scanner.hasNextInt()) {
                cardIndex = scanner.nextInt();
                if (cardIndex >= 0 && cardIndex <= currentPlayer.getHand().handSize()) {
                    return cardIndex;
                } else {
                    System.out.println("Invalid Index for Hand, Please try again.");
                }
            } else {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        }
    }

    /**
     * Handles drawing a card from the deck.
     *
     * @param currentPlayer The current player who is drawing a card.
     */
    protected void handleDrawCard(UnoPlayer currentPlayer) {
        Card drawnCard = deck.draw();
        currentPlayer.drawCard(drawnCard);
        view.updateDrawCardMessagePanel("Player " + currentPlayer.getPlayerName() + " drew a card: ", drawnCard);
        view.nextPlayerButton(true);
        view.drawCardButton(false);
        view.update();
        view.cardButtons(false);
    }

    /**
     * Handles playing a card from the player's hand.
     *
     * @param scanner       The input scanner.
     * @param currentPlayer  The current player.
     * @param cardIndex     The index of the card to play.
     * @param direction     The direction of play.
     * @return True if the play was successful; otherwise, false.
     */
    /*private boolean handlePlayCard(Scanner scanner, Player currentPlayer, int cardIndex, boolean direction) {
        Card selectedCard = currentPlayer.getHand().getCards().get(cardIndex - 1);
        if (selectedCard.getValue() == Card.Value.WILD) {
            return handleWildCard(scanner, currentPlayer, selectedCard);
        } else if (selectedCard.getValue() == Card.Value.SKIP) {
            return handleSkipCard(currentPlayer, selectedCard, direction);
        } else if (selectedCard.getValue() == Card.Value.REVERSE) {
            return handleReverseCard(currentPlayer, selectedCard, direction);
        } else if (selectedCard.getValue() == Card.Value.WILD_DRAW_TWO_CARDS && selectedCard.getColor() == topCard.getColor()) {
            return handleWildDrawTwoCards(scanner, currentPlayer, selectedCard, direction);
        } else if (isValidUnoPlay(selectedCard)) {
            return handleValidPlay(currentPlayer, selectedCard);
        } else {
            System.out.println("Invalid play. Try again.");
            return false;
        }
    }*/

    /**
     * Handles Uno status and reminds the player to say Uno if applicable.
     *
     * @param scanner       The input scanner.
     * @param currentPlayer The current player.
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
     * @param currentPlayer The current player.
     * @param selectedCard  The Wild card to play.
     * @return True if the play was successful; otherwise, false.
     */
    public void handleWildCard(Card.Colour colour, UnoPlayer currentPlayer, Card selectedCard) {
        //String chosenColor = getChosenColor(scanner);
        //System.out.println("Player " + currentPlayer.getName() + " plays: " + selectedCard.stringCard() + " color chosen: " + chosenColor);
        currentPlayer.playCard(selectedCard);
        currentCard = new Card(colour, Card.CardType.WILD);
        System.out.println("handleWildCard is reached");
        view.nextPlayerButton(true);
        view.drawCardButton(false);
        view.cardButtons(false);
        view.updateMessages(colour + " has been chosen. Player " + currentPlayerIndex + " has to draw place a " + colour + "colour card");
        view.update();
    }

    /**
     * Handles playing a Skip card. If the card matches the color of the top card,
     * it skips the next player's turn.
     *
     * @param currentPlayer The current player.
     * @param selectedCard  The Skip card to play.
     * @param direction     The direction of play.
     * @return True if the play was successful; otherwise, false.
     */
    public void handleSkipCard(UnoPlayer currentPlayer, Card selectedCard, boolean direction) {
        if (selectedCard.getColour() == currentCard.getColour() || currentCard.getCardType() == Card.CardType.SKIP) {
            //System.out.println("Player " + currentPlayer.getName() + " plays: " + selectedCard.stringCard());
            currentPlayer.playCard(selectedCard);
            currentCard = selectedCard;
            currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
            view.nextPlayerButton(true);
            view.drawCardButton(false);
            view.update();
            view.cardButtons(false);


        } else {
            view.updateMessages("Invalid play. The card must match the color of the top card.");
        }
    }

    /**
     * Handles playing a Reverse card. If the card matches the color of the top card,
     * it reverses the direction of play.
     *
     * @param currentPlayer The current player.
     * @param selectedCard  The Reverse card to play.
     * @param direction     The direction of play.
     * @return True if the play was successful; otherwise, false.
     */
    public void handleReverseCard(UnoPlayer currentPlayer, Card selectedCard, boolean direction) {
        if (selectedCard.getColour() == currentCard.getColour() || currentCard.getCardType() == Card.CardType.REVERSE) {
            //System.out.println("Player " + currentPlayer.getName() + " plays: " + selectedCard.stringCard());
            currentPlayer.playCard(selectedCard);
            currentCard = selectedCard;
            this.direction = !direction;
            view.nextPlayerButton(true);
            view.drawCardButton(false);
            view.update();
            view.cardButtons(false);
        } else {
            view.updateMessages("Invalid play. The card must match the color of the top card.");

        }
    }

    /**
     * Handles playing a Wild Draw Two Cards. If the card matches the color of the top card,
     * it forces the next player to draw two cards and skips their turn.
     *
     * @param colour        The input for the colour chosen.
     * @param currentPlayer The current player.
     * @param selectedCard  The Wild Draw Two Cards to play.
     * @param direction     The direction of play.
     * @return True if the play was successful; otherwise, false.
     */
    public void handleWildDrawTwoCards(Card.Colour colour, UnoPlayer currentPlayer, Card selectedCard, boolean direction) {
        //String chosenColor = getChosenColor(scanner);
        //System.out.println("Player " + currentPlayer.getName() + " plays: " + selectedCard.stringCard() + " color chosen: " + chosenColor);
        currentPlayer.playCard(selectedCard);
        currentCard = new Card(colour, Card.CardType.WILD_DRAW_TWO);

        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        UnoPlayer nextPlayer = players.get(currentPlayerIndex);
        nextPlayer.drawCard(deck.draw());
        nextPlayer.drawCard(deck.draw());
        System.out.println("handleWildDrawTwoCards is reached");
        view.nextPlayerButton(true);
        view.drawCardButton(false);
        view.updateMessages(colour + " has been chosen. Player " + currentPlayerIndex + " has to draw 2 cards. due to wild draw two");
        view.update();
        view.cardButtons(false);
    }

    /**
     * Handles a valid card play that matches the color or value of the top card.
     *
     * @param currentPlayer The current player.
     * @param selectedCard  The card to play.
     * @return True if the play was successful; otherwise, false.
     */
    public void handleValidPlay(UnoPlayer currentPlayer, Card selectedCard) {
        System.out.println("Player " + currentPlayer.getPlayerName() + " plays: " + selectedCard.toString());
        currentPlayer.playCard(selectedCard);
        currentCard = selectedCard;
        view.nextPlayerButton(true);
        view.drawCardButton(false);
        view.update();
        view.cardButtons(false);

    }

    public UnoPlayer getCurrentPlayer() {
        UnoPlayer currentPlayer = players.get(currentPlayerIndex);
        return currentPlayer;
    }

    public UnoPlayer getNextCurrentPlayer() {
        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
        view.update();
        return currentPlayer;
    }

    public Card getTopCard() {
        return currentCard;
    }

    public boolean getDirection() {
        return direction;
    }
}