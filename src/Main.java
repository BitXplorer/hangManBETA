import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException,ClassNotFoundException {



        Player.loadPlayersFromFile();
        System.out.println(Player.getCurrentPlayers());
        Menu.showMenu(); // == Game.runProgram(); todo "typ"




/*

   ArrayList<Player> cP = new ArrayList<Player>();

      //  cP = Player.loadPlayersFromFile();


        Player p1 = new Player("Dan",5,10);
        Player p2 = new Player("Antene",30,40);
        Player p3 = new Player("Joel",3,5);

        cP.add(p1);
        cP.add(p2);
        cP.add(p3);

        System.out.print(cP.toString());

      Player.savePlayersToFile(cP);
*/

     //   Player.createPlayer();





    }

/* // todo här är vi och arbetar
    public static void activePlayer() {
        int i = Player.nrIntActualPlayer;
        Player actualPlayer = new Player(Player.getActualName(i), Player.getActualWins(i), Player.getActualPlayed(i));
        System.out.println("Player nr: "+ i);
        System.out.println(actualPlayer);

        actualPlayer.livingPlayer(); // counter to check if there is a player active.
         }

 */
/*
        System.out.println(actualPlayer.getName());
        System.out.println(actualPlayer.getWins());
        System.out.println(actualPlayer.getPlayed());
      */

      /*
       actualPlayer.addWins();
         actualPlayer.addPlayed();
        actualPlayer.addPlayed();
       System.out.println(actualPlayer.getWins());
        System.out.println(actualPlayer.getPlayed());
*/



/*
    public static void runGame() {
           Game game1 = new Game();
        }
*/

}
