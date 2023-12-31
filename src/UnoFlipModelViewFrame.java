import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class UnoFlipModelViewFrame extends JFrame {
    private JButton drawButton;
    private JButton nextPlayerButton;
    private JPanel playerHandPanel;
    private JLabel topCardLabel;
    private JLabel currentPlayerLabel;
    private UnoFlipModel gameModel;
    private UnoPlayer currentPlayer;
    private JPanel messagesPanel;
    private JLabel messagesTextLabel;
    private JLabel imageLabel;
    private Deck deck;
    private boolean side; //true for light side, false for dark side

    private static int numPlayers;
    private static int numAIPlayers;

    JMenuItem UndoMenuItem;
    JMenuItem RedoMenuItem;

    /**
     * Constructs a viewer for UnoFlip.
     * @param game the UnoFlip game being played.
     */
    public UnoFlipModelViewFrame(UnoFlipModel game) {
        gameModel = game;
        side = gameModel.getSide();
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

        createMenuBar();

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
        messagesTextLabel = new JLabel("Pick a card to play or draw a card");
        JScrollPane messagesScrollPane = new JScrollPane(messagesTextLabel);
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

    /**
     * Create menu bar for Milestone 4 functionalities
     */
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu gameMenu = new JMenu("Menu");

        UndoMenuItem = new JMenuItem("Undo");
        RedoMenuItem = new JMenuItem("Redo");
        JMenuItem ReplayMenuItem = new JMenuItem("Replay");
        JMenuItem SaveMenuItem = new JMenuItem("Save Game");
        JMenuItem LoadMenuItem = new JMenuItem("Load Game");

        gameMenu.add(UndoMenuItem);
        gameMenu.add(RedoMenuItem);
        gameMenu.add(ReplayMenuItem);
        gameMenu.add(SaveMenuItem);
        gameMenu.add(LoadMenuItem);

        menuBar.add(gameMenu);

        UndoMenuItem.setEnabled(false);
        RedoMenuItem.setEnabled(false);
        UndoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nextPlayerButton.setEnabled(false);
                drawButton.setEnabled(true);
                cardButtons(true);
                gameModel.handleUndo();
            }
        });
        RedoMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModel.handleRedo();
            }
        });
        ReplayMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });
        SaveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    saveGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        LoadMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadGame();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * Resets the game if the "Replay" menu button is pressed
     */
    private void resetGame() {
        int result = JOptionPane.showConfirmDialog((Component) null, "Do you want to restart the game?",
                "Replay Confirmation", JOptionPane.YES_NO_OPTION);
        if (result == 0) {
            gameModel.resetPlayerHands();
            gameModel.resetTopCard();
            update();
        }
    }

    private void saveGame() throws IOException {
        String fileName = JOptionPane.showInputDialog("Enter export JSON file name:");
        fileName = fileName + ".json";
        gameModel.saveGameStateToJson(fileName);
    }

    private void loadGame() throws IOException {
        String fileName = JOptionPane.showInputDialog("Enter import JSON file name:");
        fileName = fileName + ".json";
        gameModel.restoreGameStateFromJson(fileName);
    }

    public void setUndoMenuItem(Boolean bool){
        UndoMenuItem.setEnabled(bool);
    }
    public void setRedoMenuItem(Boolean bool){
        RedoMenuItem.setEnabled(bool);
    }
    public void nextPlayerButton(Boolean bool){
        nextPlayerButton.setEnabled(bool);
    }

    public void drawCardButton(Boolean bool){
        drawButton.setEnabled(bool);
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
        playerHandPanel.removeAll(); // Clear the existing cards

        side = gameModel.getSide();

        Hand currentHand = gameModel.getCurrentPlayer().getHand();

        for (Card card : currentHand.getCards()) {
            JButton cardButton = new JButton(card.toString());
            ImageIcon cardImage = loadImagePath(card);

            cardButton.setText(card.toString(side));
            cardImage.setImage(cardImage.getImage().getScaledInstance(110, 200, Image.SCALE_SMOOTH));
            cardButton.setIcon(cardImage);

            cardButton.setLayout(new BoxLayout(cardButton, BoxLayout.Y_AXIS));
            JLabel cardLabel = new JLabel(card.toString(side), SwingConstants.CENTER);
            cardButton.add(cardLabel);

            cardButton.setPreferredSize(new Dimension(150, 200));
            cardButton.addActionListener(new UnoFlipController(gameModel, this));

            playerHandPanel.add(cardButton);
        }

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
        String cardText = topCard.toString(side);
        topCardLabel.setText(cardText);

    }

    /**
     * Update status message panel.
     */
    public void updateMessages(String message) {
        messagesTextLabel.setText(message);
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
        messagesTextLabel.setText(message);

        if (drawnCard != null) {
            ImageIcon cardImage = loadImagePath(drawnCard);
            imageLabel = new JLabel(cardImage);
            messagesPanel.add(imageLabel, BorderLayout.SOUTH);
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
        if (card.getCardType() == Card.CardType.WILD) {
            imagePath = "unoCards/Light/wild.png";
        }
        else {
            if (side) {
                imagePath = "unoCards/Light/" + card.toString(side).toLowerCase() + ".png";
            } else {
                imagePath = "unoCards/Dark/" + card.toString(side).toLowerCase() + ".png";
            }
        }
        //System.out.println(imagePath);
        imagePath = imagePath.replaceAll(" ", "_");
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
        String numAIPlayersStr = JOptionPane.showInputDialog(null,
                "How many AI players? (0-10)",
                "Number of AI Players",
                JOptionPane.QUESTION_MESSAGE);
        try {
            numAIPlayers = Integer.parseInt(numAIPlayersStr);
            if (numAIPlayers < 0 || numAIPlayers > 10) {
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

        UnoFlipModel unoGame = new UnoFlipModel(numPlayers, numAIPlayers, deck);
        UnoFlipModelViewFrame view = new UnoFlipModelViewFrame(unoGame);
        view.setVisible(true);
    }
}