
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
    private static boolean gameRun;
    public Game() {

        Player.addPlayed();
        Player.randomizeTurn();

        ArrayList<String> wordList = new ArrayList<>();
        Game.getGameWords(wordList); //Method which fills the list of words from file.
        String secretWord = Game.getRandomGameWord(wordList); //Collects random word from list.

        this.secretWord = new String[secretWord.length()];
        this.uncoveredLetters = new String[secretWord.length()];
        this.noOfLetters = secretWord.length();
        this.gameRun=true;

        for (int i=0;i<secretWord.length();i++) { //Fills and array with "_" to show status on guessed word.
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
    public void update(String alpha) throws IOException, InterruptedException {
        if (!Arrays.asList(secretWord).contains(alpha)) {
            this.hangCounter += "*"; //increases hang counter
            this.guessedLetters += alpha + " "; //adds guessed letter to String of guessed letters
            if (this.hangCounter.length()==10) { //check if the hang counter has reached 10
                gameRun = false;
                //TODO hantering av single/multiplayer mode.
                //TODO här ska spelaren tas bort från matchen, flyttas tillbaka till currentPlayers med ny statistik, currentPoints ska rensas för denne.

                System.out.println("You failed to complete the word.");
                Player.removePlayer();
                System.out.println("The word was: " + getSecretArray());
                Player.playersLeft();
            }
            Player.nextPlayer();
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
    public static void playAgain() throws IOException, InterruptedException {
        String answer;
        Scanner in = new Scanner(System.in);

        if (in.hasNext()) {
            answer = in.next();
            if (answer.toUpperCase().equals("YES")) {
                Player.addRemoved();
                Game game2 = new Game();
                game2.showGame();
            }
            else if (answer.toUpperCase().equals("NO")) {
                gameRun=false;
                Menu.showMenu();
            }
            else {
                System.out.println("Incorrect input. Please enter (YES) or (NO): ");
                playAgain();
            }
        }
    }

    /**
     * Runs an instance of the game "Hangman".
     * @throws FileNotFoundException
     */
    public static void startHangMan() throws IOException, InterruptedException {
        Game game1 = new Game();
        game1.showGame();
    }

    /**
     * Starts the whole program, beginning with loading players from list
     * and then showing the Menu.
     * @throws FileNotFoundException
     */
    public static void runProgram() throws IOException, ClassNotFoundException, InterruptedException {
        Player.loadPlayersFromFile();
        Menu.showMenu();
    }

    private void computerTurn() throws IOException, InterruptedException {

        Thread.sleep(2500);

        String guessedLetter = computerGuess();

        System.out.println("You guessed: " + guessedLetter + "\n\n");
        update(guessedLetter);
    }


    private String computerGuess(){

        String computerGuess;
        char guess;
        char[] abc = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        char[] lettersGuessed = this.guessedLetters.toCharArray();
        ArrayList<Character> guessToFail = new ArrayList<>();
        ArrayList<Character> guessToWin = new ArrayList<>();

        for (int k = 0; abc.length > guessToFail.size(); k++) {
            guessToFail.add(abc[k]);
        }

        for (int i = 0; this.secretWord.length > i; i++){
            String s = this.secretWord[i];
            char c = s.charAt(0);
            guessToWin.add(c);
        }

            for(int j = 0; lettersGuessed.length > j; j++) {
                for(int i = 0; guessToWin.size() > i; i++){
                if (lettersGuessed[j] == (guessToWin.get(i))) {
                    guessToWin.remove(i);
                }
            }
        }

        for(int i = 0; lettersGuessed.length > i; i++) {
            for (int j = 0; guessToFail.size() > j; j++) {
                if ((lettersGuessed[i]) == guessToFail.get(j)) {
                    guessToFail.remove(j);
                }
            }
        }
            for (int i = 0; guessToWin.size() > i; i++) {
                for (int j = 0; guessToFail.size() > j; j++) {
                    if (guessToFail.get(j) == (guessToWin.get(i))) {
                        guessToFail.remove(j);
                    }
                }
            }

        int random = (int) (Math.random() * 10) + 1;
        int randomLetterToFail = (int) (Math.random() * guessToFail.size() );
        int randomLetterToWin = (int) (Math.random() * guessToWin.size() );

        if (random <= 2){
            guess = guessToWin.get(randomLetterToWin);
            computerGuess = String.valueOf(guess);
            return computerGuess;
        }
        else if (guessToFail.size() == 0){
            guess = guessToWin.get(randomLetterToWin);
            computerGuess = String.valueOf(guess);
            return computerGuess;
        }
        else{
            guess = guessToFail.get(randomLetterToFail);
            computerGuess = String.valueOf(guess);
            return computerGuess;
        }

    }


    /**
     * Shows the current status of the game.
     * @throws IOException
     */
    public void showGame() throws IOException, InterruptedException {
        while (gameRun) { //ensures that game will only play if gameRun has been set to (true)
            if (!gameWin()) { //this code block will run if player has not yet won

                System.out.println("Hey " + Player.getName(Player.getPlayerNumber()) +"! I'm thinking of a word consisting of: " +
                        this.noOfLetters + " letters. You can fail a maximum of 10 times!");
                System.out.println("So far you've made this progress: " + getUncoveredLetters());
                System.out.println("You've already guessed the following letters: " + this.guessedLetters);
                System.out.println("Hang-o-meter: " + this.hangCounter);
                System.out.println("What letter do you want to guess?");

                if (Player.getName(Player.getPlayerNumber()).contains("Computer")){
                    computerTurn();
                }
                else {
                    String guessedLetter = Menu.getAlpha();
                while (doubleLetter(guessedLetter)) { // if letter has already been used, program tries to fetch another letter
                    guessedLetter = Menu.getAlpha();
                }
                System.out.println("You guessed: " + guessedLetter + "\n\n");
                update(guessedLetter);
            }
            }
            else { //when player has guessed entire word correctly
                System.out.println(Player.getName(Player.getPlayerNumber())+"! You won!");
                System.out.println("The word was: " + getSecretArray());

                //Player.addWins(Player.getPlayerNumber()); //TODO denna bör ske när en spelare finns kvar
                Player.addPoint(Player.getPlayerNumber());
                Player.updateAverageScore();
                Player.updateMaxScore();

                gameRun=false;
                Player.playersLeft();
            }
        }
    }
}