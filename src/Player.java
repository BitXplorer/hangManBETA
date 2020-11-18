
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static javax.swing.JOptionPane.*;

public class Player implements Serializable {
   private static final long serialVersionUID = 1337; // Used to ensure the header of the storedPlayers.txt-files is correct.

    /*
     * Instance variables
     * name: the players name.
     * wins: the number of games won (multiplayer: the last player still in the game is the winner +1) (singleplayer: if the player completes the word +1).
     * currentScore: the score (completed words, final letter rewards +1) in the current game(s).
     * totalScore: total number of completed words (final letters).
     * maxScore: the most completed words in a game.           //maxScore == bestWinStreak
     * avgScore: average score is (totalScore / totalPlayed).
     * totalPlayed: the number of total games played.
     *
     * currentNumber: the player's position number from the list of current players, currentPlayers.
     * hasPlayerNumber: keeps track of the total number of players, limited to 4 players at the same time.
     * playerNumber: keeps track of the players in the game.
     *
     * computerPlayerNumber: keeps track of the computer players. (see createComputerPlayer(); and removeComputerPlayer();).
     *
     * currentPlayers: is a list with the all players, loaded and created.
     * activePlayer: is the player currently used in the game.
     * removedPlayers: is the players removed from a current game (temporarily removed from the activePlayers-list).
     */
    private String name;
    private int wins;
    private int currentScore;
    private int totalScore;
    private int maxScore;
    private double avgScore;
    private int totalPlayed;

    private static int currentNumber;
    private static int hasPlayerNumber;
    private static int playerNumber;

    private static int computerPlayerNumber = 0;

    private static ArrayList<Player> currentPlayers = new ArrayList<>();
    private static ArrayList<Player> activePlayers = new ArrayList<>();
    private static ArrayList<Player> removedPlayers = new ArrayList<>();

    /**
     * Constructor
     * Object Player.
     *
     * @param name          The players name.
     * @param wins          The number of games won.
     * @param currentScore  The number of completed words in a current game.
     * @param totalScore    The number of total completed words.
     * @param maxScore      The most completed words in a game.
     * @param avgScore      The average score (totalScore / totalPlayed).
     * @param totalPlayed   The number of total games played.
     */
    public Player(String name, int wins, int currentScore, int totalScore, int maxScore, double avgScore, int totalPlayed) {
        this.name = name;
        this.wins = wins;
        this.totalScore = totalScore;
        this.maxScore = maxScore;
        this.avgScore = avgScore;
        this.totalPlayed = totalPlayed;
        this.currentScore = currentScore;
    }



    /**
     * getPlayerNumber
     * used in Player-class and Game.showGame(); to keep track of the players.
     * @return playerNumber
     */
    public static int getPlayerNumber() {
       return playerNumber;
    }

    /**
     * getName();
     * used in Game.showGame(); to print the name of the, in the game, active player.
     * Also used in Game.showGame(); to see if the player is a Computer Player.
     *
     * @return activePlayer.name
     */
    public static String getName(int i) {
        return activePlayers.get(i).name;
    }

    /**
     * getCurrentName();
     * used in activatePlayer();, from the currentNumber, used to get the name of the player, currentName.
     *
     * @param i from the currentNumber.
     * @return currentName
     */
    private static String getCurrentName(int i) {
        String currentName = (currentPlayers.get(i).name);
        return currentName;
    }

    /**
     * getCurrentWins();
     * used in activatePlayer();, from the currentNumber, used to get the player's won games, currentWins.
     *
     * @param i from the currentNumber.
     * @return currentWins
     */
    public static int getCurrentWins(int i) {
        int currentWins = (currentPlayers.get(i).wins);
        return currentWins;
    }

    /**
     * getCurrentTotalScore();
     * used in activatePlayer();, from the currentNumber, used to get the player's totalScore, getCurrentTotalScore.
     *
     * @param i from the currentNumber.
     * @return totalScore
     */
    public static int getCurrentTotalScore(int i){
        return currentPlayers.get(i).totalScore;
    }

    /**
     * getCurrentMaxScore();
     * used in activatePlayer();, from the currentNumber, used to get the player's maxScore, getCurrentMaxScore.
     *
     * @param i from the currentNumber.
     * @return currentWins
     */
    public static int getCurrentMaxScore(int i){
        return currentPlayers.get(i).maxScore;
    }

