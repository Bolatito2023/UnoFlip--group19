import java.util.*;

public class main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Number of players must be 2 to 4 ");
        System.out.print("Enter the number of players: ");
        int numPlayers = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                numPlayers = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character
                if (numPlayers>=2&&numPlayers<=4) {
                    validInput = true;
                } else {
                    System.out.println("Please enter a number between 2 and 4:");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 2 and 4:");
                scanner.next();
            }
        }

        List<UnoPlayer> players = new ArrayList<>();
        Deck deck = new Deck();
        deck.giveDeck();
        deck.shuffle();


        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter the name of player " + (i + 1) + ": ");
            String playerName = scanner.nextLine();
            UnoPlayer player = new UnoPlayer(playerName, deck);
            players.add(player);
        }

        UnoFlip unoGame = new UnoFlip(players, deck);

        unoGame.play();
        scanner.close();
    }
}
