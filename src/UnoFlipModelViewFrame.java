import javax.swing.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.awt.*;

public class UnoFlipModelViewFrame extends JFrame implements UnoFlipModelView {

    //JButton[][] buttons;
    UnoFlipModel model;
    ArrayList<JButton> cardButtons;
    private static List<UnoPlayer> player;
    private Deck deck;
    private JPanel panel;
    private JRadioButton [] wildCardColors;
    private ButtonGroup buttonGroup;
    private  int currentPlayerIndex;
    private UnoFlipModel game;
    private JTextArea southTextArea;
    private JScrollPane scrollPane;
    private UnoFlipModel.Direction direction;
    private JPanel southPanel;
    private JPanel buttonPanel;
    private Card card;
    private Hand usersHand;
    private JButton centerButton;

    UnoFlipEvent event;
    public UnoFlipModelViewFrame() {
        super("UNO FLIP!");
        deck = new Deck();
        deck.giveDeck();
        deck.shuffle();

        //card = new Card();
        usersHand = new Hand();

        player = new ArrayList<UnoPlayer>();
        model = new UnoFlipModel(player,deck);
        model.addUnoFlipView(this);
        direction= UnoFlipModel.Direction.FORWARD;
        event = new UnoFlipEvent(model,direction);
        cardButtons = new ArrayList<JButton>();


        //currentPlayerIndex=0;

        //Ask User for number of players
        int numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("Please enter a number between from 2 to 4:"));
        boolean validInput = false;
        while (!validInput) {
            try{

                if (numberOfPlayers>=2&&numberOfPlayers<=4) {
                    validInput = true;
                } else {
                    numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("Please enter a number between from 2 to 4:"));
                }
            }catch (InputMismatchException e) {
                numberOfPlayers = Integer.parseInt(JOptionPane.showInputDialog("Invalid input. Please enter a number between 2 and 4:"));
            }
        }

        for (int i = 1; i <= numberOfPlayers; i++) {
            String playerName = JOptionPane.showInputDialog("Enter player " + i + "'s name:");
            player.add(new UnoPlayer(playerName,deck));
        }

        // Set the layout manager for the JFrame
        this.setLayout(new BorderLayout());


        // Create and add components to the frame
        JButton drawCardButton = new JButton("Draw Card");

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel yourWildCard = new JLabel ("(Used for Wild card types) Choose a color to be played:");
        panel.add(yourWildCard);
        wildCardColors = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        String [] wildLabels = {"Red", "Blue", "Yellow", "Green"};
        for (int i=0; i<4; i++) {
            wildCardColors[i] = new JRadioButton (wildLabels[i]);
            wildCardColors[i].setActionCommand (wildLabels[i].substring(0, 1));
            buttonGroup.add(wildCardColors[i]);
            panel.add(wildCardColors[i]);
        }
        // Create a panel for the SOUTH region
        southPanel = new JPanel(new BorderLayout());

        UnoPlayer currentPlayer = model.getCurrentPlayer();

        southTextArea = new JTextArea("Player " + currentPlayer.getPlayerName() +"'s Turn");
        southTextArea.setEditable(false);
        centerButton = new JButton("Current Card on Discard Pile");
        centerButton.setHorizontalAlignment(SwingConstants.CENTER);
        JButton nextPlayerButton = new JButton("Next Player");


        // Create a text area for player's cards
        southPanel.add(southTextArea, BorderLayout.CENTER);

        // Create a panel for buttons
        buttonPanel = new JPanel();
        scrollPane = new JScrollPane(buttonPanel);
        southPanel.add(scrollPane, BorderLayout.SOUTH);

        initializeButtonHand();


        this.add(drawCardButton, BorderLayout.EAST);
        this.add(panel, BorderLayout.WEST);
        this.add(southPanel, BorderLayout.SOUTH);
        this.add(centerButton, BorderLayout.CENTER);
        this.add(nextPlayerButton, BorderLayout.NORTH);

        // Set the frame size, default close operation, and make it visible
        this.setSize(800, 800); // Adjust the size as needed
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Adding the button listeners for buttons created

        DrawButtonListener listener1 = new DrawButtonListener(model,event,this);
        drawCardButton.addActionListener(listener1);

        NextPlayerButtonListener listener = new NextPlayerButtonListener(model,event,this);
        nextPlayerButton.addActionListener(listener);



        PlayCardButtonListener listener2 = new PlayCardButtonListener(model,event,this);
        for(JButton button:cardButtons){
            button.addActionListener(listener2);
        }

        this.setVisible(true);



    }

    /** Update the south text area to show which players turn is it
     *
     */

    public void updateSouthTextArea(UnoFlipEvent e ) {
        String text = "Player " + e.getPlayer().getPlayerName() + "'s Turn";
        southTextArea.setText(text);
    }
    public void updateCentre(UnoFlipEvent e){
        //centerButton.setText();
    }
    /**public void initiliazeDiscardPile(){

     centerButton.setText("DISCARD PILE \n" + game.getTopCard() +" ");

     }*/

    /**
     * This method updates a player button when they draw cards or play cards.
     */
    public void updateCards(){
        UnoPlayer player = model.getCurrentPlayer();

        // Get the player's hand
        Hand playerHand = player.getHand();


        // Create lists for the current buttons and the new buttons
        ArrayList<JButton> currentCardButtons = new ArrayList<>(cardButtons);
        ArrayList<JButton> newCardButtons = new ArrayList<>();

        // Update the display of cards in the player's hand
        for (Card card : playerHand.getCards()) {
            JButton cardButton = createCardButton(card); // Create a button for the card
            newCardButtons.add(cardButton);
        }

        // Remove buttons for cards that were played
        for (JButton button : currentCardButtons) {
            if (!newCardButtons.contains(button)) {
                buttonPanel.remove(button);
            }
        }

        // Add buttons for cards that were drawn
        for (JButton button : newCardButtons) {
            if (!currentCardButtons.contains(button)) {
                buttonPanel.add(button);
            }
        }

        // Update the cardButtons list with the new buttons
        cardButtons = newCardButtons;

    }

    /**This method creates a button for a card.
     * */

    public JButton createCardButton(Card card) {
        JButton cardButton = new JButton();

        // Set the button's text to display the card's information (e.g., color and value)
        if (card.getCardType() == Card.CardType.NUMBER){
            cardButton.setText(card.getColour() + "\n " + card.getNumberAsWord());
        }
        else{
            cardButton.setText(card.getColour() + "\n " + card.getCardType());
        }
        cardButton.setPreferredSize(new Dimension(100, 150));
        Font buttonFont = new Font("Arial", Font.PLAIN, 9);
        cardButton.setFont(buttonFont);
        return cardButton;

    }

    /**This method initializes a button of 7 cards for each player.
     *
     */

    public void initializeButtonHand() {
        // Initialize the game; each player gets 7 cards
        game = new UnoFlipModel(player, deck);

        for (UnoPlayer currentPlayer : player) {
            Hand usersHand = currentPlayer.getHand();
            ArrayList<Card> usersCards = usersHand.getCards();

            for (int i = 0; i < usersCards.size(); i++) {
                JButton b = new JButton();
                if(usersCards.get(i).getCardType()==Card.CardType.NUMBER) {
                    b.setText(usersCards.get(i).getColour() + "\n " + usersCards.get(i).getNumberAsWord());
                }
                else{
                    b.setText(usersCards.get(i).getColour() + "\n " + usersCards.get(i).getCardType());
                }

                b.addActionListener(new PlayCardButtonListener(model, event, this));
                buttonPanel.add(b);
                cardButtons.add(b);
                updateCards();
            }
        }



    }





    /** Get the buttons representing the cards of the users.
     **/
    public ArrayList<JButton> getCardButtons () {
        return cardButtons;
    }


    public static void main(String[] args) {

        new UnoFlipModelViewFrame();
    }
}