    /**
     * getCurrentAvgScore();
     * used in activatePlayer();, from the currentNumber, used to get the player's avgScore, getCurrentAvgScore.
     *
     * @param i from the currentNumber.
     * @return avgScore
     */
    public static double getCurrentAvgScore(int i){
        return currentPlayers.get(i).avgScore;
    }

    /**
     * getCurrentAvgScore();
     * used in compare(); in highScore(); used to get the player's avgScore.
     *
     * @return avgScore
     */
    public double getCurrentAvgScore(){
        return this.avgScore;
    }

    /**
     * getCurrentPlayed();
     * used in activatePlayer();, from the currentNumber, used to get the current player's total played games, currentPlayed.
     *
     * @param i from the currentNumber.
     * @return currentPlayed
     */
    private static int getCurrentPlayed(int i) {
        int currentPlayed = (currentPlayers.get(i).totalPlayed);
        return currentPlayed;
    }

    /**
     * getCurrentScore();
     * used in activatePlayer();, from the currentNumber, used to get the player's current score, currentScore.
     *
     * @param i from the currentNumber.
     * @return currentScore
     */
    public static int getCurrentScore(int i){
        return currentPlayers.get(i).currentScore;
    }

    /**
     * getCurrentPlayersName();
     *
     * @return names (of all current players)
     */
    public static String getCurrentPlayersName(){
        String names = "";
        for (int i = 0; currentPlayers.size() > i; i++){
            String n = currentPlayers.get(i).name;
            names = names + "\n " + n;
        }
        return names;
    }

    /**
     * getActivePlayersName();
     *
     * @return names (of all current players)
     */
    public static String getActivePlayersName(){
        String names = "";
        for (int i = 0; activePlayers.size() > i; i++){
            String n = activePlayers.get(i).name;
            names = names + "\n " + n;
        }
        return names;
    }



    /**
     * addWins();
     * adds one victory point to the players victory points when the player wins a game.
     *
     * @param i from the playerNumber
     */
    public static void addWins(int i) {
        activePlayers.get(i).wins = activePlayers.get(i).wins + 1;
        System.out.println(activePlayers.get(i));
    }

    /**
     * addPoint();
     * adds one total score and one current score in Game.showGame(); after a word is completed.
     *
     * @param i from the playerNumber
     */
    public static void addPoint(int i){
        activePlayers.get(i).totalScore +=1;
        activePlayers.get(i).currentScore +=1;
    }

    /**
     * updateMaxScore
     * updates the player's max score after a word is completed and if the score is higher than the old max score.
     * Used in Game.showGame();.
     */
    public static void updateMaxScore(){
        for (int i = 0; i<activePlayers.size(); i++){
            if (activePlayers.get(i).maxScore<activePlayers.get(i).currentScore){
                activePlayers.get(i).maxScore = activePlayers.get(i).currentScore;
            }
        }
    }

    /**
     * updateAverageScore();
     * updates the average score, in Game.showGame, after a word is completed.
     */
    public static void updateAverageScore(){
        for (int i = 0; i<activePlayers.size(); i++){
            if (activePlayers.get(i).totalPlayed == 0){ //important to avoid division with zero.
                activePlayers.get(i).avgScore = 0;
            }
            else{
                double score = activePlayers.get(i).totalScore;
                double played = activePlayers.get(i).totalPlayed;
                double avg = Math.round(score/played*100.0)/100.0;
                activePlayers.get(i).avgScore = avg;
            }
        }
    }

    /**
     * addPlayed();
     * adds one to the players played game counts when a game starts, added in Game.
     *
     */
    public static void addPlayed() {
        for (int j = 0; j < activePlayers.size(); j++) {
            activePlayers.get(j).totalPlayed = activePlayers.get(j).totalPlayed + 1;
        }
    }



    /**
     * resetPlayers();
     *
     * used in Player and in the Menu.showMenu(); to reset the activePlayers-list in order to create a new game and avoid old player data still being active.
     *
     * Set hasPlayerNumber to zero,
     * set playerNumber to zero,
     * activePlayers.clear();,
     * removedPlayers.clear();
     * and clearCurrentScore();.
     *
     */
    public static void resetPlayers() { //Used to reset the activePlayers-list.
        hasPlayerNumber = 0;
        playerNumber = 0;
        activePlayers.clear();
        removedPlayers.clear();
        clearCurrentScore();
    }

    /**
     * clearCurrentScore
     * is used in resetPlayers to clear players current score. When a new set of games starts all players shall start with current score at zero (0).
     */
    public static void clearCurrentScore(){
        for ( int i = 0; currentPlayers.size() > i; i++) {
            currentPlayers.get(i).currentScore = 0;
        }
    }

