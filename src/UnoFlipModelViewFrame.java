import javax.swing.*;
import java.awt.*;

public class UnoFlipModelViewFrame extends JFrame {
    private JButton drawButton;
    private JButton nextPlayerButton;
    private JPanel playerHandPanel;
    private JLabel topCardLabel;
    private JLabel currentPlayerLabel;
    private UnoFlipModel gameModel;
    private UnoPlayer currentPlayer;
    private JPanel messagesPanel;
    private JTextArea messagesTextArea;
    private JLabel imageLabel;

    private Deck deck;

    /**
     * Constructs a viewer for UnoFlip.
     * @param game the UnoFlip game being played.
     */
    public UnoFlipModelViewFrame(UnoFlipModel game) {
        gameModel = game;
        setTitle("Uno Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        JPanel upperHalfPanel = new JPanel(new BorderLayout());
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerLabel = new JLabel();
        currentPlayerPanel.add(currentPlayerLabel);
        upperHalfPanel.add(currentPlayerPanel, BorderLayout.WEST);

        JPanel topCardPanel = new JPanel();
        topCardLabel = new JLabel();
        topCardPanel.add(topCardLabel);
        upperHalfPanel.add(topCardPanel, BorderLayout.CENTER);

        splitPane.setTopComponent(upperHalfPanel);

        JPanel lowerHalfPanel = new JPanel(new BorderLayout());
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(playerHandPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        lowerHalfPanel.add(scrollPane, BorderLayout.CENTER);
        messagesPanel = new JPanel(new BorderLayout());
        messagesTextArea = new JTextArea(10, 30);
        JScrollPane messagesScrollPane = new JScrollPane(messagesTextArea);
        messagesPanel.add(messagesScrollPane, BorderLayout.CENTER);
        lowerHalfPanel.add(messagesPanel, BorderLayout.WEST);
        imageLabel = null;

        JPanel actionPanel = new JPanel(new FlowLayout());
        drawButton = new JButton("Draw Card");
        drawButton.addActionListener(new UnoFlipController(gameModel, this));
        actionPanel.add(drawButton);
        nextPlayerButton = new JButton("Next Player");
        nextPlayerButton.setEnabled(false);
        nextPlayerButton.addActionListener(new UnoFlipController(gameModel, this));
        actionPanel.add(nextPlayerButton);

        lowerHalfPanel.add(actionPanel, BorderLayout.SOUTH);
        splitPane.setBottomComponent(lowerHalfPanel);
        add(splitPane);
        gameModel.addView(this);
        update();
    }
    public void nextPlayerButton(Boolean bool){
        this.nextPlayerButton.setEnabled(bool);
    }

    public void drawCardButton(Boolean bool){
        this.drawButton.setEnabled(bool);
    }

    public void cardButtons(Boolean bool){
        Component[] components = playerHandPanel.getComponents();
        for (Component component: components) {
            if (component instanceof JButton) {
                JButton button = (JButton) component;
                button.setEnabled(bool);
            }
        }
    }

    /**
     * Update swing components.
     */
    public void update(){
        updatePlayerHandPanel();
        updateTopCardDisplay();
        updateCurrentPlayerDisplay();

    }
    /**
     * Update player's hand panel.
     */
    public void updatePlayerHandPanel() {
        playerHandPanel.removeAll(); // Clear the existing cards (buttons)

        // Get the current player's hand
        Hand currentHand = gameModel.getCurrentPlayer().getHand();

        for (Card card : currentHand.getCards()) {
            System.out.println(card.toString());
            JButton cardButton = new JButton(card.toString());
            ImageIcon cardImage = loadImagePath(card);
            cardImage.setImage(cardImage.getImage().getScaledInstance(110, 200, Image.SCALE_SMOOTH));
            cardButton.setIcon(cardImage);

            cardButton.setLayout(new BoxLayout(cardButton, BoxLayout.Y_AXIS));

            JLabel cardLabel = new JLabel(card.toString(), SwingConstants.CENTER);
            cardButton.add(cardLabel);
            cardButton.setPreferredSize(new Dimension(150, 200));
            cardButton.addActionListener(new UnoFlipController(gameModel, this));

            // Add the button to the player hand panel
            playerHandPanel.add(cardButton);
        }

        // Refresh the panel to show the updated hand
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    /**
     * Update the current card displayed.
     */
    private void updateTopCardDisplay() {
        Card topCard = gameModel.getTopCard();
        ImageIcon topCardImage = loadImagePath(topCard);
        topCardImage.setImage(topCardImage.getImage().getScaledInstance(200, 350, Image.SCALE_SMOOTH));

        if (topCardImage.getImageLoadStatus() == MediaTracker.COMPLETE) {
            topCardLabel.setIcon(topCardImage);
        } else {
            topCardLabel.setIcon(null);
            topCardLabel.setText("Image not found");
        }
        String cardText = topCard.toString();
        topCardLabel.setText(cardText);

    }

    /**
     * Update status message panel.
     */
    public void updateMessages(String message) {
        messagesTextArea.setText("");
        messagesTextArea.append(message + "\n");
        if (imageLabel != null) {
            messagesPanel.remove(imageLabel);
            messagesPanel.revalidate();
            messagesPanel.repaint();
        }
    }

    /**
     * Update status message panel with drawn card.
     * @param message the message displayed in the panel of the card drawn.
     * @param drawnCard the card drawn.
     */
    public void updateDrawCardMessagePanel(String message, Card drawnCard) {
        messagesTextArea.setText("");
        messagesTextArea.append(message + "\n");

        if (drawnCard != null) {
            // Assuming you have a JLabel to display the image
            ImageIcon cardImage = loadImagePath(drawnCard);
            imageLabel = new JLabel(cardImage);

            // Add the image to the messages panel
            messagesPanel.add(imageLabel, BorderLayout.NORTH);
            messagesPanel.revalidate();
            messagesPanel.repaint();
        }
    }

    /**
     * Update the label displaying the current player.
     */
    private void updateCurrentPlayerDisplay(){

        currentPlayer = gameModel.getCurrentPlayer();
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getPlayerName());
    }

    /**
     *
     * @param card the card for which an image is to be loaded.
     * @return the corresponding image of the card.
     */
    protected ImageIcon loadImagePath(Card card) {
        String imagePath;

        if (card.getCardType() == Card.CardType.WILD || card.getCardType() == Card.CardType.WILD_DRAW_TWO) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getCardType().toString().toLowerCase() + ".png";
        }
        else if (card.getCardType() == Card.CardType.REVERSE) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getCardType().toString().toLowerCase() + ".png";
        }
        else if (card.getCardType() == Card.CardType.SKIP) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getCardType().toString().toLowerCase() + ".png";
        }
        else if (card.getCardType() == Card.CardType.DRAW_ONE) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getCardType().toString().toLowerCase() + ".png";
        }
        else if (card.getCardType() == Card.CardType.NUMBER) {
            imagePath = "unoCards/" + card.getColour().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getNumber().toString().toLowerCase() + ".png";
            System.out.println("Image Path: " + imagePath);
        }
        else{imagePath = "unoCards/Unknown/blank.png";}

        ImageIcon CardImage = new ImageIcon(imagePath);

        return CardImage;
    }

    /**
     * Main method for UnoFlip
     */
    public static void main(String[] args) {

        String numPlayersStr = JOptionPane.showInputDialog(null,
                "How many players? (2-4)",
                "Number of Players",
                JOptionPane.QUESTION_MESSAGE);
        int numPlayers = 0;
        try {
            numPlayers = Integer.parseInt(numPlayersStr);
            if (numPlayers < 2 || numPlayers > 4) {
                JOptionPane.showMessageDialog(null,
                        "Please choose a number between 2 and 4.",
                        "Invalid Number",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input. Please enter a number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Start the game with new deck
        Deck deck = new Deck();
        deck.giveDeck();
        deck.shuffle();

        UnoFlipModel unoGame = new UnoFlipModel(numPlayers, deck);
        UnoFlipModelViewFrame view = new UnoFlipModelViewFrame(unoGame);
        view.setVisible(true);
    }
}