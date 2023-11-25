import javax.swing.*;
import java.util.*;
public class UnoFlipModel {
    boolean direction = true;

    public enum Side {LIGHT, DARK}

    private List<UnoPlayer> players;
    private Card currentCard;
    private Deck deck;
    private boolean side; //true for light side, false for dark side
    private int currentPlayerIndex;
    private int roundNumber;
    UnoFlipModelViewFrame view;
    private UnoPlayer currentPlayer;

    /**
     * Constructs an UnoFlip game model.
     * @param numPlayers the number of players in game.
     * @param deck the game's card deck.
     */
    public UnoFlipModel(int numPlayers, Deck deck) {
        players = new ArrayList<>();
        initializePlayers(numPlayers, deck);
        direction = true;
        side = true;
        currentPlayerIndex = 0;
        this.deck = deck;
        currentCard = deck.draw();
        roundNumber = 1;
    }

    public void addView(UnoFlipModelViewFrame view) {
        this.view = view;
    }

    /**
     * Initializes the players of the game and allows user to enter their names.
     * @param numPlayers The number of players in the game.
     */
    private void initializePlayers(int numPlayers, Deck deck) {
        Scanner scanner = new Scanner(System.in);

        for (int i = 1; i <= numPlayers; i++) {
            String playerName = JOptionPane.showInputDialog(null,
                    "Enter Player 1's name",
                    "Name:",
                    JOptionPane.QUESTION_MESSAGE);
            UnoPlayer player = new UnoPlayer(playerName, deck);
            players.add(player);
        }
    }

    /**
     * Checks if the card can be played.
     * @param card The card to be checked.
     * @return True if the card can be played; otherwise, false.
     */
    public boolean isValidUnoPlay(Card card) {
        if (side) {
            if (card.getColour() == Card.Colour.NONE){
                return card.getColour() == currentCard.getColour() || card.getCardType() == currentCard.getCardType();
            }
            else {
                return card.getColour() == currentCard.getColour() || card.getNumber() == currentCard.getNumber();
            }
        }
        else {
            if (card.getDarkColour() == Card.DarkColor.NONE){
                return card.getDarkColour() == currentCard.getDarkColour() || card.getCardDarkType() == currentCard.getCardDarkType();
            }
            else {
                return card.getDarkColour() == currentCard.getDarkColour() || card.getNumber() == currentCard.getNumber();
            }
        }
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
        view.updateDrawCardMessagePanel("Player " + currentPlayer.getPlayerName() + " drew a card: ", drawnCard);
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
        System.out.println("Player " + currentPlayer.getPlayerName() + " plays: " + selectedCard.toString(side));
        currentPlayer.playCard(selectedCard);
        currentCard = selectedCard;
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
        UnoPlayer currentPlayer = players.get(currentPlayerIndex);
        return currentPlayer;
    }

    /**
     * Returns the next player.
     * @return currentPlayer the next player.
     */
    public UnoPlayer getNextCurrentPlayer() {
        currentPlayerIndex = (currentPlayerIndex + (direction ? 1 : -1) + players.size()) % players.size();
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
}