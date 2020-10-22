import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private final String[] secretWord;
    private final String[] uncoveredLetters;
    private String guessedLetters = "";
    private String hangCounter= "";
    private final int noOfLetters;
    private boolean gameRun;
    public Game() {

        ArrayList<String> wordList = new ArrayList<>();
        Game.getGameWords(wordList); //Metod fyller listan med ord från filen
        String secretWord = Game.getRandomGameWord(wordList); //hämtar slumpat ord från listan

        this.secretWord = new String[secretWord.length()];
        this.uncoveredLetters = new String[secretWord.length()];
        this.guessedLetters = guessedLetters;
        this.hangCounter = hangCounter;
        this.noOfLetters = secretWord.length();
        this.gameRun=true;

        for (int i=0;i<secretWord.length();i++) { // Fyller en array med understreck, för att visa status på ordet man gissar på
            this.uncoveredLetters[i]="_";
        }

        for (int i = 0;i<secretWord.length();i++) {
            this.secretWord[i] = secretWord.toUpperCase().substring(i,i+1);
        }
    }

    /**
     * Method for extracting words from a .txt into an ArrayList.
     * Words need to be separated by new lines.
     * Tries to do so from a file named "game_words.txt".
     * @param list name of list you want to populate.
     */
    public static void getGameWords(ArrayList<String>list) {
        String gameWord;
        try {
            File myFile = new File("game_words.txt");
            Scanner getter = new Scanner(new File("game_words.txt"));

            while (getter.hasNextLine()) {
                gameWord = getter.nextLine();
                list.add(gameWord);
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Couldn't find file: game_words.txt");
        }
    }

    /**
     * Method returns a word at a random index in a list.
     * @param wordList name of list from which to pick the word.
     * @return a word.
     */
    public static String getRandomGameWord(ArrayList<String>wordList) {
        return wordList.get((int)(Math.random()*wordList.size()-1));
    }

    /**
     * Method for getting the current secret word.
     * @return The secret word.
     */
    private String getSecretArray() {
        String secret = "";
        for (String letter: this.secretWord) {
            secret += letter;
        }
        return secret;
    }

    /**
     * Method for printing out current status of letters guessed right.
     * @return string of letters.
     */
    public String getUncoveredLetters() {
        String correct = "";
        for (String letter : this.uncoveredLetters) {
            correct += letter;
        }
        return correct;
    }

    /**
     * Returns true if user tries to guess a letter that has
     * been previously guessed.
     * @param guessedLetter User input
     * @return true if duplicate occurs.
     */
    private boolean doubleLetter(String guessedLetter) {
        if (this.guessedLetters.contains(guessedLetter)) {
            System.out.println("You've already guessed this letter, use another one!");
            return true;
        }
        return false;
    }

    /**
     * Method updates state of the game depending on user input.
     * @param alpha guessed letter.
     * @throws IOException
     */
    public void update(String alpha) throws IOException {
        if (!Arrays.asList(secretWord).contains(alpha)) {
            this.hangCounter += "*"; //increases hang counter
            this.guessedLetters += alpha + " "; //adds guessed letter to String of guessed letters
            if (this.hangCounter.length()==10) { //check if the hang counter has reached 10
                gameRun = false;
                Player.addPlayed();

                System.out.println("Game over!\nWould you like to play again?");
                playAgain();
            }
        }
        else {
            for (int i = 0;i< this.secretWord.length;i++) {
                    if (this.secretWord[i].equals(alpha)) {
                        this.uncoveredLetters[i]=alpha; //replaces underscore with correct letter.
                    }
            }
            this.guessedLetters += alpha + " "; //adds guessed letter to be shown as already guessed.
        }
        showGame();
    }

    /**
     * Checks if all elements in the uncovered array match the secret array.
     * @return true if matching.
     */
    public boolean gameWin() {
        String u = getUncoveredLetters();
        String s = getSecretArray();
        return s.equals(u);
    }

    /**
     * Checks if user wants to play again.
     * Use after win/loss.
     * @throws IOException
     */
    public void playAgain() throws IOException {
        String answer;
        Scanner in = new Scanner(System.in);

        if (in.hasNext()) {
            answer = in.next();
            if (answer.toUpperCase().equals("YES")) {
                Game game2 = new Game();
                game2.showGame();
            }
            else if (answer.toUpperCase().equals("NO")) {
                gameRun=false;
                Menu.showMenu();
            }
            else {
                System.out.println("Incorrect input. Please enter (yes) or (no): ");
                playAgain();
            }
        }
    }

    /**
     * Runs an instance of the game "Hangman".
     * @throws FileNotFoundException
     */
    public static void startHangMan() throws IOException {
        Game game1 = new Game();
        game1.showGame();
    }

    /**
     * Starts the whole program, beginning with loading players from list
     * and then showing the Menu.
     * @throws FileNotFoundException
     */
    public static void runProgram() throws IOException, ClassNotFoundException {
        Player.loadPlayersFromFile();
        System.out.println(Player.getCurrentPlayers());
        Menu.showMenu();
    }

    /**
     * Shows the current status of the game.
     * @throws IOException
     */
    public void showGame() throws IOException {
        while (gameRun) { //ensures that game will only play if gameRun has been set to (true)
            if (!gameWin()) { //this codeblock will run if player has not yet won

                System.out.println("Hey " + Player.getName() +"! I'm thinking of a word consisting of: " +
                        this.noOfLetters + " letters");
                System.out.println("So far you've made this progress: " + getUncoveredLetters());
                System.out.println("You've already guessed the following letters: " + this.guessedLetters);
                System.out.println("Hang-o-meter: " + this.hangCounter);
                System.out.println("What letter do you want to guess?");
                String guessedLetter = Menu.getAlpha();
                while (doubleLetter(guessedLetter)) {
                    guessedLetter = Menu.getAlpha();
                }
                System.out.println("You guessed: " + guessedLetter);
                update(guessedLetter);
            }
            else { //when player has guessed entire word correctly
                System.out.println("You won!");
                System.out.println("The word was: " + getSecretArray());
                Player.addWins();
                Player.addPlayed(); //player stats are updated

                gameRun=false;
                System.out.println("Do you want to play again? (YES) or (NO):");
                playAgain();
            }
        }
    }
}