import java.io.*;
import java.util.ArrayList;

public class Player implements Serializable {
    private static final long serialVersionUID = 1337;

    private String name;
    private int wins;
    private int totalPlayed;

    private static Player activePlayer = new Player("",0,0);

    private static ArrayList<Player> currentPlayers = new ArrayList<Player>();


    public static int nrIntActualPlayer;
    public static int livingPlayer = 0;


    public Player (String name, int wins, int totalPlayed) {

        this.name = name;
        this.wins = wins;
        this.totalPlayed = totalPlayed;

    }


    public static String getName(){return activePlayer.name;}
    public int getWins(){return this.wins;}
    public int getPlayed(){ return this.totalPlayed;}

   // public Player getActualPlayer(){this.actualPlayer; } // todo Här är vi i koden !!!  ! !! !

    public static ArrayList<Player> getCurrentPlayers() {
        return currentPlayers;
    }

    public static void addWins(){ activePlayer.wins = activePlayer.wins + 1;
        System.out.println(activePlayer);}

    public static void addPlayed(){ activePlayer.totalPlayed = activePlayer.totalPlayed + 1;
        System.out.println(activePlayer);}


    public int livingPlayer(){
        livingPlayer = 1;
        return livingPlayer;
    }





    public static void checkPlayer() throws IOException {
        if ( livingPlayer > 0){
            Game.startHangMan();


            //  System.out.println("HÄR SKA SPELET VISAS SER DU DETTA SÅ FUNKAR DET!"); // todo add Game.showGame(); VARFÖR INTE STATIC??
            // Main.runGame();
        }
        else{
            Player.createPlayer();
        }


    }


    /**
     * Method to create a player, the player may enter a name,
     * then the player will be added to the array of players.
     * The basic info will be shown for the player.
     */
    public static void createPlayer() throws IOException {

        System.out.println("Enter a player name: ");


        String newName = Menu.getString();
        System.out.println(newName);

        int i = 0;
        boolean found = false;
        while (i < currentPlayers.size() && !found) {
            if (newName.equals(currentPlayers.get(i).name)) {
                found = true;
            } else {
                i++;
            }
        }
        if (found) {
            System.out.println(newName);
            System.out.println("Player " + newName + " found at position: " + i);
            System.out.println(currentPlayers.get(i));

            System.out.println("Name already exists.");
            createPlayer();

        } else {
            Player newPlayer = new Player(newName, 0, 0);

            System.out.println("Player name, Games played, Games won: ");
            System.out.println(newPlayer);

            // todo add the player to the player array currentPlayers
            currentPlayers.add(newPlayer);

            // todo add the player to activatePlayer
            nrIntActualPlayer = currentPlayers.size()-1;
            Player.activatePlayer();

            // todo add a method to run the game.
            checkPlayer();
        }
    }


    public static void savePlayersToFile(ArrayList<Player> arr ) throws IOException {
        addActivePlayerToCurrentPlayerList();
        File savePlayers = new File ("storedPlayers.txt");
        ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(savePlayers));

        output.writeObject(currentPlayers);
        System.out.println(currentPlayers);
        output.close();
        System.out.println("Players saved.");

        Menu.showMenu();
    }

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



    // loadPlayer
    public static int loadPlayer() throws IOException {

        String loadedName = "";

        System.out.println("These are the players from before: ");
        for (int i = 0; i < currentPlayers.size();i++){
            loadedName = currentPlayers.get(i).name; // Gets the name of the Player
            System.out.println(loadedName); // Prints the name
        }

        System.out.println("Enter the name of the player to load.");

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
            System.out.println(text);
            System.out.println("Player " + text + " found at position: " + i);
            System.out.println(currentPlayers.get(i));
            nrIntActualPlayer = i;
            Player.activatePlayer();
            Player.checkPlayer();

        } else {
            System.out.println("Player " + text + " not found. Back to menu.");

            try {
                Menu.showMenu();
            }
            catch(IOException e) {
                e.printStackTrace();
            }

        }
        return i;
    }


    public static String getActualName(int i) {
        String actName = (currentPlayers.get(i).name);
        return actName;
    }

    public static int getActualWins(int i) {
        int actWins = (currentPlayers.get(i).wins);
        return actWins;
    }

    public static int getActualPlayed(int i) {
        int actPlayed = (currentPlayers.get(i).totalPlayed);
        return actPlayed;
    }


    public static void activatePlayer() {
        int i = nrIntActualPlayer;
        Player aPlayer = new Player(getActualName(i), getActualWins(i), getActualPlayed(i));
        System.out.println("Player nr: "+ i);
        System.out.println(aPlayer);

        aPlayer.livingPlayer(); // counter to check if there is a player active.
        activePlayer=aPlayer;
    }


    public static void addActivePlayerToCurrentPlayerList(){
        int i = nrIntActualPlayer;
        currentPlayers.set(i,activePlayer);
    }





    @Override
    public String toString(){
        return "Name: " + name + ", " +
                "Wins: " + wins + ", " +
                "Games played: " + totalPlayed + "\n";

    }
}