    /**
     * randomizeTurn();
     * when a new game starts the players turn order is randomized with randomizeTurn();. Used early in Game.
     * It there is more than one player, the turn order will be displayed.
     */
    public static void randomizeTurn(){
        Collections.shuffle(activePlayers);
        System.out.println("The turn order is: ");
        for ( int j = 0; j < activePlayers.size(); j++) {
           System.out.println( (j+1) + " " + activePlayers.get(j).name);
         }
        if (activePlayers.size() > 1) {
            JOptionPane.showMessageDialog(null, "The turn order is: \n" + getActivePlayersName(),"Turn order", INFORMATION_MESSAGE, null );
        }
    }

    /**
     * nextPlayer();
     * when a players turn is up nextPlayer(); changes the playerNumber to the next player's playerNumber, and thereby the turn is changed.
     * Is used in Game.update();, used when a player has guessed a letter wrong.
     */
    public static void nextPlayer() {
        playerNumber = playerNumber + 1;
        if (playerNumber >= activePlayers.size()) {
            playerNumber = 0;
        }
    }

    /**
     * removePlayer();
     * when a player guesses the final letter wrong (at Game.update();), that player is removed from the game with this method.
     * The removed player is temporarily placed in the removedPlayers-list.
     */
    public static void removePlayer(){
        removedPlayers.add(activePlayers.get(Player.getPlayerNumber()));
        activePlayers.remove(Player.getPlayerNumber());
    }

    /**
     * addRemoved();
     * when a new part-game restarts the removed players is allowed in again! Happy days!
     * Used in Game.playAgain();
     */
    public static void addRemoved(){
        for (int i = 0; removedPlayers.size() > i; i++){
            activePlayers.add(removedPlayers.get(i));
        }
        removedPlayers.clear();
    }

    /**
     * removeComputerPlayer();
     * when a game is finished the computer players is be removed from the list of activePlayers and currentPlayers with this method.
     */
    public static void removeComputerPlayer(){
        computerPlayerNumber = 0;
        for(int i = 0; activePlayers.size() > i; i++) {
            if (activePlayers.get(i).name.contains("Computer")) {
                activePlayers.remove(activePlayers.get(i));
                i--;
            }
        }

        for(int i = 0; currentPlayers.size() > i; i++){
            if (currentPlayers.get(i).name.contains("Computer")) {
                currentPlayers.remove(currentPlayers.get(i));
                i--;
            }
        }
    }

    /**
     * playersLeft();
     *
     * checks if there is players still left in the game.
     * If there are players left, then options are presented; to continue, save or go back to the menu.
     * The playersLeft(); handles situations both for single player-games and for multiplayer-games and handle the addWins(); accordingly to the game type.
     *
     */
    public static void playersLeft() throws IOException, InterruptedException, ClassNotFoundException {
       // If there is more than one player in the game, options below will be displayed.
        if (activePlayers.size() > 1) {
            nextPlayer();
            System.out.println("\nCONTINUE this game? SAVE this game and go back to the menu? Or to go back the MENU without saving?\n Enter (CONTINUE), (SAVE) or (MENU).");

            Object[] options = {"Continue", "Save", "Menu"};

            int answer = JOptionPane.showOptionDialog(null,
                    getActivePlayersName() + " are still in the game!\n" +
                            "\nContinue this game? " +
                            "\nSave this game and go back to the menu? " +
                            "\nOr exit to the menu without saving?",
                    "Continue this game?",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("hGameOver.gif"),
                    options,
                    options[0]);
            switch (answer) {
                case JOptionPane.YES_OPTION:
                    Game.startHangMan();
                    break;
                case NO_OPTION:
                    saveGame();
                    break;
                case JOptionPane.CANCEL_OPTION:
                case JOptionPane.CLOSED_OPTION:
                    new MenuFrame();
                    break;
            }
        }
else{
            // Last player in multiplayer game gets +1 wins.
                if (removedPlayers.size() >= 1  && activePlayers.size() == 1) {
                    nextPlayer();
                    addWins(Player.getPlayerNumber());
                    JOptionPane.showMessageDialog(null, Player.getName(Player.getPlayerNumber()) + "! You survived and won!",
                            Player.getName(Player.getPlayerNumber()) + "! You won!",
                            JOptionPane.INFORMATION_MESSAGE, new ImageIcon("hWon.gif"));
                    Game.playAgain();

                    // Win single player game (the word is completed), 1+ wins.
                } else if (activePlayers.size() == 1 && removedPlayers.size() == 0) {
                    addWins(Player.getPlayerNumber());
                    Game.playAgain();

                    // Fail game (no wins added).
                } else {
                    Game.playAgain();
                }
            }
        }

