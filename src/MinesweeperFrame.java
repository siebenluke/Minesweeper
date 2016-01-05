/**
 * A basic main to create a jframe to holds a minesweeperjpanel.
 *
 * @author Luke Sieben
 * @version 2012/09/19
 */

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MinesweeperFrame {
    private static Color backgroundColor = new Color(30, 30, 30), foregroundColor = new Color(255, 255, 255),
            highlightColor = new Color(51, 143, 255);

    /**
     * The main method that always runs first in any java program.
     * This one schedules a job for the event-dispatching thread to create and show this application's GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Sets up a basic jframe to hold a minesweeperjpanl.
     */
    public static void createAndShowGUI() {
        UIManager.put("control", backgroundColor); // empty (aka background of JPanels) component color
        UIManager.put("info", backgroundColor); // tooltip background color

        UIManager.put("nimbusBase", backgroundColor); // color of radio buttons dots/first item of combo box background

        UIManager.put("nimbusFocus", highlightColor); // color around focused component

        UIManager.put("nimbusLightBackground", backgroundColor); // text area background

        UIManager.put("nimbusOrange", backgroundColor); // progress bar background color

        UIManager.put("nimbusSelectedText", backgroundColor); // selected text color
        UIManager.put("nimbusSelectionBackground", highlightColor); // selected text's background color
        UIManager.put("text", foregroundColor); // text color

        UIManager.put("nimbusBlueGrey", backgroundColor); // general background color of components

        // try setting up Nimbus Look and Feel
        try {
            for(UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());

                    break;
                }
            }
        }
        catch(Exception e) {}

        JFrame frame = new JFrame("Minesweeper 2015-06-12");
        int rows = 9;
        int columns = 9;
        int minePercentage = 10;
        frame.add(new MinesweeperPanel(rows, columns, minePercentage));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // add frame icon
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL url = MinesweeperFrame.class.getResource("Minesweeper.png");
        if(url != null) {
            Image image = toolkit.getImage(url);
            if(image != null) {
                frame.setIconImage(image);
            }
        }

        int width = 555;
        int height = 555;
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
