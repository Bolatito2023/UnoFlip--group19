import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class UnoFlipController  implements ActionListener {
    UnoFlipModel gameModel;
    UnoFlipModelViewFrame gameView;
    private UnoPlayer currentPlayer;
    private boolean side; //true for light side, false for dark side

    /**
     * Constructs a controller for UnoFlip.
     * @param game the UnoFlip game being played.
     * @param view the viewer for the UnoFlip game being played.
     */
    public UnoFlipController(UnoFlipModel game, UnoFlipModelViewFrame view) {
        super();
        this.gameModel = game;
        this.gameView = view;
    }

    /**
     * Handles player actions.
     * @param e the event to be processed.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        currentPlayer = gameModel.getCurrentPlayer();
        side = gameModel.getSide();
        String clickedButton = e.getActionCommand();
        ArrayList<Card> handCards = currentPlayer.getHand().getCards();

        if (clickedButton == "Draw Card") {
            gameModel.handleDrawCard(currentPlayer);
        }
        if (clickedButton == "Next Player") {
            currentPlayer = gameModel.getNextCurrentPlayer();
            gameView.nextPlayerButton(false);
            gameView.drawCardButton(true);
            gameView.updateMessages("Pick a card to play or draw a card");

        }
        for (Card c : handCards) {
            if (side) {
                if (clickedButton.equals(c.toString(true))) {
                    if (c.getCardType() == Card.CardType.WILD) {
                        gameModel.handleWildCard(chooseColour(), currentPlayer, c);
                        break;
                    }
                    if (c.getCardType() == Card.CardType.WILD_DRAW_TWO) {
                        gameModel.handleWildDrawTwoCards(chooseColour(), currentPlayer, c, gameModel.getDirection());
                        break;
                    }
                    if (c.getCardType() == Card.CardType.SKIP && c.getColour() == gameModel.getTopCard().getColour()) {
                        gameModel.handleSkipCard(currentPlayer, c, gameModel.getDirection());
                        break;
                    }
                    if (c.getCardType() == Card.CardType.REVERSE && c.getColour() == gameModel.getTopCard().getColour()) {
                        gameModel.handleReverseCard(currentPlayer, c, gameModel.getDirection());
                        break;
                    }
                    if (c.getCardType() == Card.CardType.FLIP && c.getColour() == gameModel.getTopCard().getColour()) {
                        gameModel.handleFlipCard(currentPlayer, c);
                        break;
                    }
                    if (gameModel.isValidUnoPlay(c)) {
                        gameModel.handleValidPlay(currentPlayer, c);
                        break;
                    }
                    else {
                        gameView.updateMessages("This card cannot be played!");
                        gameView.drawCardButton(true);
                    }
                }
            }
            else {
                if (clickedButton.equals(c.toString(false))) {
                    if (c.getCardDarkType() == Card.DarkCardType.SKIP_EVERYONE && c.getDarkColour() == gameModel.getTopCard().getDarkColour()) {
                        gameModel.handleSkipEveryoneCard(currentPlayer, c);
                        break;
                    }
                    if (c.getCardDarkType() == Card.DarkCardType.DRAW_FIVE) {
                        gameModel.handleDrawFive(currentPlayer, c, gameModel.getDirection());
                        break;
                    }
                    if (c.getCardDarkType() == Card.DarkCardType.FLIP && c.getDarkColour() == gameModel.getTopCard().getDarkColour()) {
                        gameModel.handleFlipCard(currentPlayer, c);
                        break;
                    }
                    if (c.getCardDarkType() == Card.DarkCardType.REVERSE && c.getDarkColour() == gameModel.getTopCard().getDarkColour()) {
                        gameModel.handleReverseCard(currentPlayer, c, gameModel.getDirection());
                        break;
                    }
                    if (c.getCardDarkType() == Card.DarkCardType.WILD) {
                        gameModel.handleWildCard(chooseDarkColour(), currentPlayer, c);
                        break;
                    }
                    if (c.getCardDarkType() == Card.DarkCardType.WILD_DRAW_COLOUR) {
                        gameModel.handleWildDrawColourCard(chooseDarkColour(), currentPlayer, c, gameModel.getDirection());
                        break;
                    }
                    if (gameModel.isValidUnoPlay(c)) {
                        gameModel.handleValidPlay(currentPlayer, c);
                        break;
                    } else {
                        gameView.updateMessages("Invalid play");
                        gameView.drawCardButton(true);
                    }
                }
            }
        }
    }

    /**
     * Returns the colour a player chooses after playing a wild card.
     * @return the colour the current player chooses.
     */
    private static Card.Colour chooseColour() {
        Card.Colour[] possibleColours = Card.Colour.values();
        String[] colourNames = new String[possibleColours.length];

        for (int i = 0; i < possibleColours.length; i++) {
            colourNames[i] = possibleColours[i].name();
        }

        String chosenColourName = null;

        while (true) {
            chosenColourName = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose a colour:",
                    "Colour Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    colourNames,
                    colourNames[0]);

            // Check if a valid colour was chosen
            if (chosenColourName != null) {
                try {
                    return Card.Colour.valueOf(chosenColourName);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, "Invalid selection. Please choose a colour.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "You must choose a colour.");
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
}
