
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MenuFrame extends JFrame{

    private JPanel menuPanel;
    private JLabel menuLabel;
    private JButton menuButton1;
    private JButton menuButton2;
    private JButton menuButton3;
    private JButton menuButton4;

    /**
     * Menu for the options at the start of the program.
     * Contains 4 buttons.
     *
     * 1. Play - Player.resetPlayers(); new PlayerFrame();
     * 2. Load Game - Player.loadGame();
     * 3. Save Players - Player.savePlayersToFile();
     * 4. Highscore - Player.highScore();
     *
     * Exit the program on [X] in upper right corner.
     */
    public MenuFrame(){
        this.setSize(600,600);
        this.setTitle("MenuFrame");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setMenuPanel();
        this.add(menuPanel);
        this.setVisible(true);

        Player.addRemoved();
        Player.removeComputerPlayer();
    }

    private void notVisibleMenuFrame(){
        this.setVisible(false);
    }

    private void doVisibleMenuFrame(){
        this.setVisible(true);
    }

    private void setMenuPanel(){
        menuPanel = new JPanel(new GridLayout(7,7));

        menuLabel = new JLabel("Menu");
        menuPanel.add(menuLabel);

        menuButton1 = new JButton("1. Play");
        Button1Listener b1l = new Button1Listener();
        menuButton1.addActionListener(b1l);
        menuPanel.add(menuButton1);

        menuButton2 = new JButton ("2. Load Game");
        Button2Listener b2l = new Button2Listener();
        menuButton2.addActionListener(b2l);
        menuPanel.add(menuButton2);

        menuButton3 = new JButton ("3. Save Players");
        Button3Listener b3l = new Button3Listener();
        menuButton3.addActionListener(b3l);
        menuPanel.add(menuButton3);

        menuButton4 = new JButton ("4. Highscore");
        Button4Listener b4l = new Button4Listener();
        menuButton4.addActionListener(b4l);
        menuPanel.add(menuButton4);

        doVisibleMenuFrame();

    }

    private class Button1Listener implements ActionListener  {

        @Override
        public void actionPerformed(ActionEvent event) {
            notVisibleMenuFrame();
            Player.resetPlayers();

                new PlayerFrame();

        }
    }

    private class Button2Listener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                notVisibleMenuFrame();
                try {
                    Player.loadGame();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

    private class Button3Listener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent event) {
                notVisibleMenuFrame();
                try {
                    Player.savePlayersToFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    private class Button4Listener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent event) {
            notVisibleMenuFrame();
            try {
                Player.highScore();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

