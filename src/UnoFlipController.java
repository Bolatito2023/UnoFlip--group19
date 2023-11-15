import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;


public class UnoFlipController  implements ActionListener {
    UnoFlipModel gameModel;
    UnoFlipModelViewFrame gameView;
    UnoPlayer currentPlayer;



    public UnoFlipController(UnoFlipModel game, UnoFlipModelViewFrame view) {
        super();
        this.gameModel = game;
        this.gameView = view;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentPlayer = gameModel.getCurrentPlayer();
        String clickedButton = e.getActionCommand();
        ArrayList<Card> handCards = currentPlayer.getHand().getCards();

        if (clickedButton == "Draw Card") {
            gameModel.handleDrawCard(currentPlayer);
        }
        if (clickedButton == "Next Player") {
            currentPlayer = gameModel.getNextCurrentPlayer();
            gameView.nextPlayerButton(false);
            gameView.drawCardButton(true);
            gameView.updateMessages("Choose a card or draw a card");

        }
        for (Card c : handCards) {
            if (clickedButton.equals(c.toString())) {
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
                if (gameModel.isValidUnoPlay(c)) {
                    gameModel.handleValidPlay(currentPlayer, c);
                    break;
                }
                else {
                    gameView.updateMessages("Invalid play");
                    gameView.drawCardButton(true);
                }
            }
        }
    }

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


}
