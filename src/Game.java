import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private String[] secretWord;
    private String[] uncoveredLetters;
    private String guessedLetters = "";
    private String hangCounter= "";
    private int noOfLetters;
    private boolean gameRun;
    public Game()
    {
        ArrayList<String> wordList = new ArrayList<>();
        Game.getGameWords(wordList);
        String secretWord = Game.getRandomGameWord(wordList);

        this.secretWord = new String[secretWord.length()];
        this.uncoveredLetters = new String[secretWord.length()];
        this.guessedLetters = guessedLetters;
        this.hangCounter = hangCounter;
        this.noOfLetters = secretWord.length();
        this.gameRun=true;

        for (int i=0;i<secretWord.length();i++)
        {
            this.uncoveredLetters[i]="_";
        }

        for (int i = 0;i<secretWord.length();i++)
        {
            this.secretWord[i] = secretWord.toUpperCase().substring(i,i+1);
        }
    }

    public static void getGameWords(ArrayList<String>list) {
        String gameWord = "";
        try {
            File myFile = new File("game_words.txt");
            Scanner getter = new Scanner(new File("game_words.txt"));

            while (getter.hasNextLine()) {
                gameWord = getter.nextLine();
                list.add(gameWord);
            }
        } catch (FileNotFoundException exception)
        {
            System.out.println("Couldn't find file: game_words.txt");
        }
    }

    public static String getRandomGameWord(ArrayList<String>wordList)
    {
        return wordList.get((int)(Math.random()*wordList.size()-1));
    }

    private String getSecretArray()
    {
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
    public String getUncoveredLetters()
    {
        String correct = "";
        for (String letter : this.uncoveredLetters) {
            correct += letter;
        }
        return correct;
    }

    /**
     * Validates input, returns if input is a letter.
     * @return First letter in input.
     */

    /**
     * Returns true if user tries to guess a letter that has
     * been previously guessed.
     * @param guessedLetter User input
     * @return true if duplicate occurs, false if not.
     */
    private boolean doubleLetter(String guessedLetter)
    {
        if (this.guessedLetters.contains(guessedLetter))
        {
            System.out.println("You've already guessed this letter, use another one!");
            return true;
        }
        return false;
    }

    public void update(String alpha) throws IOException {
        if (!Arrays.asList(secretWord).contains(alpha))
        {
            this.hangCounter += "*";
            this.guessedLetters += alpha + " ";
            if (this.hangCounter.length()==10)
            {
                gameRun = false;

                Player.addPlayed();

                System.out.println("Game over!\nWould you like to play again?"); //TODO Här skall antal spelade omgångar öka med 1.
                playAgain();
            }
        }
        else
        {
            for (int i = 0;i< this.secretWord.length;i++)
            {
                for (int j = 0;j< this.uncoveredLetters.length;j++)
                {
                    if (this.secretWord[i].equals(alpha))
                    {
                        this.uncoveredLetters[i]=alpha;
                    }
                }
            }
            this.guessedLetters += alpha + " ";
        }
        showGame();
    }

    public boolean gameWin()
    {
        String u = getUncoveredLetters();
        String s = getSecretArray();
        return s.equals(u);
    }


    public void playAgain() throws IOException {
        String answer;
        Scanner in = new Scanner(System.in);

        if (in.hasNext())
        {
            answer = in.next();
            if (answer.toUpperCase().equals("YES"))
            {
                Game game2 = new Game();
                game2.showGame();
            }
            else if (answer.toUpperCase().equals("NO"))
            {
                gameRun=false;
                Menu.showMenu();
            }
            else
            {
                System.out.println("Incorrect input. Please enter (yes) or (no): ");
                playAgain();
            }
        }
    }

    /**
     * Runs the game "Hangman"
     * @throws FileNotFoundException
     */
    public static void startHangMan() throws IOException {
        Game game1 = new Game();
        game1.showGame();
    }

    /**
     * Starts the whole program, beginning with showing the Menu.
     * @throws FileNotFoundException
     */
    public static void runProgram() throws IOException { //Denna metod drar igång spelet
        Menu.showMenu();
    }

    public void showGame() throws IOException {
        while (gameRun) {
            if (!gameWin())
            {

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
            else
            {
                System.out.println("You won!");
                System.out.println("The word was: " + getSecretArray());

                //TODO här skall antal spelade OCH vunna matcher öka med 1.
                Player.addPlayed();
                Player.addWins();


                gameRun=false;
                System.out.println("Do you want to play again? (YES) or (NO):");
                playAgain();
            }
        }
    }
}





/* // todo OBS OLD GAME

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Game {

    private String[] secretWord;
    private String[] uncoveredLetters;
    private String guessedLetters = "";
    private String hangCounter= "";
    private int noOfLetters;
    private boolean gameRun;
    public Game()
    {
        ArrayList<String> wordList = new ArrayList<>();
        Game.getGameWords(wordList);
        String secretWord = Game.getRandomGameWord(wordList);

        this.secretWord = new String[secretWord.length()];
        this.uncoveredLetters = new String[secretWord.length()];
        this.guessedLetters = guessedLetters;
        this.hangCounter = hangCounter;
        this.noOfLetters = secretWord.length();
        this.gameRun=true;

        for (int i=0;i<secretWord.length();i++)
        {
            this.uncoveredLetters[i]="_";
        }

        for (int i = 0;i<secretWord.length();i++)
        {
            this.secretWord[i] = secretWord.toUpperCase().substring(i,i+1);
        }
    }

    public static void getGameWords(ArrayList<String>list) {
        String gameWord = "";
        try {
            File myFile = new File("game_words.txt");
            Scanner getter = new Scanner(new File("game_words.txt"));

            while (getter.hasNextLine()) {
                gameWord = getter.nextLine();
                list.add(gameWord);
            }
        } catch (FileNotFoundException exception)
        {
            System.out.println("Couldn't find file: game_words.txt");
        }
    }

    public static String getRandomGameWord(ArrayList<String>wordList)
    {
        return wordList.get((int)(Math.random()*wordList.size()-1));
    }

    private String getSecretArray()
    {
        String secret = "";
        for (String letter: this.secretWord) {
            secret += letter;
        }
        return secret;
    }


    public String getUncoveredLetters()
    {
        String correct = "";
        for (String letter : this.uncoveredLetters) {
            correct += letter;
        }
        return correct;
    }

    /**
     * Validates input, returns if input is a letter.
     * @return First letter in input.
     */
  /*  public String getAlpha()
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
     * Returns true if user tries to guess a letter that has
     * been previously guessed.
     * @param guessedLetter User input
     * @return true if duplicate occurs, false if not.
     */
 /*   private boolean doubleLetter(String guessedLetter)
    {
        if (this.guessedLetters.contains(guessedLetter))
        {
            System.out.println("You've already guessed this letter, use another one!");
            return true;
        }
        return false;
    }

    public void update(String alpha)
    {
        if (!Arrays.asList(secretWord).contains(alpha))
        {
            this.hangCounter += "*";
            this.guessedLetters += alpha + " ";
            if (this.hangCounter.length()==10)
            {
                gameRun = false;
            //    actualPlayer.addPlayed(); //TODO Här skall antal spelade omgångar öka med 1.
                System.out.println("Game over!\nWould you like to play again?");
                playAgain();
            }
        }
        else
        {
            for (int i = 0;i< this.secretWord.length;i++)
            {
                for (int j = 0;j< this.uncoveredLetters.length;j++)
                {
                    if (this.secretWord[i].equals(alpha))
                    {
                        this.uncoveredLetters[i]=alpha;
                    }
                }
            }
                        this.guessedLetters += alpha + " ";
        }
        showGame();
    }

    public boolean gameWin()
    {
        String u = getUncoveredLetters();
        String s = getSecretArray();
        return s.equals(u);
    }

    public void playAgain()
    {
        String answer;
        Scanner in = new Scanner(System.in);

        if (in.hasNext())
        {
            answer = in.next();
            if (answer.toUpperCase().equals("YES"))
            {
                Game game2 = new Game();
                game2.showGame();
            }
            else if (answer.toUpperCase().equals("NO"))
            {
                gameRun=false;

          /*      try {
                    Menu.showMenu(); //TODO gå tillbaka till menyn
                }
                catch(IOException e) {
                    e.printStackTrace();
                } */


  /*          }
            else
            {
                System.out.println("Incorrect input. Please enter (yes) or (no): ");
                playAgain();
            }
        }
    }

    public void showGame()
    {
        while (gameRun) {
            if (!gameWin())
            {

            System.out.println("Hey playerName! I'm thinking of a word consisting of: " +
                    this.noOfLetters + " letters");
            System.out.println("So far you've made this progress: " + getUncoveredLetters());
            System.out.println("You've already guessed the following letters: " + this.guessedLetters);
            System.out.println("Hang-o-meter: " + this.hangCounter);
            System.out.println("What letter do you want to guess?");
            //System.out.println("Secret word is: " + getSecretArray() + " (This is only for cheaters..)");
            String guessedLetter = getAlpha();
            while (doubleLetter(guessedLetter)) {
                guessedLetter = getAlpha();
            }
            System.out.println("You guessed: " + guessedLetter);

            update(guessedLetter);
            }
            else
            {
                System.out.println("You won!");
                System.out.println("The word was: " + getSecretArray());

              //  actualPlayer.addWins();  //TODO här skall antal spelade OCH vunna matcher öka med 1.
              //  actualPlayer.addPlayed();

                gameRun=false;
                System.out.println("Do you want to play again? (YES) or (NO):");
                playAgain();
            }
        }
    }
}
*/