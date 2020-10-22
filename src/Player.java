import java.io.*;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 1337; // Used to ensure the header of the storedPlayers.txt-files is correct. Have experienced crashes without it.

    /*
     * Instance variables
     * name: the players name.
     * wins: the number of games won.
     * totalPlayed: the number of total games played.
     *
     * currentNumber: the player's position number from the list of current players, currentPlayers.
     * hasPlayer: is used to check if any player is selected, to play with or to save.
     *
     * currentPlayers: is a list with the all players, loaded and created.
     * activePlayer: is the player currently used in the game.
     */
    private String name;
    private int wins;
    private int totalPlayed;

    private static int currentNumber;
    private static boolean hasPlayer = false;

    private static ArrayList<Player> currentPlayers = new ArrayList<>();

    private static Player activePlayer = new Player("",0,0);

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

    /**
     * getName();
     * used in Game.showGame(); to print the name of the, in the game, active player.
     * @return activePlayer.name
     */
    public static String getName(){
        return activePlayer.name;
    }

    /**
     * addWins();
     * adds one victory point to the players victory points when the player wins a game.
     */
    public static void addWins(){
        activePlayer.wins = activePlayer.wins + 1;
    }

    /**
     * addPlayed();
     * adds one to the players played game counts after a game is played.
     * Shows the players activePlayer's status (current game won and games played).
     */
    public static void addPlayed(){
        activePlayer.totalPlayed = activePlayer.totalPlayed + 1;
        System.out.println(activePlayer);
    }

    /**
     * checkPlayer();
     * (Option 1. Play from the menu, Menu.showMenu();).
     * checks if there is yet an activePlayer or not,
     * if there is an activePlayer the game will start.
     * else createPlayer(); will be called.
     * @throws IOException when file is not found.
     */
    public static void checkPlayer() throws IOException {
        if ( hasPlayer ){
            Game.startHangMan();
        }
        else {
            Player.createPlayer();
        }
    }

    /**
     * hasPlayer();
     * switches the instance variable hasPlayer from false to true, hasPlayer is used in checkPlayer();.
     */
    public static void hasPlayer() {
        hasPlayer = true;
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
    public static void createPlayer() throws IOException {
        int i = 0;
        boolean found = false;

        System.out.println("Enter a player name: ");
        String newName = Menu.getString();

        while (i < currentPlayers.size() && !found) {
            if (newName.equals(currentPlayers.get(i).name)) {
                found = true;
            }
            else {
                i++;
            }
        }
        if (found) {
            System.out.println("Player " + newName + " found at position: " + i);
            System.out.println(currentPlayers.get(i));
            System.out.println("Name already exists.");
            createPlayer();
        }
        else {
            Player newPlayer = new Player(newName, 0, 0);
            System.out.println("Player name, Games played, Games won: ");
            System.out.println(newPlayer);
            currentPlayers.add(newPlayer);
            currentNumber = currentPlayers.size()-1;
            activatePlayer();
            checkPlayer();
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
    public static void activatePlayer() {
        int i = currentNumber;
        Player aPlayer = new Player(getCurrentName(i), getCurrentWins(i), getCurrentPlayed(i));
        System.out.println("Player nr: "+ i);
        System.out.println(aPlayer);
        activePlayer=aPlayer;
        hasPlayer();
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
    public static int loadPlayer() throws IOException {
        String loadedName = "";
        System.out.println("These are the players from before: ");
        for (int i = 0; i < currentPlayers.size();i++){
            loadedName = currentPlayers.get(i).name; // Gets the name of the Player
            System.out.println(loadedName); // Prints the name
        }
        System.out.println("\nEnter the name of the player to load: ");
        String text = Menu.getString();

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
            System.out.println("Player " + text + " found at position: " + i);
            System.out.println(currentPlayers.get(i));
            currentNumber = i;
            Player.activatePlayer();
            Player.checkPlayer();

        } else {
            System.out.println("Player " + text + " not found. Back to menu.");
            Menu.showMenu();
        }
        return i;
    }

    /**
     * addActiveToCurrent();
     * adds the activePlayer to the list of currentPlayers.
     * Used in savePlayersToFile(); to store the activePlayer's progress in the game to the list of current players before the list is saved.
     */
    public static void addActiveToCurrent(){
        currentPlayers.set(currentNumber,activePlayer);
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
     * toString();
     * Overrides the Player's information to string.
     * @return a string with the Player's name, won games and total played gamed (then ends the line).
     */
    @Override
    public String toString(){
        return "Name: " + name + ", Wins: " + wins + ", Games played: " + totalPlayed + ".\n";
    }
}