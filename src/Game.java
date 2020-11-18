
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Game {

    private static String[] secretWord;
    private static String[] uncoveredLetters;
    private static String guessedLetters = "";
    private static String hangCounter = "";
    private static int noOfLetters;
    private static boolean gameRun;

    private static int difficulty;

    public Game() throws IOException, InterruptedException, ClassNotFoundException {

        Player.addPlayed();
        Player.randomizeTurn();

        ArrayList<String> wordList = new ArrayList<>();
        Game.getGameWords(wordList); //Method which fills the list of words from file.
        String secretWord = Game.getRandomGameWord(wordList); //Collects random word from list.

        this.secretWord = new String[secretWord.length()];
        this.uncoveredLetters = new String[secretWord.length()];
        this.noOfLetters = secretWord.length();
        this.gameRun = true;

        for (int i = 0; i < secretWord.length(); i++) { //Fills and array with "_ " to show status on guessed word.
            this.uncoveredLetters[i] = "_ ";
        }

        for (int i = 0; i < secretWord.length(); i++) {
            this.secretWord[i] = secretWord.toUpperCase().substring(i, i + 1);
        }
    showGame();
    }

    /**
     * Method to ensure the game has been reset at start.
     */
    private static void clearGame(){
        guessedLetters = "";
        hangCounter = "";
    }

    /**
     * Method for extracting words from a .txt into an ArrayList.
     * Words need to be separated by new lines.
     * Tries to do so from a file named "game_words.txt".
     *
     * If the file does not exists the program will create a the file, but add only one word to it.
     *
     * @param list name of list you want to populate.
     */
    public static void getGameWords(ArrayList<String> list) throws FileNotFoundException {
        String gameWord;
        try {
            Scanner getter = new Scanner(new File("game_words.txt"));

            while (getter.hasNextLine()) {
                gameWord = getter.nextLine();
                list.add(gameWord);
            }
        } catch (FileNotFoundException exception) {
            System.out.println("\nCouldn't find file: game_words.txt.\nCreates a game_words.txt (with only one word in it) to the game folder.\nUse the game_words.txt to put new words in the game. Each new word needs to be on a new row. Letters only in the words. \nSorry for the inconvenience!\n");
            File createSavedPlayersFile = new File("game_words.txt");
            PrintWriter out = new PrintWriter(createSavedPlayersFile);
            out.println("SHAME");
            out.close();

            Scanner getter = new Scanner(new File("game_words.txt"));

            while (getter.hasNextLine()) {
                gameWord = getter.nextLine();
                list.add(gameWord);
            }
        }
    }

    /**
     * Method returns a word at a random index in a list.
     *
     * @param wordList name of list from which to pick the word.
     * @return a word.
     */
    public static String getRandomGameWord(ArrayList<String> wordList) {
        return wordList.get((int) (Math.random() * wordList.size() - 1));
    }

    /**
     * Method for getting the current secret word.
     *
     * @return The secret word.
     */
    private static String getSecretArray() {
        String secret = "";
        for (String letter : secretWord) {
            secret += letter;
        }
        return secret;
    }

    /**
     * Method for printing out current status of letters guessed right.
     *
     * @return string of letters.
     */
    public static String getUncoveredLetters() {
        String correct = "";
        for (String letter : uncoveredLetters) {
            correct += letter;
        }
        return correct;
    }

    /**
     * Method updates state of the game depending on user input.
     *
     * @param alpha guessed letter.
     * @throws IOException
     */
    public void update(String alpha) throws IOException, InterruptedException, ClassNotFoundException {
        if (!Arrays.asList(secretWord).contains(alpha)) {
            hangCounter += "*"; //increases hang counter
            guessedLetters += alpha + " "; //adds guessed letter to String of guessed letters
            if (hangCounter.length() == 7) { //check if the hang counter has reached 7
                gameRun = false;

                JOptionPane.showMessageDialog(null,
                        "You failed to complete the word.\n" + Player.getName(Player.getPlayerNumber())
                                + " has been removed from this game."
                                + "\nThe word was: " + getSecretArray(),
                        "You failed to complete the word.",
                        JOptionPane.WARNING_MESSAGE, getIcon());

                Player.removePlayer();
                Player.playersLeft();
            }
            Player.nextPlayer();
        } else {
            for (int i = 0; i < secretWord.length; i++) {
                if (secretWord[i].equals(alpha)) {
                    uncoveredLetters[i] = alpha; //replaces underscore with correct letter.
                }
            }
            guessedLetters += alpha + " "; //adds guessed letter to be shown as already guessed.
        }
        showGame();
    }

    /**
     * Checks if all elements in the uncovered array match the secret array.
     *
     * @return true if matching.
     */
    public static boolean gameWin() {
        String u = getUncoveredLetters();
        String s = getSecretArray();
        return s.equals(u);
    }

    /**
     * Checks if user wants to play again.
     * Use after win/loss.
     *
     * @throws IOException
     */
    public static void playAgain() throws IOException, InterruptedException, ClassNotFoundException {

        int result = JOptionPane.showConfirmDialog(null,
                "\n Game over!\nWould you like to play again?",
                "Play again?", JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,new ImageIcon("hGameOver.gif"));

        switch (result) {
            case JOptionPane.YES_OPTION:
                Player.addRemoved();
                startHangMan();
                break;
            case JOptionPane.NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                new MenuFrame();
                break;
        }

    }

    /**
     * Runs an instance of the game "Hangman".
     *
     * @throws FileNotFoundException
     */
    public static void startHangMan() throws IOException, InterruptedException, ClassNotFoundException {
        clearGame();
        if (Player.getActivePlayersName().contains("Computer")){
            setDifficulty();
        }
        new Game();
    }


    /**
     * computerTurn();
     * the computer player gets a "random" letter from the computerGuess(); and makes a guess.
     */
    private void computerTurn() throws IOException, InterruptedException, ClassNotFoundException {

        String guessedLetter = computerGuess();

        JOptionPane.showMessageDialog(null, ("Hey " + Player.getName(Player.getPlayerNumber()) + "! I'm thinking of a word consisting of: " + noOfLetters + " letters. You can fail a maximum of 7 times!"
                        + "\nSo far you've made this progress: " + getUncoveredLetters()
                        + "\nYou've already guessed the following letters: " + guessedLetters
                        + "\nHang-o-meter: " + hangCounter + " (" + hangCounter.length() +")"
                        + "\nWhat letter do you want to guess? "+ Player.getName(Player.getPlayerNumber()) + " guessed "
                        + guessedLetter) + ".",
                Player.getName(Player.getPlayerNumber()) + " turn!",
                JOptionPane.QUESTION_MESSAGE,getIcon());

        update(guessedLetter);
    }

    /**
     * computerGuess();
     * Makes two arrays for the computer player to "guess" from, one array with only wrong letters and one array with only right letters.
     * The computer makes the guess by a "random" choice between the two arrays, and gets a "random" letter from one of the arrays.
     * That letter is the computer players guess.
     * (if getDifficulty(); == 2 is same as 20% right guess).
     *
     * @return computerGuess
     */
    private String computerGuess() {

        String computerGuess;
        char guess;
        char[] abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] lettersGuessed = guessedLetters.toCharArray();
        ArrayList<Character> guessToFail = new ArrayList<>();
        ArrayList<Character> guessToWin = new ArrayList<>();

        // Adds the possible letters to the guessToFail ArrayList.
        for (int k = 0; abc.length > guessToFail.size(); k++) {
            guessToFail.add(abc[k]);
        }

        // Adds the secretWord to the guessToWin ArrayList.
        for (int i = 0; secretWord.length > i; i++) {
            String s = secretWord[i];
            char c = s.charAt(0);
            guessToWin.add(c);
        }

        // Removes the guessed letters from guessToWin
        for (int j = 0; lettersGuessed.length > j; j++) {
            for (int i = 0; guessToWin.size() > i; i++) {
                if (lettersGuessed[j] == (guessToWin.get(i))) {
                    guessToWin.remove(i);
                }
            }
        }

        // Removes the guessed letters from guessToFail
        for (int i = 0; lettersGuessed.length > i; i++) {
            for (int j = 0; guessToFail.size() > j; j++) {
                if ((lettersGuessed[i]) == guessToFail.get(j)) {
                    guessToFail.remove(j);
                }
            }
        }

        // Removes the letters to win from guessToFail
        for (int i = 0; guessToWin.size() > i; i++) {
            for (int j = 0; guessToFail.size() > j; j++) {
                if (guessToFail.get(j) == (guessToWin.get(i))) {
                    guessToFail.remove(j);
                }
            }
        }


        int random = (int) (Math.random() * 10) + 1;
        int randomLetterToFail = (int) (Math.random() * guessToFail.size());
        int randomLetterToWin = (int) (Math.random() * guessToWin.size());

        if (random <= getDifficulty()) { // if random <= the difficulty, guess a "random letterToWin".
            guess = guessToWin.get(randomLetterToWin);
            computerGuess = String.valueOf(guess);
            return computerGuess;
        } else if (guessToFail.size() == 0) { // if there is no more "fail-letters" to guess, guess a "random letterToWin".
            guess = guessToWin.get(randomLetterToWin);
            computerGuess = String.valueOf(guess);
            return computerGuess;
        } else { // otherwise guss a "random letterToFail".
            guess = guessToFail.get(randomLetterToFail);
            computerGuess = String.valueOf(guess);
            return computerGuess;
        }
    }

    /**
     * Displays the difficulty options for the player, 0-10.
     * The higher the number, the better the computer player is to "guess" letters.
     * 0 = never right.
     * 10 = always right.
     * 2 = 20% right.
     *
     * @return difficulty
     */
    private static int setDifficulty(){

        ArrayList <Integer> possibleDifficulties = new ArrayList<>();

        for (int i = 0; i <= 10; i++){
            possibleDifficulties.add(i);
        }

        Object[] difficultyLevel = new Object [possibleDifficulties.size()];

        for (int i = 0; possibleDifficulties.size() > i; i++){
            difficultyLevel[i] = possibleDifficulties.get(i);
        }

                difficulty = JOptionPane.showOptionDialog(
                null,
                "Choose how good the computer player is to guess letters."
                        + "\n0 = never right. 10 = always right. 2 = 20% right.",
                "Computer Player Difficulty",JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon("hGameOver.gif"),
                difficultyLevel,
                        difficultyLevel[2]);

return difficulty;

    }

    /**
     * From setDifficulty.
     * Used in computerGuess();.
     * @return difficulty
     */
    private int getDifficulty(){
        return this.difficulty;
    }

    /**
     * Displays the game to the player and the possible letter-options.
     * Already guessed letters are removed.
     * The game suggests a random letter to the player.
     * (Not to confuse with the letters the computer player gets by "random")
     *
     * @return guess.toString();
     */
    private static String guessDialogBox() {

        char[] abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] lettersGuessed = guessedLetters.toCharArray();
        ArrayList<Character> guessToFail = new ArrayList<>();
        ArrayList<Character> guessToWin = new ArrayList<>();

        ArrayList<Character> possibleGuess = new ArrayList<>();

        // Adds the possible letters to the guessToFail ArrayList.
        for (int k = 0; abc.length > guessToFail.size(); k++) {
            guessToFail.add(abc[k]);
        }

        // Adds the secretWord to the guessToWin ArrayList.
        for (int i = 0; secretWord.length > i; i++) {
            String s = secretWord[i];
            char c = s.charAt(0);
            guessToWin.add(c);
        }

        // Removes the guessed letters from guessToWin
        for (int j = 0; lettersGuessed.length > j; j++) {
            for (int i = 0; guessToWin.size() > i; i++) {
                if (lettersGuessed[j] == (guessToWin.get(i))) {
                    guessToWin.remove(i);
                }
            }
        }

        // Removes the guessed letters from guessToFail
        for (int i = 0; lettersGuessed.length > i; i++) {
            for (int j = 0; guessToFail.size() > j; j++) {
                if ((lettersGuessed[i]) == guessToFail.get(j)) {
                    guessToFail.remove(j);
                }
            }
        }

        // Removes the letters to win from guessToFail
        for (int i = 0; guessToWin.size() > i; i++) {
            for (int j = 0; guessToFail.size() > j; j++) {
                if (guessToFail.get(j) == (guessToWin.get(i))) {
                    guessToFail.remove(j);
                }
            }
        }

        // (guessToWin + guessToFail) == possibleGuess == possibilities
        for (int i = 0; guessToFail.size() > i; i++) {
            possibleGuess.add(guessToFail.get(i));
        }

        // remove doubles (if the word is like "LETTER", then only "LETR" is printed in the GUI-guessbox.
        for (int i = 0; guessToWin.size() > i; i++) {
            if (!possibleGuess.contains(guessToWin.get(i))){
                possibleGuess.add(guessToWin.get(i));
            }
            else{
                // do not add the letter.
            }
        }

        // Sorts the possibleGuess
        Collections.sort(possibleGuess);
        // "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"

        // Add the ArrayList<Character> possibleGuess to the Array of Objects possibilities.
        Object[] possibilities = new Object [possibleGuess.size()];
        for (int i = 0; possibleGuess.size() > i; i++){
            possibilities[i] = possibleGuess.get(i);
        }

        // Random letter possibilities[r], to suggest the random letter.
        int r = (int) (Math.random() * possibilities.length);

        // the GUI-box.
        Character guess = (Character) JOptionPane.showInputDialog(
                null,
                ("Hey " + Player.getName(Player.getPlayerNumber()) + "! I'm thinking of a word consisting of: " + noOfLetters + " letters. You can fail a maximum of 7 times!"
                        + "\nSo far you've made this progress: " + getUncoveredLetters()
                        + "\nYou've already guessed the following letters: " + guessedLetters
                        + "\nHang-o-meter: " + hangCounter + " (" + hangCounter.length() +")"
                        + "\nWhat letter do you want to guess?"),
                Player.getName(Player.getPlayerNumber()) + "! It is your turn! Choose a letter!",
                JOptionPane.QUESTION_MESSAGE, getIcon(),
                possibilities, possibilities[r]);

        if (guess == null){ // Option for cancel-button.
            new MenuFrame();

        }
        return guess.toString();
    }

    private static Icon getIcon() {
            Icon icon = new ImageIcon(hangCounter.length() + ".gif");
            return icon;

    }

    /**
     * Shows the current status of the game.
     *
     * @throws IOException
     */
    public void showGame() throws IOException, InterruptedException, ClassNotFoundException {

        while (gameRun) { //ensures that game will only play if gameRun has been set to (true)
            if (!gameWin()) { //this code block will run if player has not yet won

            if (Player.getName(Player.getPlayerNumber()).contains("Computer")) { // Computer players turn
                    computerTurn();
                } else { // Players turn
               String guessedLetter = guessDialogBox();
               update(guessedLetter);
            }
        }
            else{ //when player has guessed entire word correctly
                    System.out.println(Player.getName(Player.getPlayerNumber()) + "! You won!");
                    System.out.println("The word was: " + getSecretArray());
                gameRun = false;

                    Player.addPoint(Player.getPlayerNumber());
                    Player.updateAverageScore();
                    Player.updateMaxScore();

                JOptionPane.showMessageDialog(null, Player.getName(Player.getPlayerNumber()) + "! You won!"
                        + "\nThe word was: " + getSecretArray(),
                        Player.getName(Player.getPlayerNumber()) + "! You won!",
                        JOptionPane.PLAIN_MESSAGE,
                        new ImageIcon("hWon.gif"));

                Player.playersLeft();
            }
        }
    }
}







