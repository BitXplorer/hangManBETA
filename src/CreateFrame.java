
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class CreateFrame extends JFrame {

    private JPanel createPlayerPanel;
    private JLabel createPlayerLabel;
    private JButton createYesButton;
    private JButton createNoButton;

    /**
     *  CreateFrame (is a confirmation-frame)
     * "Create named player?"
     * "Yes" - Player.createPlayer(PlayerFrame.setPlayerTextField());
     * "No" - new PlayerFrame();
     */
    public CreateFrame() {
        this.setSize(600, 200);
        this.setTitle("CreateFrame");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        setCreatePlayerPanel();
        this.add(createPlayerPanel);
        doVisible();
    }

    private void notVisible() {
        this.setVisible(false);
    }

    private void doVisible() {
        this.setVisible(true);
    }

    private void setCreatePlayerPanel(){
        createPlayerPanel = new JPanel((new GridLayout(0,3)));

        createPlayerLabel = new JLabel("Create named player?");
        createPlayerPanel.add(createPlayerLabel);

        createYesButton = new JButton("Yes");
        ButtonYesListener byl = new ButtonYesListener();
        createYesButton.addActionListener(byl);
        createPlayerPanel.add(createYesButton);

        createNoButton = new JButton("No");
        ButtonNoListener bnl = new ButtonNoListener();
        createNoButton.addActionListener(bnl);
        createPlayerPanel.add(createNoButton);
    }

    private class ButtonYesListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            notVisible();

            try {
                Player.createPlayer(PlayerFrame.setPlayerTextField());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }

    private class ButtonNoListener implements  ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            notVisible();
            new PlayerFrame();
        }
    }

}