import java.util.*;
public class UnoFlip {
    public enum Direction {FORWARD, BACKWARD}

    public enum Side {LIGHT, DARK}

    private List<UnoPlayer> players;
    private Deck deck;
    private Card currentCard;

    private Direction direction;
    private Side side;
    private int currentPlayerIndex;
    private HashMap<String, Integer> scores;
    private Card.Colour chosenColor = null;
    private int roundNumber;
    private Scanner scan;

    public UnoFlip(List<UnoPlayer> players, Deck deck) {
        this.players = players;
        direction = Direction.FORWARD;
        side = Side.LIGHT;
        this.deck = deck;
        this.currentPlayerIndex = 0;
        this.roundNumber=1;
        scan = new Scanner(System.in);
        //Initialize score of each player
        scores = new HashMap<>();
        for (UnoPlayer player : players) {
            scores.put(player.getPlayerName(), 0);
        }
    }

    public void reverseDirection() {
        direction = (direction == Direction.FORWARD) ? Direction.BACKWARD : Direction.FORWARD;
    }

    public Direction getDirection(){
        return direction;
    }

    //calculate the total score of each player.
    private int calculateScore() {
        int totalScore = 0;
        for (UnoPlayer player : players) {
            if (player != players.get(currentPlayerIndex)) { // Exclude the current player
                for (Card card : player.getHand().getCards()) {
                    totalScore += card.Scoring();
                }
            }
        }
        return totalScore;
    }
    private void noPlayableDrawCards(UnoPlayer player) {
        System.out.println(player.getPlayerName() + "'s Hand: " + player.getHand());
        System.out.print(player.getPlayerName() + ", you don't have any playable cards. Press Enter key to draw a card: ");
        scan.nextLine(); // Waits for the player to press Enter
        Card drawnCard = deck.draw();
        player.drawCard(drawnCard);
        System.out.println();
        System.out.println(player.getPlayerName() + " draws a card: " + drawnCard);
    }


    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the game of UnoFlip!");
        System.out.println("Plays like regular UNO, except there are two sides to the deck of cards");
        System.out.println();
    }


    public void play() {
        printWelcome();
        int skipsThisTurn=0;


        Scanner scanner = new Scanner(System.in);
        boolean unoGameWon = false;


        currentCard = deck.draw();
        do {
            currentCard = deck.draw();
        } while (currentCard.getCardType() != Card.CardType.NUMBER); //Game does not start deck with action card.

        System.out.println("Round " + roundNumber);

        for(UnoPlayer pla :players){
            System.out.println(pla.getPlayerName() +" current score is "+scores.get(pla.getPlayerName()));
        }

        while (!unoGameWon) {


            UnoPlayer currentPlayer = players.get(currentPlayerIndex);
            Hand currentHand = currentPlayer.getHand();

            List<Card> playableCards = currentHand.getPlayableCards(currentCard, chosenColor);
            System.out.println();



            System.out.println("You are currently on the " + side + " side");
            System.out.println("Current Card on discard pile: " + currentCard);

            if (!playableCards.isEmpty()) {

                if (currentCard.getCardType() == Card.CardType.SKIP) {

                    if (direction == Direction.FORWARD) {
                        System.out.println(currentPlayer.getPlayerName() + " you can't play; you have been given a skip.");
                        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                        currentPlayer = players.get(currentPlayerIndex);
                        currentHand = currentPlayer.getHand();
                    } else {
                        System.out.println(currentPlayer.getPlayerName() + " you can't play; you have been given a skip.");
                        if (currentPlayerIndex == 0) {
                            currentPlayerIndex = players.size() - 1; // Wrap around to the last player
                        } else {
                            currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
                        }
                        currentPlayer = players.get(currentPlayerIndex);
                        currentHand = currentPlayer.getHand();
                    }

                }
                if (currentCard.getCardType() == Card.CardType.DRAW_ONE) {
                    if (direction == Direction.FORWARD) {
                        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                    } else {
                        currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
                    }
                    UnoPlayer nextPlayer = players.get(currentPlayerIndex);
                    System.out.print(currentPlayer.getPlayerName() + ", Oops! You have to draw a card you can't play. ");
                    Card drawnCard = deck.draw();
                    currentPlayer.drawCard(drawnCard);
                    System.out.println();
                    System.out.println(currentPlayer.getPlayerName() + " draws a card: " + drawnCard);
                    currentPlayer = nextPlayer;
                    currentHand = currentPlayer.getHand();

                }
                int inputIndex = -1;  // Initialize inputIndex to -1
                if (!playableCards.isEmpty()) {
                    playableCards = currentHand.getPlayableCards(currentCard, chosenColor);
                    System.out.println(currentPlayer.getPlayerName() + "'s Hand: " + currentHand);
                    System.out.print(currentPlayer.getPlayerName() + " Playable cards are: " + playableCards);
                    System.out.println();
                    System.out.println(currentPlayer.getPlayerName() + " Enter the index of the card to play (0-" + (playableCards.size() - 1) + "). To draw a card, enter -1.");
                    System.out.println();
                    boolean validInput = false;
                    while (!validInput) {
                        try {
                            inputIndex = scanner.nextInt();
                            scanner.nextLine(); // Consume the newline character
                            if (inputIndex >= -1 && inputIndex < playableCards.size()) {
                                validInput = true;
                            } else {
                                System.out.println("Invalid Input");
                                System.out.println(currentPlayer.getPlayerName() + " Enter the index of the card to play (0-" + (playableCards.size() - 1) + "). To draw a card, enter -1.");
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("Invalid input");
                            System.out.println(currentPlayer.getPlayerName() + " Enter the index of the card to play (0-" + (playableCards.size() - 1) + "). To draw a card, enter -1.");
                            scanner.next();
                        }
                    }

                }
                if (inputIndex == -1) {
                    // The player chose to draw a card
                    Card drawncard = deck.draw();
                    currentPlayer.drawCard(drawncard);
                    System.out.println();
                    System.out.println(currentPlayer.getPlayerName() + " draws a card: " + drawncard);

                }

                //int inputIndex = scanner.nextInt();
                if (inputIndex >= 0 && inputIndex < playableCards.size()) {
                    Card cardPlayed = playableCards.get(inputIndex);
                    if (cardPlayed.getCardType() == Card.CardType.WILD) {
                        System.out.println(currentPlayer.getPlayerName() + " plays " + cardPlayed);
                        currentPlayer.playCard(cardPlayed);
                        System.out.print(currentPlayer.getPlayerName() + ", choose the color to continue play:\n");
                        System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");

                        int chooseColour = 0;
                        boolean validInput = false;
                        while (!validInput) {
                            try {
                                chooseColour = scanner.nextInt();
                                if (chooseColour >= 1 && chooseColour <= 4) {
                                    validInput = true;
                                } else {
                                    System.out.println("Invalid color choice. Please choose a valid color.");
                                    System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("Invalid color choice. Please choose a valid color.");
                                System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");
                                scanner.next();
                            }
                        }
                        chosenColor = Card.Colour.values()[chooseColour - 1];
                        currentCard = cardPlayed;
                        currentCard.setColour(chosenColor);
                        System.out.println(currentPlayer.getPlayerName() + " has chosen the color: " + chosenColor);
                    }
                    //Bonus
                    if (cardPlayed.getCardType() == Card.CardType.WILD_DRAW_TWO) {
                        if (direction == Direction.FORWARD) {
                            System.out.println(currentPlayer.getPlayerName() + " plays " + cardPlayed);
                            currentPlayer.playCard(cardPlayed);
                            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
                            currentPlayer = players.get(currentPlayerIndex);
                            System.out.print(currentPlayer.getPlayerName() + ", Oops! You have to draw a card you can't play. ");
                            //Draw one card for the next player
                            UnoPlayer nextPlayer = players.get(currentPlayerIndex);
                            Card drawnCard = deck.draw();
                            Card drawnCard2 = deck.draw();
                            nextPlayer.drawCard(drawnCard);
                            nextPlayer.drawCard(drawnCard2);
                            System.out.println();
                            System.out.println(nextPlayer.getPlayerName() + " draws cards: " + drawnCard + " and " + drawnCard2);
                            currentPlayer.playCard(cardPlayed);
                            System.out.print("Player who played WILD CARD 2 choose the color to continue play:\n");
                            System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");

                            int chooseColour = 0;
                            boolean validInput = false;
                            while (!validInput) {
                                try {
                                    chooseColour = scanner.nextInt();
                                    if (chooseColour >= 1 && chooseColour <= 4) {
                                        validInput = true;
                                    } else {
                                        System.out.println("Invalid color choice. Please choose a valid color.");
                                        System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid color choice. Please choose a valid color.");
                                    System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");
                                    scanner.next();
                                }
                            }
                            chosenColor = Card.Colour.values()[chooseColour - 1];
                            currentCard = cardPlayed;
                            currentCard.setColour(chosenColor);
                            System.out.println(currentPlayer.getPlayerName() + " has chosen the color: " + chosenColor);

                        } else {
                            System.out.println(currentPlayer.getPlayerName() + " plays " + cardPlayed);
                            currentPlayer.playCard(cardPlayed);
                            if (currentPlayerIndex == 0) {
                                currentPlayerIndex = players.size() - 1; // Wrap around to the last player
                            } else {
                                currentPlayerIndex = (currentPlayerIndex - 1) % players.size();
                            }
                            currentPlayer = players.get(currentPlayerIndex);
                            System.out.print(currentPlayer.getPlayerName() + ", Oops! You have to draw a card you can't play. ");
                            //Draw one card for the next player
                            UnoPlayer nextPlayer = players.get(currentPlayerIndex);
                            Card drawnCard = deck.draw();
                            nextPlayer.drawCard(drawnCard);
                            System.out.println();
                            System.out.println(nextPlayer.getPlayerName() + " draws a card: " + drawnCard);
                            currentPlayer.playCard(cardPlayed);
                            System.out.print("Player who played WILD CARD 2 choose the color to continue play:\n");
                            System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");

                            int chooseColour = 0;
                            boolean validInput = false;
                            while (!validInput) {
                                try {
                                    chooseColour = scanner.nextInt();
                                    if (chooseColour >= 1 && chooseColour <= 4) {
                                        validInput = true;
                                    } else {
                                        System.out.println("Invalid color choice. Please choose a valid color.");
                                        System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("Invalid color choice. Please choose a valid color.");
                                    System.out.println("Enter 1 for Blue, Enter 2 for Green, Enter 3 for Red, Enter 4 for Yellow");
                                    scanner.next();
                                }
                            }
                            chosenColor = Card.Colour.values()[chooseColour - 1];
                            currentCard = cardPlayed;
                            currentCard.setColour(chosenColor);
                            System.out.println(currentPlayer.getPlayerName() + " has chosen the color: " + chosenColor);
                        }

                    } else {
                        System.out.println(currentPlayer.getPlayerName() + " plays " + cardPlayed);
                        currentCard = cardPlayed;
                        currentPlayer.playCard(cardPlayed);
                    }

                } else {
                    if(inputIndex!=-1 ) {
                        System.out.println("Invalid index. Please enter a valid card index.");
                        int index = scanner.nextInt();
                        Card cardPlayed = playableCards.get(index);
                        System.out.println(currentPlayer.getPlayerName() + " plays " + cardPlayed);
                        currentPlayer.playCard(cardPlayed);
                        currentCard = cardPlayed;
                    }
                }
            } else {

                if (direction == Direction.FORWARD) {
                    noPlayableDrawCards(currentPlayer);
                    currentPlayer = players.get(currentPlayerIndex);
                } else {
                    noPlayableDrawCards(currentPlayer);
                    currentPlayer = players.get((currentPlayerIndex - 1 + players.size()) % players.size());
                }

            }
            if (deck.isEmpty()) {
                deck.reAdd();
            }
            if (currentPlayer.hasUno()) {
                System.out.println(currentPlayer.getPlayerName() + " declares UNO!");
            }
            if (currentPlayer.emptyHand()) {
                int opponentScore = calculateScore();
                scores.put(currentPlayer.getPlayerName(), scores.get(currentPlayer.getPlayerName()) + opponentScore);
                for (UnoPlayer player : players) {
                    if ((scores.get(player.getPlayerName())) >= 500) {
                        System.out.println(currentPlayer.getPlayerName() + " wins the game!");
                        unoGameWon = true;
                    } else {
                        for (UnoPlayer pl : players) {
                            pl.resetHand();
                        }
                        deck.giveDeck();
                        deck.shuffle();
                        roundNumber++;
                        currentPlayerIndex=0;
                        for (UnoPlayer pl : players) { //Start a new round with 7 new cards
                            for (int i = 0; i < 7; i++) {
                                Card drawnCard = deck.draw();
                                pl.drawCard(drawnCard);
                            }
                        }
                        play();
                    }

                }
            }
            //To add to the deck discard pile
            if (deck.isEmpty()) {
                deck.reAdd();
            }

            if (currentCard.getCardType() == Card.CardType.REVERSE) {
                reverseDirection();
                System.out.print("The direction has been reversed. The game is now " + direction + " ");
                System.out.println();
            }

            if (direction == Direction.FORWARD) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

            } else {
                currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
            }

        }

    }

}
