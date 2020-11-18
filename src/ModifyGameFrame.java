
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ModifyGameFrame extends JFrame{

    private JPanel modifyGamePanel;
    private JTextArea activePlayersTextArea;
    private JButton addPlayerButton;
    private JButton addComputerButton;
    private JButton startButton;
    private JButton menuButton;

    /**
     * ModifyGameFrame (aka. GameOptions).
     *
     * "Add Player" - new PlayerFrame();
     * "Add Computer Player" -  Player.createComputerPlayer();
     * "Start Game" - Game.startHangMan();
     * "Menu" - new MenuFrame();
     */
    public ModifyGameFrame(){
        this.setSize(600, 600);
        this.setTitle("ModifyGameFrame");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setModifyGamePanel();
        this.add(modifyGamePanel);
        doVisible();
    }

    private void notVisible() {
        this.setVisible(false);
    }

    private void doVisible() {
        this.setVisible(true);
    }

    private void setModifyGamePanel(){
        modifyGamePanel = new JPanel((new GridLayout(5,0)));

        activePlayersTextArea = new JTextArea(Player.getActivePlayersName());
        activePlayersTextArea.setEditable(false);
        modifyGamePanel.add(activePlayersTextArea);

        addPlayerButton = new JButton("Add Player");
        AddPlayerButtonListener bpl = new AddPlayerButtonListener();
        addPlayerButton.addActionListener(bpl);
        modifyGamePanel.add(addPlayerButton);

        addComputerButton = new JButton("Add Computer Player");
        AddComputerButtonListener bcl = new AddComputerButtonListener();
        addComputerButton.addActionListener(bcl);
        modifyGamePanel.add(addComputerButton);

        startButton = new JButton("Start Game");
        StartButtonListener bgl = new StartButtonListener();
        startButton.addActionListener(bgl);
        modifyGamePanel.add(startButton);

        menuButton = new JButton("Menu");
        MenuButtonListener bml = new MenuButtonListener();
        menuButton.addActionListener(bml);
        modifyGamePanel.add(menuButton);
    }

    private class AddPlayerButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            notVisible();
            new PlayerFrame();
        }
    }

    private class AddComputerButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            notVisible();
            try {
                Player.createComputerPlayer();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
           notVisible();

            try {
                Game.startHangMan();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private class MenuButtonListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            notVisible();
            new MenuFrame();
        }
    }

}