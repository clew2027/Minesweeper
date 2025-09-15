package org.cis1200.mineSweeper;


import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class MineSweeper {
    private Box[][] board;
    private int points;
    private Dimension[] minePositions;
    private boolean gameOver;

    private BufferedReader csvReader;

    static final String GAME_STATE = "files/game_state.csv";

    /**
     * Constructor sets up game state.
     */
    public MineSweeper() {
        reset();
    }

    // make new constructor so that can test and mines are not random.

    /**
     * playTurn allows players to play a turn. Returns true if the move is
     * successful and false if a player tries to play in a location that is
     * taken or after the game has ended. If the turn is successful and the game
     * has not ended, the player is changed. If the turn is unsuccessful or the
     * game has ended, the player is not changed.
     *
     * @param c column to play in
     * @param r row to play in
     * @return whether the turn was successful
     */
    public boolean playTurn(int r, int c) {
        // if the clicked on a mine or game over
        System.out.println("played");
        System.out.println("" + gameOver);
        if (gameOver) {
            return false;
        }
        if ((board[r][c].getStatus() == 9) || gameOver) {
            gameOver = true;
            board[r][c].setExposed(true);
            return false;
        } else {
            // call method that clears out others
            LinkedList<Box> alreadyCleared = new LinkedList<Box>();
            clearOutSurrounding(r, c, alreadyCleared);
            points += alreadyCleared.size();
            return true;
        }
    }

    public void clearOutSurrounding(int r, int c, LinkedList<Box> alreadyCleared) {
        if (alreadyCleared.contains(board[r][c])) {
            return;
        } else if (board[r][c].getStatus() == 0) {
            board[r][c].setExposed(true);
            alreadyCleared.add(board[r][c]);
            // for loop r r-1 to r+1, c-1 to c+1, if in bounds then recurisve call, if not
            // then nothing (continue).
            for (int i = r - 1; i < r + 2; i++) {
                for (int j = c - 1; j < c + 2; j++) {
                    if (j > -1 && j < 10 && i > -1 && i < 10) {
                        clearOutSurrounding(i, j, alreadyCleared);
                    }
                }
            }
        } else if (board[r][c].getStatus() < 9) {
            board[r][c].setExposed(true);
            alreadyCleared.add(board[r][c]);
        }
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     * checkWinner only looks for horizontal wins.
     *
     * @return 0 if player has not won yet, 1 if game is won, 2 if game is lost.
     */
    public int checkWinner() {
        if (gameOver) {
            return 2;
        }
        if (points < 100 - minePositions.length) {
            return 0;
        } else if (points == 100 - minePositions.length) {
            gameOver = true;
            return 1;
        }
        return 0;
    }

    /**
     * printGameState prints the current game state
     * for debugging.
     */
    public void printGameState() {
        System.out.println("\n\nPoints " + points + ":\n");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j]);
                if (j < 2) {
                    System.out.print(" | ");
                }
            }
            if (i < 2) {
                System.out.println("\n---------");
            }
        }
    }

    public Dimension[] getMinePositions() {
        return minePositions;
    }

    public void setMinePositions(Dimension[] newArray) {
        Dimension[] encapsulation = newArray;
        minePositions = encapsulation;
    }

    public void setExposed(Dimension d, boolean b) {
        board[d.width][d.height].setExposed(b);
    }

    /**
     * reset (re-)sets the game state to start a new game.
     * Randomizes where the mines are
     */
    public void reset() {
        board = new Box[10][10];
        points = 0;
        setPoints(0);

        gameOver = false;
        minePositions = randomMineGenerator();
        csvReader = FileUtilities.fileToReader(GAME_STATE);
        for (int i = 0; i < minePositions.length; i++) {
            int width = (int) minePositions[i].getWidth();
            int height = (int) minePositions[i].getHeight();
        }
        // make game state empty
        setUpBoard();
    }

    public void setUpBoard() {
        board = new Box[10][10];
        // set the board with box with mines
        for (int i = 0; i < minePositions.length; i++) {
            Dimension curr = minePositions[i];
            int width = (int) curr.getWidth();
            int height = (int) curr.getHeight();
            board[width][height] = new Box(9);
        }
        // set the board with box without mines
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Dimension c = new Dimension(i, j);
                if (board[i][j] != null) {
                    continue;
                } else {
                    board[i][j] = new Box(numAdjacent(c, minePositions));
                }
            }
        }
    }

    public void save() throws IOException {
        FileUtilities
                .writeStringsToFile(Collections.singletonList(("" + points)), GAME_STATE, false);

        ArrayList<String> minePositionsStr = new ArrayList<>();
        for (Dimension d : minePositions) {
            minePositionsStr.add(d.width + "," + d.height);
        }
        FileUtilities.writeStringsToFile(minePositionsStr, GAME_STATE, true);

        for (int i = 0; i < board.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < board[i].length; j++) {
                int x = board[i][j].isExposed() ? 1 : 0;
                sb.append(x);
                if (j < board[i].length - 1) {
                    sb.append(",");
                }
            }
            FileUtilities
                    .writeStringsToFile(Collections.singletonList(sb.toString()), GAME_STATE, true);
        }
    }

    public void load() throws IOException {
        LineIterator csvLineReader = new LineIterator(csvReader);
        LinkedList<Dimension> mineList = new LinkedList<Dimension>();
        String points;
        String shape;
        if (csvLineReader.hasNext()) {
            points = csvLineReader.next();
            setPoints(Integer.parseInt(points));
        } else {
            return;
        }

        for (int i = 0; i < 10; i++) {
            if (csvLineReader.hasNext()) {
                shape = csvLineReader.next();
                String[] data = shape.split(",");
                int c = Integer.parseInt(data[0]);
                int d = Integer.parseInt(data[1]);
                mineList.add(new Dimension(c, d));
            }
        }
        Dimension[] mineArray = new Dimension[mineList.size()];
        for (int i = 0; i < mineList.size(); i++) {
            mineArray[i] = mineList.get(i);
        }

        setMinePositions(mineArray);
        setUpBoard();
        while (csvLineReader.hasNext()) {
            for (int i = 0; i < 10; i++) {
                String[] data = csvLineReader.next().split(",");
                for (int j = 0; j < 10; j++) {
                    int exposedInt = Integer.parseInt(data[j]);
                    if (exposedInt == 0) {
                        board[i][j].setExposed(false);
                    } else {
                        board[i][j].setExposed(true);
                    }
                }
            }
        }
        csvReader.close();
    }

    public void setPoints(int pointss) {
        points = pointss;
    }

    // method to get how many adjacent
    public static int numAdjacent(Dimension d, Dimension[] mineArray) {
        int finalInt = 0;
        for (int i = 0; i < mineArray.length; i++) {
            Dimension curr = mineArray[i];
            int mineX = curr.width;
            int mineY = curr.height;
            int boxX = d.width;
            int boxY = d.height;
            if ((boxX + 1 == mineX && boxY + 1 == mineY) || (boxX + 1 == mineX && boxY == mineY) ||
                    (boxX == mineX && boxY + 1 == mineY) || (boxX - 1 == mineX && boxY + 1 == mineY)
                    ||
                    (boxX - 1 == mineX && boxY == mineY) || (boxX - 1 == mineX && boxY - 1 == mineY)
                    ||
                    (boxX + 1 == mineX && boxY - 1 == mineY)
                    || (boxX == mineX && boxY - 1 == mineY)) {
                finalInt++;
            }
        }
        return finalInt;
    }

    public static Dimension[] randomMineGenerator() {
        Dimension[] finalArray = new Dimension[10];
        int mines = 0; // Keep track of unique mines added

        while (mines < 10) {
            Dimension newMine = new Dimension(
                    (int) (10 * Math.random()), (int) (10 * Math.random())
            );
            boolean isUnique = true;
            for (int j = 0; j < mines; j++) {
                if (finalArray[j].equals(newMine)) {
                    isUnique = false;
                    break;
                }
            }

            if (isUnique) {
                finalArray[mines] = newMine;
                mines++;
            }
        }

        return finalArray;
    }

    /**
     * getCell is a getter for the contents of the cell specified by the method
     * arguments.
     *
     * @param c column to retrieve
     * @param r row to retrieve
     * @return an integer denoting the if the box is exposed
     */
    public Box getCell(int r, int c) {
        return board[r][c];
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getPoints() {
        return points;
    }

    /**
     * This main method illustrates how the model is completely independent of
     * the view and controller. We can play the game from start to finish
     * without ever creating a Java Swing object.
     *
     * This is modularity in action, and modularity is the bedrock of the
     * Model-View-Controller design framework.
     *
     * Run this file to see the output of this method in your console.
     */
    public static void main(String[] args) {

    }
}
