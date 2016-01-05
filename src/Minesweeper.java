/**
 * A basic class that can be used to play a game of minesweeper.
 * For example:
 * <pre>
 *     Minesweeper minesweeper = new Minesweeper(4, 5);
 *     System.out.println(minesweeper);
 *     minesweeper.playAt(0, 0);
 *     System.out.println(minesweeper);
 * </pre>
 *
 * @author Luke Sieben
 * @version 2012/09/16
 */

import java.util.Random;

public class Minesweeper {
    public static final int MIN_ROW_AMOUNT = 9, MIN_COLUMN_AMOUNT = 9, MIN_MINE_PERCENTAGE = 5,
            MAX_ROW_AMOUNT = 30, MAX_COLUMN_AMOUNT = 24, MAX_MINE_PERCENTAGE = 25;
    private boolean isOver;
    private int columns;
    private int mines;
    private int rows;
    private int minePercentage;
    private int visibleCells;
    private Cell board[][];
    public static int EMPTY_VALUE = 0;
    public static int MINE_VALUE = -1;

    /**
     * Creates a minesweeper board of size rows by columns.
     */
    public Minesweeper(int rows, int columns, int minePercentage) {
        isOver = false;
        visibleCells = 0;
        this.rows = rows;
        this.columns = columns;
        this.minePercentage = minePercentage;

        initializeBoard(rows, columns);
        addRandomMines();
        addNumbersAroundMines();
    }

    /**
     * Adds a set amount of randomly placed mines around the board.
     * It begins by calculating the number of mines required for the board.
     * Then it adds randomly located mines to the board if the randomly found spot is empty.
     */
    private void addRandomMines() {
        Random random = new Random();
        int randomColumn;
        int randomRow;

        // percentage of mines should be around 9x9 = 81 = 8%, 16*16 = 256 = 6.4%, 16*30 = 480 = 4.8%
        //                                      10mines        40mines              99mines
        int size = rows * columns;

        mines = (int) (size * (minePercentage / 100.0));

        for(int counter = 0; counter < mines; counter++) {
            do {
                randomRow = random.nextInt(rows);
                randomColumn = random.nextInt(columns);
            } while(!board[randomRow][randomColumn].isEmpty());

            board[randomRow][randomColumn].setValue(MINE_VALUE);
        }
    }

