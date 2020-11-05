
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Player implements Serializable {
    private static final long serialVersionUID = 1337; // Used to ensure the header of the storedPlayers.txt-files is correct. Have experienced crashes without it.

    /*
     * Instance variables
     * name: the players name.
     * wins: the number of games won.
     * totalPlayed: the number of total games played.
     *
     * currentNumber: the player's position number from the list of current players, currentPlayers.
     *
     * currentPlayers: is a list with the all players, loaded and created.
     * activePlayer: is the player currently used in the game.
     */
    private String name;
    private int wins;
    private int totalPlayed;
    private double avgScore;
    private int maxScore;
    private int totalScore;
    private int currentScore;

    private static int currentNumber;
    private static int hasPlayerNumber;
    private static int playerNumber;

    private static int computerPlayerNumber = 0;

    private static String text; // Used for the player's name

    private static ArrayList<Player> currentPlayers = new ArrayList<>();

    private static ArrayList<Player> activePlayers = new ArrayList<>();

    private static ArrayList<Player> removedPlayers = new ArrayList<>();

    /**
     * Constructor
     * Object Player.
     *
     * @param name        The players name.
     * @param wins        The number of games won.
     * @param totalPlayed The number of total games played.
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

    public static int getPlayerNumber() {
       return playerNumber;
    }

    public static void nextPlayer() {
        playerNumber = playerNumber + 1;
        if (playerNumber >= activePlayers.size()) {
            playerNumber = 0;
        }
    }

    /**
     * getName();
     * used in Game.showGame(); to print the name of the, in the game, active player.
     *
     * @return activePlayer.name
     */
    public static String getName(int i) {
        return activePlayers.get(i).name;
    }

    /**
     * addWins();
     * adds one victory point to the players victory points when the player wins a game.
     */
    public static void addWins(int i) {
        activePlayers.get(i).wins = activePlayers.get(i).wins + 1;
    }

    public static void addPoint(int i){
        activePlayers.get(i).totalScore +=1;
        activePlayers.get(i).currentScore +=1;
    }

    /**
     * addPlayed();
     * adds one to the players played game counts after a game is played.
     * Shows the players activePlayer's status (current game won and games played).
     */
    public static void addPlayed() {
        for (int j = 0; j < activePlayers.size(); j++) {
            activePlayers.get(j).totalPlayed = activePlayers.get(j).totalPlayed + 1;
        }
    }

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

    public static void updateMaxScore(){
        for (int i = 0; i<activePlayers.size(); i++){
            if (activePlayers.get(i).maxScore<activePlayers.get(i).currentScore){
                activePlayers.get(i).maxScore = activePlayers.get(i).currentScore;
            }
        }
    }

    public static void showActivePlayers() {
        System.out.println(activePlayers);
    }

    /**
     * startGame();
     * Option for the operator to quit adding payers and start the game.
     * Operator is forced to start the game if there are 4 active players, hasPlayerNumber == 4.
     */
    public static void startGame() throws IOException, InterruptedException, ClassNotFoundException {
        if (hasPlayerNumber == 4) {
            Game.startHangMan();
        } else {
            System.out.println("Do you want to start the game?  (YES) or (NO)?  (MENU) to exit to the menu:");
            String answer;
            Scanner in = new Scanner(System.in);
            if (in.hasNext()) {
                answer = in.next();
                if (answer.toUpperCase().equals("YES")) {
                    Game.startHangMan();
                } else if (answer.toUpperCase().equals("NO")) {
                    addPlayer();
                } else if (answer.toUpperCase().equals("MENU")) {
                    Menu.showMenu();
                } else {
                    System.out.println("Incorrect input. Please try again. ");
                    startGame();
                }
            }
        }
    }

    public static void resetActivePlayers() { //Used to reset the activePlayers-list.
        hasPlayerNumber = 0;
        playerNumber = 0;
        activePlayers.clear();
        removedPlayers.clear();
    }

    public static void addPlayer() throws IOException, InterruptedException, ClassNotFoundException {
        if (hasPlayerNumber == 0) {
            loadPlayer();
        }
        if (hasPlayerNumber != 0 && hasPlayerNumber < 4) {
            System.out.println("Do you want to add another Player to this game?  (YES) or (NO)?  (MENU) to exit to the menu:");
            String answer;
            Scanner in = new Scanner(System.in);

            if (in.hasNext()) {
                answer = in.next();
                if (answer.toUpperCase().equals("YES")) {
                    loadPlayer();
                } else if (answer.toUpperCase().equals("NO")) {
                    addComputerPlayer();
                } else if (answer.toUpperCase().equals("MENU")) {
                    hasPlayerNumber = 0;
                    Menu.showMenu();
                } else {
                    System.out.println("Incorrect input. Please try again. ");
                    addPlayer();
                }
            }
        }
        if (hasPlayerNumber == 4) {
            startGame();
        }
    }


    public static void addComputerPlayer() throws IOException, InterruptedException, ClassNotFoundException {
        System.out.println("Do you want to add a Computer Player?   (YES) or (NO)?");
        String answer;
        Scanner in = new Scanner(System.in);
        if (in.hasNext()) {
            answer = in.next();
            if (answer.toUpperCase().equals("YES")) {
                createComputerPlayer();
            } else if (answer.toUpperCase().equals("NO")) {
                startGame(); }
        } else {
            System.out.println("Incorrect input. Please try again. ");
            addComputerPlayer();
        }
    }


    /**
     * createPlayer();
     * called in the Menu.showMenu();, option 1, to create a player.
     * The player is prompted to enter a name.
     * (If the name is already in the list of currentPlayers the player will be shown an error message and gets a new try to enter a name).
     * Once a valid name is entered the player will be created and added to the array list of players, currentPlayers.
     * The basic info about the player will be shown.
     * <p>
     * The players position number in the currentPlayers-list will be added to the currentNumber instance variable.
     * activatePlayer(); is called and the player will be activated as the activePlayer from that method.
     * checkPlayer(); is called to confirm that the player is added as the activePlayer.
     * From the checkPlayer(); the game then starts.
     */
    public static void createPlayer(String newName) throws IOException, InterruptedException, ClassNotFoundException {
        int i = 0;
        boolean found = false;

        if (newName.contains("Computer")){
            System.out.println(newName + " is not a valid name. Please enter a name without the word \"Computer\".");
            loadPlayer();
        }

        while (i < currentPlayers.size() && !found) {
            if (newName.equals(currentPlayers.get(i).name)) {
                found = true;
            } else {
                i++;
            }
        }

        if (!found) {
            while (i < activePlayers.size()) {
                if (newName.equals(activePlayers.get(i).name)) {
                    found = true;
                } else {
                    i++;
                }
            }
        }

        if (found) {
            System.out.println("Player " + newName + "already exists.");
            loadPlayer();
        } else {
            Player newPlayer = new Player(newName, 0,0,0,0,0, 0);
            currentPlayers.add(newPlayer);
            currentNumber = currentPlayers.size() - 1;
            activatePlayer();
            addPlayer();
        }
    }

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

    public static int getCurrentMaxScore(int i){
        return currentPlayers.get(i).maxScore;
    }

    public static int getCurrentTotalScore(int i){
        return currentPlayers.get(i).totalScore;
    }

    public static double getCurrentAvgScore(int i){
        return currentPlayers.get(i).avgScore;
    }

    public static int getCurrentScore(int i){
        return currentPlayers.get(i).currentScore;
    }

    public static void clearCurrentScore(int i){
        currentPlayers.get(i).currentScore = 0;
    }

    /**
     * getCurrentPlayed();
     * used in activatePlayer();, from the currentNumber, used to get the player's total played games, currentPlayed.
     *
     * @param i from the currentNumber.
     * @return currentPlayed
     */
    public static int getCurrentPlayed(int i) {
        int currentPlayed = (currentPlayers.get(i).totalPlayed);
        return currentPlayed;
    }

    /**
     * activatePlayer();
     * activates the player to the game, and ables the player to be saved.
     * activatePlayer(); uses the players position number from the list of currentPlayers,
     * the position number, currentNumber, is then used to get the information about the player and
     * creates a new player outside of the currentPlayers-list.
     * The newly created player, aPlayer, is then sat as the activePlayer instance variable.
     * hasPlayer(); is called to tell the program that there is an activePlayer in the game.
     */
    public static void activatePlayer() throws IOException {
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
     * loadPlayersFromFile();
     * (runs first in Game.runProgram();).
     * loads the Players form the file, "storedPlayers.txt", and adds them to a list of loadedPlayers.
     * Locates the file, storedPlayers.txt. (Throws exception if there is an error).
     * (Try the input on the file are objects, catches with an exception).
     * The players on the loadedPlayers-list is then added to the list of the currentPlayers.
     *
     * @throws IOException when file isn't found
     * @throws ClassNotFoundException when class not found.
     */

    public static ArrayList<Player> loadPlayersFromFile() throws IOException, ClassNotFoundException {


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
     * (Option 2. Load player from the menu, Menu.showMenu();).
     * sets one Player from the list of currentPlayers to the activePlayer.
     * First shows the names of the currentPlayers.
     * Prompts to enter a name.
     * If the name is in the list that the player will be loaded (sat to the activePlayer).
     * Else an error message will be shown and returns to the menu.
     */
    public static int loadPlayer() throws IOException, InterruptedException, ClassNotFoundException {
        String loadedName = "";
        System.out.println("These are the players from before: ");
        for (int i = 0; i < currentPlayers.size();i++){
            loadedName = currentPlayers.get(i).name; // Gets the name of the Player
            System.out.println(loadedName); // Prints the name
        }
        System.out.println("\nEnter the name of the player to load or a new name to create a player: ");
        text = Menu.getString();

        int i = 0;
        int j = 0;
        boolean found = false;
        boolean found2 = false;

        while (i < currentPlayers.size() && !found) {
            if (text.equals(currentPlayers.get(i).name)) {
                found = true;
            } else {
                i++;
            }
        }

        while (j < activePlayers.size() && !found2) {
                if (text.equals(activePlayers.get(j).name)) {
                    found2 = true;
                } else {
                    j++;
                }
            }

        if (found2) {
            System.out.println("Player " + text + " already exists. Enter another name.");
            loadPlayer();
        }

        if (found) {
            System.out.println( text + " found!");
            currentNumber = i;
            Player.activatePlayer();
            addPlayer();

        } else {

            System.out.println("Do you want to create Player " + text + "? (YES) or (NO):"); // New way in to createPlayer!
            String answer;
            Scanner in = new Scanner(System.in);

            if (in.hasNext()) {
                answer = in.next();
                if (answer.toUpperCase().equals("YES")) {
                    createPlayer(text);
                }
                else if (answer.toUpperCase().equals("NO")) {
                    loadPlayer();
                }
                else {
                    System.out.println("Incorrect input. But it is okay, let us pretend it is a NO. ");
                    loadPlayer();
                }
            }

        }
        return i;
    }

    /**
     * addActiveToCurrent();
     * adds the activePlayer to the list of currentPlayers.
     * Used in savePlayersToFile(); to store the activePlayer's progress in the game to the list of current players before the list is saved.
     */
    public static void addActiveToCurrent(){

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
     * (Option 3. Save player from the menu, Menu.showMenu();).
     * saves the currentPlayers to storedPlayers.txt-file.
     * First adds the activePlayer to the currentPlayers-list.
     * Locates the file, storedPlayers.txt. (Throws exception if there is an error).
     * With ObjectOutputStream writes the currentPlayers-list to the file.
     * Prints a confirmation message, then the menu is show again, Menu.showMenu();.
     * @throws IOException
     */
    public static void savePlayersToFile() throws IOException, InterruptedException, ClassNotFoundException {
        addActiveToCurrent();
        File savePlayers = new File ("storedPlayers.txt");
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(savePlayers));
        output.writeObject(currentPlayers);
        System.out.println(currentPlayers);
        output.close();
        System.out.println("Players saved.");
        Menu.showMenu();
    }

    public static void saveGame() throws IOException, InterruptedException, ClassNotFoundException {

        String activePlayersFile = "savedActivePlayers.txt"; // Names the file
        File savedActivePlayers = new File(activePlayersFile); // Creates a file
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(savedActivePlayers));
        out.writeObject( activePlayers );
        out.close(); // Important to close the file.

        String removedPlayersFile = "savedRemovedPlayers.txt"; // Names the file
        File savedRemovedPlayers = new File(removedPlayersFile); // Creates a file
        ObjectOutputStream outs = new ObjectOutputStream(new FileOutputStream(savedRemovedPlayers));
        outs.writeObject( removedPlayers );
        outs.close(); // Important to close the file.

        System.out.println("Game has been saved.");
        Menu.showMenu();
    }

    public static void loadGame() throws IOException, ClassNotFoundException {

        String activePlayersFile = "savedActivePlayers.txt";
        String removedPlayersFile = "savedRemovedPlayers.txt";
        try {
            File savedActivePlayers = new File(activePlayersFile);
            File savedRemovedPlayers = new File(removedPlayersFile);
            if (!savedActivePlayers.exists() || !savedRemovedPlayers.exists()) { // If the file do not exists then do nothing.
                System.out.println("There is no game saved.");
                Menu.showMenu();
            } else {
                resetActivePlayers(); // If there is players from a running game, those players are removed from the two arrays of players.

                ObjectInputStream input = new ObjectInputStream(new FileInputStream("savedActivePlayers.txt"));
                Object activePlayer = input.readObject();
                activePlayers = (ArrayList<Player>) activePlayer;

                ObjectInputStream in = new ObjectInputStream(new FileInputStream("savedRemovedPlayers.txt"));
                Object removedPlayer = in.readObject();
                removedPlayers = (ArrayList<Player>) removedPlayer;

                Game.startHangMan();

                }

            }catch (IOException | InterruptedException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }




    /**
     * Randomizes the players turn order.
     */
    public static void randomizeTurn(){
        Collections.shuffle(activePlayers);
        System.out.println("The turn order is: ");
        for ( int j = 0; j < activePlayers.size(); j++) {
            System.out.println( (j+1) + " " + activePlayers.get(j).name);
        }
    }

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

    public static void removePlayer(){
        System.out.print(Player.getName(Player.getPlayerNumber()));
        System.out.println(" has been removed from this game.");
        removedPlayers.add(activePlayers.get(Player.getPlayerNumber()));
        activePlayers.remove(Player.getPlayerNumber());
    }

    public static void playersLeft() throws IOException, InterruptedException, ClassNotFoundException {
        if (activePlayers.size() > 1) {
            nextPlayer();
            System.out.println("\nCONTINUE this game? SAVE this game and go back to the menu? Or to go back the MENU without saving?\n Enter (CONTINUE), (SAVE) or (MENU).");

            String answer;
            Scanner in = new Scanner(System.in);

            if (in.hasNext()) {
                answer = in.next();
                if (answer.toUpperCase().equals("CONTINUE")) {
                    Game.startHangMan();
                } else if (answer.toUpperCase().equals("SAVE")) {
                    saveGame();
                } else if (answer.toUpperCase().equals("MENU")) {
                    Menu.showMenu();
                } else {
                    System.out.println("Incorrect input. Please enter (CONTINUE), (SAVE) or (MENU): ");
                    playersLeft();
                }
            } }
        else {
                System.out.println("\n Game over!\nWould you like to play again? (YES) or (NO):");
                Game.playAgain();
            }
        }

        public static void addRemoved(){
        for (int i = 0; removedPlayers.size() > i; i++){
            activePlayers.add(removedPlayers.get(i));
        }
    }

// todo nedan är ett utkast på highscore (ca 270 rader ner)

    public static void showHighscore() throws InterruptedException, IOException, ClassNotFoundException {

        System.out.println(currentPlayers);

        System.out.println(" * * * * * * * * *");
        System.out.println(" *  HangMan 1.0  *  ");
        System.out.println(" *    HIGSCORE   *  ");
        System.out.println(" * * * * * * * * *\n");
        addActiveToCurrent();

        if (currentPlayers.isEmpty()){
            System.out.println("There is yet no players. Returns to the menu.");
        }
        else {

            getHighscoreWins();
            getHighscoreTotalScore();
            getHighscoreMaxScore();
            getHighscoreAvgScore();
            getHighscoreTotalPlayed();
            System.out.println("SAVE the highscore to a file. MENU to exit to the menu.");

            Scanner in = new Scanner(System.in);
            String answer;
            if (in.hasNext()) {
                answer = in.next();
                if (answer.toUpperCase().equals("SAVE")) {
                    saveHighscore();
                } else if (answer.toUpperCase().equals("MENU")) {
                    Menu.showMenu();
                } else {
                    System.out.println("Incorrect input. Please enter (SAVE) or (MENU): ");
                    playersLeft();
                }
            }
        }
        Menu.showMenu();
    }

    private static String getHighscoreWins(){

        Player[] statList = new Player[currentPlayers.size()];
        Player[] tempList = new Player[1];

        for (int j = 0; currentPlayers.size() > j; j++){
            statList[j] = currentPlayers.get(j);
        }

            for (int i = 1; statList.length > i; i++) {
                while (statList[i].wins > statList[i-1].wins) {

                    tempList[0] = statList[i-1];
                    statList[i-1] = statList[i];
                    statList[i] = tempList[0];
                    if ( i == 1 ){
                    }
                    else{
                        i--; // stannar kvar på platsen i
                    }
                }
            }

        String stringTotalPlayed;
        String s = "";

        System.out.println(" * * * * * * * * *");
        System.out.println(" * * * WINS  * * *");
        System.out.println(" * * * * * * * * *");
        for (int i = 0; 5 > i; i++) {
            if (statList[i].wins > 0){
                System.out.println(" " + (i + 1) + ". " + statList[i].name + " " + statList[i].wins);
                String tempS = (" " + (i + 1) + ". " + statList[i].name + " " + statList[i].wins + "\n");
                s = s + tempS;
            } // (else do nothing.)
        }
        System.out.println(" * * * * * * * * * \n");

        stringTotalPlayed  = " * * * * * * * * *\n" +" * * * WINS  * * *\n"+" * * * * * * * * *\n" + s + " * * * * * * * * * \n";
        return stringTotalPlayed;
    }

    private static String getHighscoreTotalScore() {
        Player[] statList = new Player[currentPlayers.size()];
        Player[] tempList = new Player[1];

        for (int j = 0; currentPlayers.size() > j; j++){
            statList[j] = currentPlayers.get(j);
        }

        for (int i = 1; statList.length > i; i++) {
            while (statList[i].totalScore > statList[i-1].totalScore) {

                tempList[0] = statList[i-1]; // Skriver Player från platsen innan till temp
                statList[i-1] = statList[i]; // Skriver spelaren till platsen innan
                statList[i] = tempList[0]; // återför Player till spelarens plats
                if ( i == 1 ){
                }
                else{
                i--; // stannar kvar på platsen i
                }

            }
        }

        String stringTotalPlayed;
        String s = "";

        System.out.println(" * * * * * * * * *");
        System.out.println(" * *TOTAL SCORE* *");
        System.out.println(" * * * * * * * * *");
        for (int i = 0; 5 > i; i++) {
            if (statList[i].totalScore > 0) {
                System.out.println(" " + (i + 1) + ". " + statList[i].name + " " + statList[i].totalScore);
                String tempS = (" " + (i + 1) + ". " + statList[i].name + " " + statList[i].totalScore + "\n");
                s = s + tempS;
            } // (else do nothing.)
        }
        System.out.println(" * * * * * * * * * \n");

        stringTotalPlayed  = " * * * * * * * * *\n" +" * *TOTAL SCORE* *\n"+" * * * * * * * * *\n" + s + " * * * * * * * * * \n";
        return stringTotalPlayed;
    }

    private static String getHighscoreMaxScore() {
        Player[] statList = new Player[currentPlayers.size()];
        Player[] tempList = new Player[1];

        for (int j = 0; currentPlayers.size() > j; j++){
            statList[j] = currentPlayers.get(j);
        }

        for (int i = 1; statList.length > i; i++) {
            while (statList[i].maxScore > statList[i-1].maxScore) {

                tempList[0] = statList[i-1];
                statList[i-1] = statList[i];
                statList[i] = tempList[0];
                if ( i == 1 ){
                }
                else{
                    i--; // stannar kvar på platsen i
                }
            }
        }

        String stringTotalPlayed;
        String s = "";

        System.out.println(" * * * * * * * * *");
        System.out.println(" * * MAX SCORE * *");
        System.out.println(" * * * * * * * * *");
        for (int i = 0; 5 > i; i++) {
            if (statList[i].maxScore > 0) {
                System.out.println(" " + (i + 1) + ". " + statList[i].name + " " + statList[i].maxScore);
                String tempS = (" " + (i + 1) + ". " + statList[i].name + " " + statList[i].maxScore + "\n");
                s = s + tempS;
            } // (else do nothing.)
        }
        System.out.println(" * * * * * * * * * \n");

        stringTotalPlayed  = " * * * * * * * * *\n" +" * * MAX SCORE * *\n"+" * * * * * * * * *\n" + s + " * * * * * * * * * \n";
        return stringTotalPlayed;
    }

    private static String getHighscoreAvgScore() {
        Player[] statList = new Player[currentPlayers.size()];
        Player[] tempList = new Player[1];

        for (int j = 0; currentPlayers.size() > j; j++){
            statList[j] = currentPlayers.get(j);
        }

        for (int i = 1; statList.length > i; i++) {
            while (statList[i].avgScore > statList[i-1].avgScore) {

                tempList[0] = statList[i-1];
                statList[i-1] = statList[i];
                statList[i] = tempList[0];
                if ( i == 1 ){
                }
                else{
                    i--; // stannar kvar på platsen i
                }
            }
        }

        String stringTotalPlayed;
        String s = "";

        System.out.println(" * * * * * * * * *");
        System.out.println(" * AVERAGE SCORE *");
        System.out.println(" * * * * * * * * *");
        for (int i = 0; 5 > i; i++) {
            if (statList[i].avgScore > 0) {
                System.out.println(" " + (i + 1) + ". " + statList[i].name + " " + statList[i].avgScore);
                String tempS = (" " + (i + 1) + ". " + statList[i].name + " " + statList[i].avgScore + "\n");
                s = s + tempS;
            } // (else do nothing.)
        }
        System.out.println(" * * * * * * * * * \n");

        stringTotalPlayed  = " * * * * * * * * *\n" +" * AVERAGE SCORE *\n"+" * * * * * * * * *\n" + s + " * * * * * * * * * \n";
        return stringTotalPlayed;
    }

    private static String getHighscoreTotalPlayed() {
        Player[] statList = new Player[currentPlayers.size()];
        Player[] tempList = new Player[1];

        for (int j = 0; currentPlayers.size() > j; j++){
            statList[j] = currentPlayers.get(j);
        }

        for (int i = 1; statList.length > i; i++) {
            while (statList[i].totalPlayed > statList[i-1].totalPlayed) {

                tempList[0] = statList[i-1];
                statList[i-1] = statList[i];
                statList[i] = tempList[0];
                if ( i == 1 ){
                }
                else{
                    i--; // stannar kvar på platsen i
                }
            }
        }

        String stringTotalPlayed;
        String s = "";

        System.out.println(" * * * * * * * * *");
        System.out.println(" *  TOTAL PLAYED *");
        System.out.println(" * * * * * * * * *");
        for (int i = 0; 5 > i; i++) {
            if (statList[i].totalPlayed > 0) {
                System.out.println(" " + (i + 1) + ". " + statList[i].name + " " + statList[i].totalPlayed);
                String tempS = (" " + (i + 1) + ". " + statList[i].name + " " + statList[i].totalPlayed + "\n");
                s = s + tempS;
            } // (else do nothing.)
        }
        System.out.println(" * * * * * * * * * \n");

        stringTotalPlayed  = " * * * * * * * * *\n" +" *  TOTAL PLAYED *\n"+" * * * * * * * * *\n" + s + " * * * * * * * * * \n";
        return stringTotalPlayed;
    }

    public static void saveHighscore() throws InterruptedException, IOException, ClassNotFoundException {

        String highscoreFile = "highscore.txt"; // Names the file
        File highscore = new File(highscoreFile); // Creates a file

        PrintWriter out = new PrintWriter(highscore);

        out.println(" * * * * * * * * *");
        out.println(" *  HangMan 1.0  *  ");
        out.println(" *    HIGSCORE   *  ");
        out.println(" * * * * * * * * *\n");

        out.println(getHighscoreWins());
        out.println(getHighscoreTotalScore());
        out.println(getHighscoreMaxScore());
        out.println(getHighscoreAvgScore());
        out.println(getHighscoreTotalPlayed());

        out.close(); // Important to close the file.

        System.out.println("Highscore saved to file \"highscore.txt\" in the game folder.");
        System.out.println("Remember to rename or replace the file if you like to keep it.");
        Menu.showMenu();
    }

// todo ovan är ett utkast till highscore (ca 270 rader upp)

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