    /**
     * addPlayer();
     * starting off from the hasPlayerNumber, which keeps tracks of the number of players in the game, different options will be presented.
     * First player will be directed to the PlayerFrame();
     * Once there is a player there will be options to add new players, computer players, start the game or go back to the menu, ModifyGameFrame();.
     * After the fourth player is added the game will start, Game.startHangman();.
     */
    public static void addPlayer() throws IOException, InterruptedException, ClassNotFoundException {
        if (hasPlayerNumber == 0) {
            new PlayerFrame();
        }
        if (hasPlayerNumber != 0 && hasPlayerNumber < 4) {
            new ModifyGameFrame();
        }
        if (hasPlayerNumber == 4) {
            Game.startHangMan();

        }
    }

    /**
     * loadPlayersFromFile();
     * (runs first in Main).
     * loads the Players form the file, "storedPlayers.txt", and adds them to a list of loadedPlayers.
     * Locates the file, storedPlayers.txt. (Throws exception if there is an error).
     * (Try the input on the file are objects, catches with an exception).
     * The players on the loadedPlayers-list is then added to the list of the currentPlayers.
     *
     * Creates the file, "storedPlayers.txt", if it does not exists.
     *
     * @throws ClassNotFoundException when class not found.
     */
    public static ArrayList<Player> loadPlayersFromFile() throws ClassNotFoundException {

        ArrayList<Player> loadedPlayers = new ArrayList<>();

        try {File savedPlayersFile = new File("storedPlayers.txt");
            if (savedPlayersFile.exists()){
                ObjectInputStream input = new ObjectInputStream(new FileInputStream("storedPlayers.txt"));

                Object player = input.readObject();
                loadedPlayers = (ArrayList<Player>) player;

                input.close();
                currentPlayers.addAll(loadedPlayers);
                return loadedPlayers;
            }
            else{

                File createPlayersFile = new File ("storedPlayers.txt");
                ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(createPlayersFile));
                output.writeObject(currentPlayers); // Writes the object to the file (if no file, currentPlayers is empty).
                output.close();

            }
        }catch (IOException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
        return loadedPlayers;
    }

    /**
     * loadPlayer();
     * sets one Player from the list of currentPlayers to the activePlayer.
     * First shows the names of the currentPlayers.
     * Prompts to enter a name.
     * If the name is in the list that the player will be loaded (sat to the activePlayer).
     * Else there is an option to create that player.
     */
    public static int loadPlayer() throws IOException, InterruptedException, ClassNotFoundException {
        String text; // Used for the player's name

        String loadedName;
        System.out.println("These are the players from before: ");
        for (int i = 0; i < currentPlayers.size();i++){
            loadedName = currentPlayers.get(i).name; // Gets the name of the Player
            System.out.println(loadedName); // Prints the name
        }
        System.out.println("\nEnter the name of the player to load or a new name to create a player: ");

        text = PlayerFrame.setPlayerTextField(); // Get input from the GUI

        int i = 0;
        boolean found = false;

        while (i < currentPlayers.size() && !found) {
            if (text.equals(currentPlayers.get(i).name)) {
                found = true;
            } else {
                i++;
            }
        }

        if (found) {
            System.out.println( text + " found!");
            currentNumber = i;
            Player.activatePlayer();
            addPlayer();

        } else {

            new CreateFrame(); // Create player

        }
        return i;
    }

    /**
     * createPlayer();
     * called from the CreateFrame-Class (after Player.loadPlayer());, if there is no player to load with the specified name.
     * Once a valid name is entered the player will be created (after "yes"-button in CreateFrame) and added to the array list of players, currentPlayers.
     *
     * The players position number in the currentPlayers-list will be added to the currentNumber instance variable.
     * activatePlayer(); is called and the player will be activated as the activePlayer from that method.
     */
    public static void createPlayer(String newName) throws IOException, InterruptedException, ClassNotFoundException {
            Player newPlayer = new Player(newName, 0,0,0,0,0, 0);
            currentPlayers.add(newPlayer);
            currentNumber = currentPlayers.size() - 1;
            activatePlayer();
            addPlayer();
        }



