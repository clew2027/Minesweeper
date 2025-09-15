package org.cis1200.mineSweeper;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class RunMineSweeper implements Runnable {

    /**
     * This class sets up the top-level frame and widgets for the GUI.
     *
     * This game adheres to a Model-View-Controller design framework. This
     * framework is very effective for turn-based games. We STRONGLY
     * recommend you review these lecture slides, starting at slide 8,
     * for more details on Model-View-Controller:
     * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
     *
     * In a Model-View-Controller framework, Game initializes the view,
     * implements a bit of controller functionality through the reset
     * button, and then instantiates a GameBoard. The GameBoard will
     * handle the rest of the game's view and controller functionality, and
     * it will instantiate a TicTacToe object to serve as the game's model.
     */
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("MineSweeper");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // points panel
        final JPanel points_panel = new JPanel();
        frame.add(points_panel, BorderLayout.WEST);
        final JLabel points = new JLabel("Points: ");
        points_panel.add(points);

        // Game board
        final MineSweeperBoard board = new MineSweeperBoard(status, points);
        frame.add(board, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // save button
        final JButton save = new JButton("save");
        save.addActionListener(e -> {
            try {
                board.save();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        control_panel.add(save);

        // load button
        final JButton load = new JButton("load");
        load.addActionListener(e -> {
            try {
                board.load();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        control_panel.add(load);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