    /**
     * Adds the proper numbers around mines on a board.
     * It begins by finding a mine on the board.
     * Then it attempts to increment all of the cells around it.
     */
    private void addNumbersAroundMines() {
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                if(board[row][column].isMine()) {
                    incrementValueAt(row - 1, column - 1);
                    incrementValueAt(row - 1, column);
                    incrementValueAt(row - 1, column + 1);
                    incrementValueAt(row, column - 1);
                    incrementValueAt(row, column + 1);
                    incrementValueAt(row + 1, column - 1);
                    incrementValueAt(row + 1, column);
                    incrementValueAt(row + 1, column + 1);
                }
            }
        }
    }

    /**
     * Attempts to flag the given cell on the board.
     * It first checks that the given cell is within the board's bounds.
     * Then it checks if the given cell is not visible.
     * It then sets the given cell to be flagged or unflagged based on it's previous flagged setting.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public void flagAt(int row, int column) {
        if(isInboundsAt(row, column) && !board[row][column].isVisible()) {
            if(board[row][column].isFlag()) {
                board[row][column].setFlag(false);
            }
            else {
                board[row][column].setFlag(true);
            }
        }
    }

    /**
     * Attempts to floodfill the area around a given cell.
     * It first checks that the given cell is within the board's bounds.
     * Then it checks if the given cell is not visible and not flagged.
     * It then sets the current cell to be visible now.
     * Then if the current cell is empty, it proceeds to floodfill around that cell.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    private void floodFillAt(int row, int column) {
        if(isInboundsAt(row, column) && !board[row][column].isVisible() && !board[row][column].isFlag()) {
            board[row][column].setVisible(true);
            visibleCells++;

            if(board[row][column].isEmpty()) {
                floodFillAt(row - 1, column - 1);
                floodFillAt(row - 1, column);
                floodFillAt(row - 1, column + 1);
                floodFillAt(row, column - 1);
                floodFillAt(row, column + 1);
                floodFillAt(row + 1, column - 1);
                floodFillAt(row + 1, column);
                floodFillAt(row + 1, column + 1);
            }
        }
    }

    /**
     * Returns the number of columns on the board.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the numbers of rows on the board.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Gets the mine percentage.
     * @return the mine percentage
     */
    public int getMinePercentage() {
        return minePercentage;
    }

    /**
     * Gets the number of mines.
     * @return the number of mines
     */
    public int getMines() {
        return mines;
    }

    /**
     * Returns the value of the cell on the board.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public int getValueAt(int row, int column) {
        return board[row][column].getValue();
    }

    /**
     * Increments the value of the given cell.
     * It first checks that the given cell is within the board's bounds.
     * Then it checks that the given cell is not a mine.
     * And then it increases the value of that cell.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    private void incrementValueAt(int row, int column) {
        if(isInboundsAt(row, column) && !board[row][column].isMine()) {
            board[row][column].setValue(board[row][column].getValue() + 1);
        }
    }

    /**
     * Returns true if the given cell is empty, false otherwise.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public boolean isEmptyAt(int row, int column) {
        return board[row][column].isEmpty();
    }

    /**
     * Returns true if the given cell is empty, false otherwise.
     * It first checks that the given cell is within the board's bounds.
     * Then it returns whether or not that cell is flagged.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public boolean isFlagAt(int row, int column) {
        return board[row][column].isFlag();
    }

    /**
     * Returns true if the given cell is in bounds, false otherwise.
     * It firsts checks that the given row is within the board's bounds.
     * Then it checks that the given column is within the board's bounds.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     * @return       the boolean value of whether or not the given cell is inbounds.
     */
    public boolean isInboundsAt(int row, int column) {
        return row > -1 && row < rows && column > -1  && column < columns;
    }

    /**
     * Returns true if the given cell is a mine, false otherwise.
     * It first checks that the given cell is within the board's bounds.
     * Then it returns whether or not that cell is a mine.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public boolean isMineAt(int row, int column) {
        return board[row][column].isMine();
    }

    /**
     * Returns true if the game is over, false otherwise.
     * It checks to see if any mines are visible.
     */
    public boolean isOver() {
        return isOver;
    }

    /**
     * Returns true if the given cell is visible, false otherwise.
     * It first checks that the given cell is within the board's bounds.
     * Then it returns whether or not that cell is visible.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public boolean isVisibleAt(int row, int column) {
        return board[row][column].isVisible();
    }

    /**
     * Returns true if the game is won, false otherwise.
     * It checks to see if all the non-mine cells are visible.
     */
    public boolean isWon() {
        return visibleCells == rows * columns - mines;
    }

    /**
     * Attempts to make a move on the board using the given cell.
     * It first checks that the given cell is within the board's bounds.
     * Then if the cell is empty, it attempts to floodfill around it.
     * Otherwise, it justs sets the single cell to be visible.
     *
     * @param row    the row of the given cell
     * @param column the column of the given cell
     */
    public void playAt(int row, int column) {
        if(isInboundsAt(row, column) && !board[row][column].isFlag()) {
            if(board[row][column].isEmpty()) {
                floodFillAt(row, column);
            }
            else if(board[row][column].isMine()) {
                isOver = true;
            }
            else {
                board[row][column].setVisible(true);
                visibleCells++;
            }
        }
    }

    /**
     * Randomizes the board.
     * It begins by making all of the cells on the board empty, not visible, and not flagged.
     * Then it adds random mines to the board.
     * And finally it adds numbers around the randomly placed mines on the board.
     * <p>
     * NOTE: This can cause an infinite loop if the board size is less than 12.
     *       This is because the user can add in a basic for loop to ensure that the first move a player takes will always uncover an empty square.
     */
    public void randomizeBoard() {
        isOver = false;
        visibleCells = 0;

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                board[row][column].setFlag(false);
                board[row][column].setVisible(false);
                board[row][column].setValue(EMPTY_VALUE);
            }
        }

        addRandomMines();
        addNumbersAroundMines();
    }

    /**
     * Sets the size of the board.
     * It first sets the board to be this new size and initializes each cell on it.
     * And finally it adds numbers around the randomly placed mines on the board.
     *
     * @param rows    the number of rows on the board
     * @param columns the number of columns on the board
     */
    private void initializeBoard(int rows, int columns) {
        board = new Cell[rows][columns];

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                board[row][column] = new Cell();
            }
        }
    }

    /**
     * Returns a nicely formatted string of the board.
     */
    @Override
    public String toString() {
        StringBuilder boardDisplay = new StringBuilder(rows * columns + columns + 1);
        int value;

        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                if(board[row][column].isVisible()) {
                    value = board[row][column].getValue();

                    if(value == EMPTY_VALUE) {
                        boardDisplay.append(' ');
                    }
                    else if(value == MINE_VALUE) {
                        boardDisplay.append('*');
                    }
                    else {
                        boardDisplay.append(value);
                    }
                }
                else if(board[row][column].isFlag()) {
                    boardDisplay.append('!');
                }
                else {
                    boardDisplay.append('#');
                }
            }
            boardDisplay.append('\n');
        }
        return boardDisplay.toString();
    }

    /**
     * A private inner class used to represent the structure of a cell in a minesweeper game.
     * For example:
     * <pre>
     *     Cell cell = new Cell();
     * </pre>
     *
     * @author Luke Sieben
     * @version 2012/09/19
     */

    private class Cell {
        private boolean isFlag;
        private boolean isVisible;
        private int value;

        /**
         * Creates an empty cell.
         */
        public Cell() {
            this(EMPTY_VALUE, false, false);
        }

        /**
         * Creates a cell with the given values.
         */
        public Cell(int value, boolean isVisible, boolean isFlag) {
            setFlag(isFlag);
            setVisible(isVisible);
            setValue(value);
        }

        /**
         * Returns the value of the cell.
         */
        public int getValue() {
            return value;
        }

        /**
         * Returns true if the cell is empty, false otherwise.
         */
        public boolean isEmpty() {
            return value == EMPTY_VALUE;
        }

        /**
         * Returns true if the cell is flagged, false otherwise.
         */
        public boolean isFlag() {
            return isFlag;
        }

        /**
         * Returns true if the cell is a mine, false otherwise.
         */
        public boolean isMine() {
            return value == MINE_VALUE;
        }

        /**
         * Returns true if the cell is visible, false otherwise.
         */
        public boolean isVisible() {
            return isVisible;
        }

        /**
         * Sets the isFlag variable of a cell.
         */
        public void setFlag(boolean isFlag) {
            this.isFlag = isFlag;
        }

        /*
         * Sets the value of a cell.
         */
        public void setValue(int value) {
            this.value = value;
        }

        /**
         * Sets the isVisible variable of a cell.
         */
        public void setVisible(boolean isVisible) {
            this.isVisible = isVisible;
        }

        /**
         * Returns a nicely formatted string of the cell.
         */
        @Override
        public String toString() {
            return getClass().getName() + "[value=" + value + ", isVisible=" + isVisible + ", isFlag=" + isFlag + "]";
        }
    }
}
