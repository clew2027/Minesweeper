package org.cis1200.mineSweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class MineSweeperBoard extends JPanel {
    private final MineSweeper MSP; // model for the game
    private JLabel status; // current status text

    private JLabel points; // current points

    // Game constants
    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;

    /**
     * Initializes the game board.
     */
    public MineSweeperBoard(JLabel statusInit, JLabel pointsInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        MSP = new MineSweeper(); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        points = pointsInit;

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // updates the model given the coordinates of the mouseclick
                MSP.playTurn(p.x / 30, p.y / 30);
                updateStatus(); // updates the status JLabel
                updatePoints();
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        MSP.reset();
        status.setText("Game Start");
        updatePoints();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void save() throws IOException {
        MSP.save();
        repaint();
        requestFocusInWindow();
    }

    public void load() throws IOException {
        MSP.load();
        repaint();
        requestFocusInWindow();
    }

    private void updatePoints() {
        points.setText("Points: " + MSP.getPoints());
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {

        int winner = MSP.checkWinner();
        if (winner == 0) {
            status.setText("Keep Playing!!!");
        } else if (winner == 1) {
            status.setText("You won!!!");
        } else if (winner == 2) {
            status.setText("You lost");
        }
    }

    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        int unitWidth = BOARD_WIDTH / 10;
        int unitHeight = BOARD_HEIGHT / 10;

        for (int i = 0; i < 10; i++) {
            g.drawLine(unitWidth * i, 0, unitWidth * i, BOARD_HEIGHT);
            g.drawLine(0, unitHeight * i, BOARD_WIDTH, unitHeight * i);
        }

        // Draws the state of the box (if is adjacent to mine)
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Box currBox = MSP.getCell(j, i);
                if (currBox.isExposed()) {
                    String character = currBox.getStatus() + "";
                    g.drawString(character, (30 * (j)) + 10, (30 * (i + 1) - 5));
                }
            }
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