    /**
     * createComputerPlayer();
     * creates a computer player, recognized by the word "Computer" in the rest of the program.
     * Called in the ModifyGameFrame when a computer player is added.
     */
    public static void createComputerPlayer() throws IOException, InterruptedException, ClassNotFoundException {
        computerPlayerNumber++;
        String computerName = "Computer Player " + computerPlayerNumber;
        Player NewComputerPlayer = new Player(computerName, 0,0,0,0,0, 0);
        currentPlayers.add(NewComputerPlayer);
        currentNumber = currentPlayers.size() - 1;
        activatePlayer();
        addPlayer();
    }

    /**
     * activatePlayer();
     * activates the player to the game, and ables the player to be saved.
     * activatePlayer(); uses the players position number from the list of currentPlayers,
     * the position number, currentNumber, is then used to get the information about the player and
     * creates a new player outside of the currentPlayers-list.
     * The newly created player, aPlayer, is then added to the activePlayers-list.
     */
    private static void activatePlayer() {
        int i = currentNumber;
        Player aPlayer = new Player(getCurrentName(i), getCurrentWins(i), getCurrentScore(i), getCurrentTotalScore(i), getCurrentMaxScore(i), getCurrentAvgScore(i), getCurrentPlayed(i));

        if (hasPlayerNumber == 3) {
            activePlayers.add(3, aPlayer);
            hasPlayerNumber = 4;
        } else if (hasPlayerNumber == 2) {
            activePlayers.add(2, aPlayer);
            hasPlayerNumber = 3;
        } else if (hasPlayerNumber == 1) {
            activePlayers.add(1, aPlayer);
            hasPlayerNumber = 2;
        } else if (hasPlayerNumber == 0) {
            activePlayers.add(0, aPlayer);
            hasPlayerNumber = 1; // Changes the number to the next player/list-position
        } else {
            hasPlayerNumber = 0;
            activePlayers.add(0, aPlayer);
            hasPlayerNumber = 1;
        }

            System.out.println(aPlayer);
            System.out.println("Player nr: " + hasPlayerNumber);
        }



    /**
     * addActiveToCurrent();
     * adds the activePlayer to the list of currentPlayers.
     * Used in savePlayersToFile(); to store the activePlayer's progress in the game to the list of current players before the list is saved.
     * Used in highscore(); to add the activePlayers to the currentPlayers.
     */
    private static void addActiveToCurrent(){

      for ( int j = 0; j < activePlayers.size(); j++) {
        for (int i = 0; i < currentPlayers.size(); i++) {
            if (currentPlayers.get(i).name.equals(activePlayers.get(j).name)) { // if name i (from currentPlayers) is the same as name j (from activePlayers),
                currentPlayers.set(i,(activePlayers.get(j))); // write over the found player i (name i) with the player j (name j).
            }
            }
        }
    }

    /**
     * savePlayersToFile();
     * (Option 3. Save player from the menu, MenuFrame-Class).
     * saves the currentPlayers to storedPlayers.txt-file.
     * First adds the activePlayer to the currentPlayers-list.
     * Locates the file, storedPlayers.txt. (Throws exception if there is an error).
     * With ObjectOutputStream writes the currentPlayers-list to the file.
     * Prints a confirmation message, then the menu is show again, Menu.showMenu();.
     * @throws IOException
     */
    public static void savePlayersToFile() throws IOException {
        addActiveToCurrent();
        File savePlayers = new File ("storedPlayers.txt");
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(savePlayers));
        output.writeObject(currentPlayers);
        System.out.println(currentPlayers);
        output.close();
        System.out.println("Players saved.");
        resetPlayers();

        JOptionPane.showMessageDialog(null, "Players saved.", "Players saved.", JOptionPane.INFORMATION_MESSAGE);

