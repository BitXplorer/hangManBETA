
/**
 * Newton
 *
 * HangMan 1.0
 *
 * Kurs:
 * Objektorienterad programmering 1
 * Klass: Systemutvecklare Java 2 - Stockholm (SYSJS 2)
 * Termin och år: HT 2020
 * Författare:  Joel Reboia, Joakim Nilsson,
 * Dan Danielsson, Anteneh Tsegaye
 * Lärare: Ola Lindquist
 */

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        Player.loadPlayersFromFile();
        new MenuFrame();
    }

}