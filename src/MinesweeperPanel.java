/**
 * A basic jpanel that holds a game of minesweeper.
 * For example:
 * <pre>
 *     JFrame yourFrame = new JFrame("yourTitle");
 *     yourFrame.add(new MinesweeperJPanel(9, 9));
 *     yourFrame.setVisible(true);
 * </pre>
 *
 * @author Luke Sieben
 * @version 2012/09/19
 */

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class MinesweeperPanel extends JPanel {
    private boolean isFirstTurn;
    private MouseAdapter boardListener;
    private JLabel board[][];
    private Minesweeper game;

    /**
     * Creates a minesweeper board of size rows by columns.
     * NOTE #1: Spacebar is used to randomize the game board.
     * NOTE #2: An infinite loop will be caused if row * columns is less than 12.
     *          This is because of this panel's special feature which ensures that the first move a player takes will always uncover an empty square.
     */
    public MinesweeperPanel(int rows, int columns, int minePercentage) {
        boardListener = new BoardListener();

        setLayout(new BorderLayout());

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if(keyCode == KeyEvent.VK_SPACE) {
                    randomizeBoard();
                }
                else if(keyCode == KeyEvent.VK_1 || keyCode == KeyEvent.VK_NUMPAD1) {
                    decreaseRows();
                }
                else if(keyCode == KeyEvent.VK_2 || keyCode == KeyEvent.VK_NUMPAD2) {
                    increaseRows();
                }
                else if(keyCode == KeyEvent.VK_3 || keyCode == KeyEvent.VK_NUMPAD3) {
                    decreaseColumns();
                }
                else if(keyCode == KeyEvent.VK_4 || keyCode == KeyEvent.VK_NUMPAD4) {
                    increaseColumns();
                }
                else if(keyCode == KeyEvent.VK_5 || keyCode == KeyEvent.VK_NUMPAD5) {
                    decreaseMinePercentage();
                }
                else if(keyCode == KeyEvent.VK_6 || keyCode == KeyEvent.VK_NUMPAD6) {
                    increaseMinePercentage();
                }

                repaint();
            }
        });
        setFocusable(true);

        initializeBoard(rows, columns, minePercentage);
    }

    /**
     * Decreases the amount of rows by 1.
     */
    private void decreaseRows() {
        int rows = game.getRows();
        int columns = game.getColumns();
        int minePercentage = game.getMinePercentage();

        if(rows > Minesweeper.MIN_ROW_AMOUNT) {
            initializeBoard(rows - 1, columns, minePercentage);
        }
    }

    /**
     * Increases the amount of rows by 1.
     */
    private void increaseRows() {
        int rows = game.getRows();
        int columns = game.getColumns();
        int minePercentage = game.getMinePercentage();

        if(rows < Minesweeper.MAX_ROW_AMOUNT) {
            initializeBoard(rows + 1, columns, minePercentage);
        }
    }

    /**
     * Decreases the amount of columns by 1.
     */
    private void decreaseColumns() {
        int rows = game.getRows();
        int columns = game.getColumns();
        int minePercentage = game.getMinePercentage();

        if(columns > Minesweeper.MIN_COLUMN_AMOUNT) {
            initializeBoard(rows, columns - 1, minePercentage);
        }
    }

    /**
     * Increases the amount of columns by 1.
     */
    private void increaseColumns() {
        int rows = game.getRows();
        int columns = game.getColumns();
        int minePercentage = game.getMinePercentage();

        if(columns < Minesweeper.MAX_COLUMN_AMOUNT) {
            initializeBoard(rows, columns + 1, minePercentage);
        }
    }

    /**
     * Increases the amount of rows by 1.
     */
    private void decreaseMinePercentage() {
        int rows = game.getRows();
        int columns = game.getColumns();
        int minePercentage = game.getMinePercentage();

        if(minePercentage > Minesweeper.MIN_MINE_PERCENTAGE) {
            initializeBoard(rows, columns, minePercentage - 1);
        }
    }

    /**
     * Increases the amount of columns by 1.
     */
    private void increaseMinePercentage() {
        int rows = game.getRows();
        int columns = game.getColumns();
        int minePercentage = game.getMinePercentage();

        if(minePercentage < Minesweeper.MAX_MINE_PERCENTAGE) {
            initializeBoard(rows, columns, minePercentage + 1);
        }
    }

    /**
     * Disables a jlabel.
     * It first removes the jlabel's mouselistener.
     * Then it sets the jlabel to not be enabled.
     *
     * @param sourceLabel the jlabel that is going to be disabled
     */
    private void disableJLabel(JLabel sourceLabel) {
        sourceLabel.removeMouseListener(boardListener);
        sourceLabel.setEnabled(false);
    }

    /**
     * Flags or unflags a cell.
     * It first checks that the game is not on its first turn.
     * Then it flags or unflags the cell based on it's previous flagged setting.
     *
     * @param row the row of the given cell
     * @param column the column of the given cell
     */
    private void flagAt(int row, int column) {
        if(!isFirstTurn) {
            game.flagAt(row, column);
            if(game.isFlagAt(row, column)) {
                board[row][column].setText("<html><font size=6 color=white>!</font></html>");
            }
            else {
                board[row][column].setText("");
            }
        }
    }

    /**
     * Highlights or unhighlights a label.
     */
    private void highlightLabel(JLabel sourceLabel, boolean isHighlighted) {
        if(isHighlighted) {
            sourceLabel.setBorder(BorderFactory.createLineBorder(Color.white));
        }
        else {
            sourceLabel.setBorder(BorderFactory.createLineBorder(Color.black));
        }
    }

    /**
     * Locks down the rest of the board after a lost game.
     */
    private void lockBoard() {
        int columns = game.getColumns();
        int rows = game.getRows();

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                if(!game.isVisibleAt(row, column)) {
                    if(game.isMineAt(row, column)) {
                       board[row][column].setBackground(Color.red.darker());
                    }
                    disableJLabel(board[row][column]);
                }
            }
        }
    }

    /**
     * Locks down all of the mines after a victory.
     */
    private void lockMines() {
        int columns = game.getColumns();
        int rows = game.getRows();

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                if(game.isMineAt(row, column)) {
                    board[row][column].setBackground(Color.green.darker());
                    disableJLabel(board[row][column]);
                }
            }
        }
    }

    /**
     * Randomizes the game board if the current game is not on its first turn.
     * NOTE: It is necessary to remove the old mouse listener and add a new one,
     *       as otherwise the mousepressed action will be called twice, making
     *       flagging impossible.
     */
    private void randomizeBoard() {
        if(!isFirstTurn) {
            int columns = game.getColumns();
            int rows = game.getRows();

            isFirstTurn = true;
            game.randomizeBoard();

            for(int row = 0; row < rows; row++) {
                for(int column = 0; column < columns; column++) {
                    board[row][column].removeMouseListener(boardListener);
                    board[row][column].addMouseListener(boardListener);
                    board[row][column].setBackground(Color.gray);
                    board[row][column].setBorder(BorderFactory.createLineBorder(Color.black));
                    board[row][column].setEnabled(true);
                    board[row][column].setText("");
                }
            }
        }
    }

    /**
     * Sets a new size for the board.
     * It begins by automatically changing the rows and columns given if they are less than one to one.
     * Then sets the board to be this new size and initializes each cell on it.
     * And finally it adds numbers around the randomly placed mines on the board.
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     */
    private void initializeBoard(int rows, int columns, int minePercentage) {
        isFirstTurn = true;
        game = new Minesweeper(rows, columns, minePercentage);

        requestFocus();
        removeAll();

        // board panel
        JPanel boardPanel = new JPanel(new GridLayout(0, columns));

        board = new JLabel[rows][columns];

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                board[row][column] = new JLabel("", JLabel.CENTER);
                board[row][column].addMouseListener(boardListener);
                board[row][column].putClientProperty("row", row);
                board[row][column].putClientProperty("column", column);
                board[row][column].setBackground(Color.gray);
                board[row][column].setBorder(BorderFactory.createLineBorder(Color.black));
                board[row][column].setOpaque(true);
                boardPanel.add(board[row][column]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);

        // status panel
        JPanel statusPanel = new JPanel(new BorderLayout());
        String mouseControls = "Mouse Controls: Try a cell [Left click], Flag a cell [Right click]";
        String keyboardControls = "Keyboard Controls: Randomize board [Space], Decrease/increase rows [1,2], \n" +
                "                                    Decrease/increase columns [3,4], Decrease/increase mine percentage [5,6]";
        String gameStats = String.format("Game Stats: Rows: %2d  Columns: %2d  Mine Percentage: %2d   Mine Count: %3d",
                rows, columns, minePercentage, game.getMines());
        JTextArea textArea = new JTextArea(mouseControls + "\n" +
                keyboardControls + "\n" +
                gameStats);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        statusPanel.add(textArea, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }

    /**
     * Updates a single cell.
     * Name of colors used in order: red, skyblue, darkseagreen, thistle, royalblue, mediumseagreen, tomato, blue, and green.
     */
    private void updateCell(int row, int column) {
        if(game.isVisibleAt(row, column)) {
            if(game.isEmptyAt(row, column)) {
                board[row][column].setBackground(Color.white.brighter());
                board[row][column].setText("");
                disableJLabel(board[row][column]);
            }
            else if(game.isMineAt(row, column)) {
                board[row][column].setBackground(Color.red.brighter());
                disableJLabel(board[row][column]);
            }
            else {
                String colorChoices[] = new String[]{"#FF0000", "#87CEEB", "#8FBC8F", "#D8BFD8", "#4169E1", "#3CB371", "#FF6347", "#0000FF", "#008000"};
                int value = game.getValueAt(row, column);

                board[row][column].setBackground(Color.white.brighter());
                board[row][column].setText("<html><font size=6 color=" + colorChoices[value] + ">" + value + "</font></span></html>");
                board[row][column].removeMouseListener(boardListener);
            }
        }
    }

    /**
     * Updates the entire minesweeper gui.
     * It only displays cells that are visible.
     */
    private void updateBoard() {
        int columns = game.getColumns();
        int rows = game.getRows();

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                updateCell(row, column);
            }
        }
    }

    /**
     * Updates the minesweeper game.
     * It first checks if the given cell is flagged.
     * Then it attempts to make the first move always be on an empty cell.
     * It then plays at the given cell.
     * And finally it checks if an end game status has been met.
     */
    private void updateGame(int row, int column) {
        if(!game.isFlagAt(row, column)) {
            if(isFirstTurn) {
                isFirstTurn = false;
                    while(!game.isEmptyAt(row, column)) {
                        game.randomizeBoard();
                    }
            }

            game.playAt(row, column);
            highlightLabel(board[row][column], false);

            if(game.isEmptyAt(row, column)) {
                updateBoard();
            }
            else {
                updateCell(row, column);
            }

            if(game.isOver()) {
                lockBoard();
            }
            else if(game.isWon()) {
                lockMines();
            }
        }
    }

    /**
     * A private inner class used to handle mouse actions.
     */
    private class BoardListener extends MouseAdapter {
        /**
         * Handles mouse pressed action for a cell.
         */
        public void mousePressed(MouseEvent e) {
            int button = e.getButton();

            if(button == MouseEvent.BUTTON1) {
                JLabel sourceLabel = (JLabel)e.getSource();

                updateGame(Integer.parseInt(sourceLabel.getClientProperty("row") + ""), Integer.parseInt(sourceLabel.getClientProperty("column") + ""));
            }
            else if(button == MouseEvent.BUTTON3) {
                JLabel sourceLabel = (JLabel)e.getSource();

                flagAt(Integer.parseInt(sourceLabel.getClientProperty("row") + ""), Integer.parseInt(sourceLabel.getClientProperty("column") + ""));
            }

            repaint();
        }

        /**
         * Handles mouse entered action for a cell.
         */
        public void mouseEntered(MouseEvent me) {
            highlightLabel((JLabel)me.getSource(), true);

            repaint();
        }

        /**
         * Handles mouse exited action for a cell.
         */
        public void mouseExited(MouseEvent me) {
            highlightLabel((JLabel)me.getSource(), false);

            repaint();
        }
    }
}
