import java.io.IOException;
import java.util.Scanner;

/**
 * Class for the Menu of the game.
 */
public class Menu {

    /**
     * A Scanner for an integer input.
     * Validates that the input is an Integer.
     *
     * @return value a validated integer value.
     */
    public static int getInt() {
        int value = -1;
        Scanner in = new Scanner(System.in);
        System.out.print("Enter a number: ");

        if (in.hasNextInt()) { // If input is a valid Integer
            value = in.nextInt();
        }
        else{ // Else input is not an Integer, error message
            System.out.println("Input is not an Integer.");
        }
        return value;
    }


    /**
     * getString is a method to get a Scanner for the player to print a string-word without space (" ").
     * @return String line
     */
    public static String getString(){
        Scanner in = new Scanner(System.in);
        String input = "";

        if (in.hasNextLine()) {
            input = in.nextLine();
            if (input.length()<1 ||Character.isWhitespace(input.charAt(0))) {
                System.out.println("You have to enter something, you know. Try it again.");
                return getString();
            }
        }
        return input;
    }
    /**
     * Validates input, returns if input is a letter.
     * @return First letter in input.
     */
    public static String getAlpha()
    {
        char alpha = 0;
        Scanner in = new Scanner(System.in);
        in.useDelimiter("");

        if (in.hasNext())
        {
            alpha = in.next().charAt(0);
            if (!Character.isLetter(alpha))
            {
                System.out.println("That's not a letter, dear. Try again.");
                return getAlpha();
            }
        }
        String stringAlpha = Character.toString(alpha);
        return stringAlpha.toUpperCase();
    }

    /**
     * Prints the menu and the four choices, 1-4.
     * Also validates the input between 1-4.
     * Takes the player to the player's choice.
     */
    public static void showMenu() throws IOException {

        System.out.println(
                "\nHangMan 1.0  \n" +
                        "\n MENU  \n" +
                        " 1. Play\n 2. Load game\n 3. Save player\n 4. Highscore \n 5. Quit \n");

        int input = getInt();

        if (input > 5 || input < 1) { // If input is not a valid Int
            System.out.println("Enter a Integer between 1-4: ");
            showMenu();
        }
        if (input == 1) {
            System.out.println("1. Play\n");
            Player.defaultHasPlayerNumber(); // Set hasPlayerNumber to zero.
            Player.addPlayer();

        } else if (input == 2) {
            System.out.println("2. Load game\n");
           // Player.loadPlayer();
            System.out.println("Här kommer en möjlighet att ladda sparat spel ligga senare"); // todo Player.loadGame();
            showMenu();

        } else if (input == 3) {
            System.out.println("3. Save players\n");
            System.out.println("Här kommer en möjlighet att spara spelare ligga senare"); // todo Player.savePlayersToFile();
            // todo något är skumt med att spara spelare, den sista i currentPlayers-listan sparas över en sista actual-player (actualPlayers-list).
            showMenu();
         //   Player.savePlayersToFile();

        } else if (input == 4) {
            System.out.println("4. Highscore \n");
            System.out.println("Här kommer en möjlighet att spara Highscore ligga senare"); // todo Player.highscore();
            showMenu();

        }else if (input == 5) {
            System.out.println("5. Quit \n Thanks for playing. =)");
    }
    }
}
