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

    private static int currentNumber;
    private static int hasPlayerNumber;
    private static int playerNumber;

    private static String text; // Used for the player's name

    private static ArrayList<Player> currentPlayers = new ArrayList<>();

    private static ArrayList<Player> activePlayers = new ArrayList();

    /**
     * Constructor
     * Object Player.
     * @param name The players name.
     * @param wins The number of games won.
     * @param totalPlayed The number of total games played.
     */
    public Player (String name, int wins, int totalPlayed) {
        this.name = name;
        this.wins = wins;
        this.totalPlayed = totalPlayed;
    }

    public static int getPlayerNumber(){
        return playerNumber;
    }

    public static void nextPlayer(){
        playerNumber = playerNumber + 1;
        if (playerNumber >= activePlayers.size() ){
            playerNumber = 0;
        }
    }

    /**
     * getName();
     * used in Game.showGame(); to print the name of the, in the game, active player.
     * @return activePlayer.name
     */
   public static String getName(int i){
        return activePlayers.get(i).name;
    }

    /**
     * addWins();
     * adds one victory point to the players victory points when the player wins a game.
     */
   public static void addWins(int i){
       activePlayers.get(i).wins = activePlayers.get(i).wins + 1;
    }

    /**
     * addPlayed();
     * adds one to the players played game counts after a game is played.
     * Shows the players activePlayer's status (current game won and games played).
     */
   public static void addPlayed(int j){
       for ( j = 0; j < activePlayers.size(); j++){
           activePlayers.get(j).totalPlayed = activePlayers.get(j).totalPlayed + 1;
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
    public static void startGame() throws IOException {
        if (hasPlayerNumber == 4 ) {
            System.out.println( Player.activePlayers); // todo listan här lär inte behöva skrivas ut
            Game.startHangMan();
        }
        else {
        System.out.println("Do you want to start the game?  (YES) or (NO)?  (MENU) to exit to the menu:");
        String answer;
        Scanner in = new Scanner(System.in);
        if (in.hasNext()) {
            answer = in.next();
            if (answer.toUpperCase().equals("YES")) {
                System.out.println( Player.activePlayers); // todo listan här lär inte behöva skrivas ut
                Game.startHangMan();
            }
            else if (answer.toUpperCase().equals("NO")) {
                addPlayer();
            }
            else if (answer.toUpperCase().equals("MENU")) {
                hasPlayerNumber = 0;
                Menu.showMenu();
            }
            else {
                System.out.println("Incorrect input. Please try again. ");
                startGame();
            }
        }
    }
    }

    public static void resetActivePlayers(){ //Used to restart the activePlayers-list.
        hasPlayerNumber = 0;
        activePlayers.clear();
    }

    public static void addPlayer() throws IOException {
        if ( hasPlayerNumber == 0){
            loadPlayer();
        }
        if ( hasPlayerNumber != 0 && hasPlayerNumber < 4) {
            System.out.println("Do you want to add another Player to this game?  (YES) or (NO)?  (MENU) to exit to the menu:");
            String answer;
            Scanner in = new Scanner(System.in);

            if (in.hasNext()) {
                answer = in.next();
                if (answer.toUpperCase().equals("YES")) {
                    loadPlayer();
                }
                else if (answer.toUpperCase().equals("NO")) {
                    startGame();
                }
                else if (answer.toUpperCase().equals("MENU")) {
                    hasPlayerNumber = 0;
                    Menu.showMenu();
                }
                else {
                    System.out.println("Incorrect input. Please try again. ");
                    addPlayer();
                }
            }
        }
        if (hasPlayerNumber == 4){
            startGame();
        }
    }

    /**
     * createPlayer();
     * called in the Menu.showMenu();, option 1, to create a player.
     * The player is prompted to enter a name.
     * (If the name is already in the list of currentPlayers the player will be shown an error message and gets a new try to enter a name).
     * Once a valid name is entered the player will be created and added to the array list of players, currentPlayers.
     * The basic info about the player will be shown.
     *
     * The players position number in the currentPlayers-list will be added to the currentNumber instance variable.
     * activatePlayer(); is called and the player will be activated as the activePlayer from that method.
     * checkPlayer(); is called to confirm that the player is added as the activePlayer.
     * From the checkPlayer(); the game then starts.
     *
     */
    public static void createPlayer(String newName) throws IOException {
        int i = 0;
        boolean found = false;

        while (i < currentPlayers.size() && !found) {
            if (newName.equals(currentPlayers.get(i).name)) {
                found = true;
            }
            else {
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
        }
        else {
            Player newPlayer = new Player(newName, 0, 0);
            currentPlayers.add(newPlayer);
            currentNumber = currentPlayers.size()-1;
            activatePlayer();
            addPlayer();
        }
    }

    /**
     * getCurrentName();
     * used in activatePlayer();, from the currentNumber, used to get the name of the player, currentName.
     * @param i  from the currentNumber.
     * @return currentName
     */
    public static String getCurrentName(int i) {
        String currentName = (currentPlayers.get(i).name);
        return currentName;
    }

    /**
     * getCurrentWins();
     * used in activatePlayer();, from the currentNumber, used to get the player's won games, currentWins.
     * @param i  from the currentNumber.
     * @return currentWins
     */
    public static int getCurrentWins(int i) {
        int currentWins = (currentPlayers.get(i).wins);
        return currentWins;
    }

    /**
     * getCurrentPlayed();
     * used in activatePlayer();, from the currentNumber, used to get the player's total played games, currentPlayed.
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
        Player aPlayer = new Player(getCurrentName(i), getCurrentWins(i), getCurrentPlayed(i));

        if (hasPlayerNumber == 3){
            activePlayers.add(3,aPlayer);
            hasPlayerNumber = 4;
        }
        else if (hasPlayerNumber == 2){
            activePlayers.add(2,aPlayer);
            hasPlayerNumber = 3;
        }
        else if (hasPlayerNumber == 1){
            activePlayers.add(1,aPlayer);
            hasPlayerNumber = 2;
        }
        else if (hasPlayerNumber == 0){
            activePlayers.add(0,aPlayer);
            hasPlayerNumber = 1; // Changes the number to the next player/list-position
        }
        else {
            hasPlayerNumber = 0;
            activePlayers.add(0,aPlayer);
            hasPlayerNumber = 1;
        }
        System.out.println(aPlayer);
        System.out.println("Player nr: "+ hasPlayerNumber);
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
        ObjectInputStream input = new ObjectInputStream(new FileInputStream("storedPlayers.txt"));
        ArrayList<Player> loadedPlayers = new ArrayList<>();
        try (input){
            Object player = input.readObject();
            loadedPlayers = (ArrayList<Player>) player;
        } catch (IOException  e) {
            e.printStackTrace();
        }
        currentPlayers.addAll(loadedPlayers);
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
    public static int loadPlayer() throws IOException { // todo do we have to return i ???
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
        for (int i = 0; i < currentPlayers.size(); i++){
            currentPlayers.set(currentNumber,currentPlayers.get(i));
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
    public static void savePlayersToFile() throws IOException {
        addActiveToCurrent();
        File savePlayers = new File ("storedPlayers.txt");
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(savePlayers));
        output.writeObject(currentPlayers);
        System.out.println(currentPlayers);
        output.close();
        System.out.println("Players saved.");
        Menu.showMenu();
    }

    /**
     * Randomizes the players turn order.
     */
    public static void randomizeTurn(){ // todo Ska denna ligga här eller i game?! Något mer som behövs för den?
        Collections.shuffle(activePlayers);
    }

    /**
     * toString();
     * Overrides the Player's information to string.
     * @return a string with the Player's name, won games and total played gamed (then ends the line).
     */
    @Override
    public String toString(){
        return "[Name: " + name + ", Wins: " + wins + ", Games played: " + totalPlayed + "]";
    }
}