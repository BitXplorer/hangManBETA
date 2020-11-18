
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class PlayerFrame extends JFrame {

    private JPanel playerPanel;
    private JLabel playerLabel;
    private JTextArea playerTextArea;
    private static JTextField playerTextField;

    /**
     * Frame used to enter a player name, either to load the player or to create the player if there is no player with that name.
     */
    public PlayerFrame() {
        this.setSize(600, 600);
        this.setTitle("PlayerFrame");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setPlayerPanel();
        this.add(playerPanel);
        doVisiblePlayerPanel();
    }

    private void notVisiblePlayerPanel() {
        this.setVisible(false);
    }

    private void doVisiblePlayerPanel() {
        this.setVisible(true);
    }

    private void setPlayerPanel() {
        playerPanel = new JPanel((new GridLayout(3, 0)));

        playerLabel = new JLabel("Enter a name of the player you would like to load, or enter a new name to create a player.");
        playerPanel.add(playerLabel);

        playerTextArea = new JTextArea(Player.getCurrentPlayersName());
        playerTextArea.setEditable(false);
        playerPanel.add(playerTextArea);

        playerTextField = new JTextField("Name", 30);
        TextListener tl = new TextListener();
        playerTextField.addActionListener(tl);
        playerPanel.add(playerTextField);
    }

    public static String setPlayerTextField(){
    String name = playerTextField.getText();
        return name;
    }

    private class TextListener implements ActionListener {
        @Override
        public void actionPerformed (ActionEvent event) {
            notVisiblePlayerPanel();

            if (playerTextField.getText().contains("Computer")){
                JOptionPane.showMessageDialog(null, playerTextField.getText() + " is not a valid name, choose another name.",
                        playerTextField.getText(),
                        JOptionPane.INFORMATION_MESSAGE);
                new PlayerFrame();
        }
            else if (Player.getActivePlayersName().contains(playerTextField.getText())){
                    JOptionPane.showMessageDialog(null, playerTextField.getText() + " is already chosen, choose another player.",
                            playerTextField.getText() + " is already in the game",
                            JOptionPane.INFORMATION_MESSAGE);
                new ModifyGameFrame();
            }
            else{
                try {
                    Player.loadPlayer();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
            }
        }
    }

}

}