        new MenuFrame();
    }



    /**
     * loadGame();
     * option 2. Load game from the MenuFrame-Class.
     * reads from two files the saved activePlayers and the saved removedPlayers,
     * then starts the game with the players from these two arrays.
     *
     */
    public static void loadGame() throws ClassNotFoundException {

        String activePlayersFile = "savedActivePlayers.txt";
        String removedPlayersFile = "savedRemovedPlayers.txt";
        try {
            File savedActivePlayers = new File(activePlayersFile);
            File savedRemovedPlayers = new File(removedPlayersFile);
            if (!savedActivePlayers.exists() || !savedRemovedPlayers.exists()) { // If the file do not exists then do nothing.
                System.out.println("There is no game saved.");
                JOptionPane.showMessageDialog(null, "There is no game saved.");
                new MenuFrame();
            } else {
                resetPlayers(); // If there is players from a running game, those players are removed from the two arrays of players.

                ObjectInputStream input = new ObjectInputStream(new FileInputStream("savedActivePlayers.txt"));
                Object activePlayer = input.readObject();
                activePlayers = (ArrayList<Player>) activePlayer;

                ObjectInputStream in = new ObjectInputStream(new FileInputStream("savedRemovedPlayers.txt"));
                Object removedPlayer = in.readObject();
                removedPlayers = (ArrayList<Player>) removedPlayer;

               JOptionPane.showMessageDialog(null, "Game loaded.", "Game loaded.", JOptionPane.INFORMATION_MESSAGE);
                Game.startHangMan();
                }

            }catch (IOException | InterruptedException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }

    /**
     * saveGame();
     * saves the players in a game to two files, one file for each array, activePlayers and removedPlayers.
     * The game is then put on halt until it is loaded again.
     */
    public static void saveGame() throws IOException {

        String activePlayersFile = "savedActivePlayers.txt"; // Names the file
        File savedActivePlayers = new File(activePlayersFile); // Creates a file
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(savedActivePlayers));
        out.writeObject( activePlayers ); // Writes the activePlayers to the file
        out.close(); // Important to close the file.

        String removedPlayersFile = "savedRemovedPlayers.txt"; // Names the file
        File savedRemovedPlayers = new File(removedPlayersFile); // Creates a file
        ObjectOutputStream outs = new ObjectOutputStream(new FileOutputStream(savedRemovedPlayers));
        outs.writeObject( removedPlayers ); // Writes the removedPlayers to the file
        outs.close(); // Important to close the file.

        System.out.println("Game has been saved.");
        new MenuFrame();
    }



    /**
     * highScore();
     * shows the five top players according to their average score.
     * There is an option for the player to save the highscore on a .txt-file, saveHighScore();
     */
    public static void highScore() throws IOException {

        String top5 = " These are the top five players in Hangman based on average score!: \n";

        addActiveToCurrent();

        ArrayList<Player> highScorelist = (ArrayList<Player>) currentPlayers.clone(); //Cloning currentPlayers list to a new highScore list.
        Collections.sort(highScorelist, new Comparator<Player>() { //Sorting the highScoreList in Descending order for average score value.

            @Override
            public int compare(Player o1, Player o2) {
                return Double.compare(o2.getCurrentAvgScore(), o1.getCurrentAvgScore());
            }
        });
        for(int i = 5;i<highScorelist.size();i++){ //removing all players except top 5 in list.
            highScorelist.remove(i);
        }

        for(int i = 0; i<highScorelist.size();i++) { // Printing Name and average score for players in top five.
            top5 = top5 + (highScorelist.get(i).name
                    + ", Average Score: " + highScorelist.get(i).avgScore
                    + ", Wins: " + highScorelist.get(i).wins
                    + ", Best win streak: " + highScorelist.get(i).maxScore
                    + ", Total games played: " + highScorelist.get(i).totalPlayed
                    + "\n");
        }

        int result = JOptionPane.showConfirmDialog(null,
                top5 + "\nWould you like to save these highscores to a .txt file?");
        switch (result) {
            case JOptionPane.YES_OPTION:
                saveHighScores(top5);
                JOptionPane.showMessageDialog(null,
                        "The highscores have been saved to highscores.txt\nRemember to rename or replace the file if you like to keep it.");
                new MenuFrame();
                break;
            case NO_OPTION:
            case JOptionPane.CANCEL_OPTION:
            case JOptionPane.CLOSED_OPTION:
                new MenuFrame();
                break;
        }
        }

    /**
     * saveHighScore();
     * saves the top five players to "highscores.txt"-file.
     * @param top5Players
     */
    public static void saveHighScores(String top5Players) throws FileNotFoundException {
        File highScores = new File("highscores.txt");
        PrintWriter out = new PrintWriter(highScores);
        out.write(String.valueOf(top5Players));
        out.close();
    }



    /**
     * toString();
     * Overrides the Player's information to string.
     * @return a string with the Player's name, won games and total played gamed (then ends the line).
     */
    @Override
    public String toString(){
        return "[Name: " + this.name + ", Wins: " + this.wins + ", Total score: " + this.totalScore + ", Max score: " + this.maxScore +
                ", Average score: " + this.avgScore + ", Games played: " + this.totalPlayed + "] \n";
    }
}
