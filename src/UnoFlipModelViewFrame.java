import javax.swing.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.awt.*;

public class UnoFlipModelViewFrame extends JFrame implements UnoFlipModelView {
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

    public UnoFlipModelViewFrame(UnoFlipModel game) {
        gameModel = game;
        setTitle("Uno Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create a split pane for the upper and lower halves
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5); // Equal resizing for upper and lower halves

        // Upper half panel
        JPanel upperHalfPanel = new JPanel(new BorderLayout());

        // Current player label (upper half, left)
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerLabel = new JLabel();
        currentPlayerPanel.add(currentPlayerLabel);
        upperHalfPanel.add(currentPlayerPanel, BorderLayout.WEST);

        // Top card display (upper half, center)
        JPanel topCardPanel = new JPanel();
        topCardLabel = new JLabel();
        topCardPanel.add(topCardLabel);
        upperHalfPanel.add(topCardPanel, BorderLayout.CENTER);

        // Add the upper half panel to the split pane
        splitPane.setTopComponent(upperHalfPanel);

        JPanel lowerHalfPanel = new JPanel(new BorderLayout());

        // Player hand panel
        playerHandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JScrollPane scrollPane = new JScrollPane(playerHandPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        lowerHalfPanel.add(scrollPane, BorderLayout.CENTER);

        // Messages panel (lower half, left)
        messagesPanel = new JPanel(new BorderLayout());
        messagesTextArea = new JTextArea(10, 20); // You can customize the size
        JScrollPane messagesScrollPane = new JScrollPane(messagesTextArea);
        messagesPanel.add(messagesScrollPane, BorderLayout.CENTER);
        lowerHalfPanel.add(messagesPanel, BorderLayout.WEST);
        imageLabel = null;

        // Action buttons panel
        JPanel actionPanel = new JPanel(new FlowLayout());
        drawButton = new JButton("Draw Card");
        drawButton.addActionListener(new UnoFlipController(gameModel, this));
        actionPanel.add(drawButton);


        nextPlayerButton = new JButton("Next Player");
        nextPlayerButton.setEnabled(false);
        nextPlayerButton.addActionListener(new UnoFlipController(gameModel, this));
        actionPanel.add(nextPlayerButton);

        lowerHalfPanel.add(actionPanel, BorderLayout.SOUTH);

        // Add the lower half panel to the split pane
        splitPane.setBottomComponent(lowerHalfPanel);

        // Add the main panel to the frame
        add(splitPane);
        // Add this view to the uno game model
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

    // Update the view components as necessary
    public void update(){
        // update the panel with player cards
        updatePlayerHandPanel();
        // update the panel with top card
        updateTopCardDisplay();
        // Update the current player label
        updateCurrentPlayerDisplay();

    }


    public void updatePlayerHandPanel() {
        playerHandPanel.removeAll(); // Clear the existing cards (buttons)

        // Get the current player's hand
        Hand currentHand = gameModel.getCurrentPlayer().getHand();

        for (Card card : currentHand.getCards()) {
            System.out.println(card.toString());
            // For each card, create a button and set the text to the card's string representation
            JButton cardButton = new JButton(card.toString());

            //Get Image Path for each card's button
            ImageIcon cardImage = loadImagePath(card);
            cardImage.setImage(cardImage.getImage().getScaledInstance(80, 160, Image.SCALE_SMOOTH));
            cardButton.setIcon(cardImage);

            cardButton.setLayout(new BoxLayout(cardButton, BoxLayout.Y_AXIS));

            JLabel cardLabel = new JLabel(card.toString(), SwingConstants.CENTER);
            cardButton.add(cardLabel);

            cardButton.setPreferredSize(new Dimension(120, 150));
            cardButton.addActionListener(new UnoFlipController(gameModel, this));

            // Add the button to the player hand panel
            playerHandPanel.add(cardButton);
        }

        // Refresh the panel to show the updated hand
        playerHandPanel.revalidate();
        playerHandPanel.repaint();
    }

    private void updateTopCardDisplay() {
        Card topCard = gameModel.getTopCard();
        ImageIcon topCardImage = loadImagePath(topCard);

        // Check if the image was successfully loaded
        if (topCardImage.getImageLoadStatus() == MediaTracker.COMPLETE) {
            topCardLabel.setIcon(topCardImage);
        } else {
            // Handle image loading failure
            topCardLabel.setIcon(null);
            topCardLabel.setText("Image not found");
        }
        String cardText = topCard.toString();
        topCardLabel.setText(cardText);

    }

    public void updateMessages(String message) {
        messagesTextArea.setText("");
        messagesTextArea.append(message + "\n");
        if (imageLabel != null) {
            messagesPanel.remove(imageLabel);
            messagesPanel.revalidate();
            messagesPanel.repaint();
        }
    }

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

    private void updateCurrentPlayerDisplay(){

        currentPlayer = gameModel.getCurrentPlayer();
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getPlayerName());
    }
    protected ImageIcon loadImagePath(Card card) {
        String imagePath;

        if (card.getCardType() == Card.CardType.WILD || card.getCardType() == Card.CardType.WILD_DRAW_TWO) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getCardType().toString().toLowerCase() + ".png";
            //System.out.println("Image Path: " + imagePath);
        }
        else if (card.getCardType() == Card.CardType.REVERSE) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getCardType().toString().toLowerCase() + ".png";
            //System.out.println("Image Path: " + imagePath);
        }
        else if (card.getCardType() == Card.CardType.SKIP) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getCardType().toString().toLowerCase() + ".png";
            //System.out.println("Image Path: " + imagePath);
        }
        else if (card.getCardType() == Card.CardType.DRAW_ONE) {
            imagePath = "unoCards/" + card.getCardType().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getCardType().toString().toLowerCase() + ".png";
            //System.out.println("Image Path: " + imagePath);
        }
        else if (card.getCardType() == Card.CardType.NUMBER) {
            imagePath = "unoCards/" + card.getColour().toString().toLowerCase() + "/" + card.getColour().toString().toLowerCase() + card.getNumber().toString().toLowerCase() + ".png";
            System.out.println("Image Path: " + imagePath);
        }
        else{imagePath = "unoCards/Unknown/blank.png";}

        ImageIcon CardImage = new ImageIcon(imagePath);

        return CardImage;
    }

    // Main method to start the game GUI
    public static void main(String[] args) {
        // Show input dialog to get the number of players
        String numPlayersStr = JOptionPane.showInputDialog(null,
                "How many players? (2-4)",
                "Number of Players",
                JOptionPane.QUESTION_MESSAGE);
        int numPlayers = 0;

        // Validate and parse the input
        try {
            numPlayers = Integer.parseInt(numPlayersStr);
            if (numPlayers < 2 || numPlayers > 4) {
                JOptionPane.showMessageDialog(null,
                        "Please choose a number between 2 and 4.",
                        "Invalid Number",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0); // Exit or repeat the process
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Invalid input. Please enter a number.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Exit or repeat the process
        }

        // Start the game with new deck and the specified number of players
        Deck deck = new Deck();
        deck.giveDeck();
        deck.shuffle();
        //System.out.println(deck.draw().toString());

        UnoFlipModel unoGame = new UnoFlipModel(numPlayers, deck);
        // Ideally, pass unoGame to the view
        UnoFlipModelViewFrame view = new UnoFlipModelViewFrame(unoGame);
        view.setVisible(true);


        // Start the game logic if needed
        unoGame.play();


    }
}