=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 1200 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Recursion: Implemented in MineSweeper class's cleaOutSurrounding method. The method sets the isExposed of the
                current box but also does the same to the 8 adjacent boxes if current box's status is not 9.

  2. JUnit: JUnit is used to test methods in the MineSweeper class.

  3. File IO: File IO is used to read and write from the game_state.csv file to save and load the game.

  4. 2D Arrays: 2D Arrays is what I used to represent the board.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

    MineSweeper has methods like reset, setUpBoard, randomMineGenerator, and numAdjacent to set up the boards
    and the status of all the boxes in the boards. Additionally, there are the save and load classes. There is also
    play turn that will change things in the game state when a player clicks on the board and there are getters and
    setters.

    MineSweeperBoard also has reset, save, load, but it also has paint component.

    RunMineSweeper runs the game and also implements everything needed for Swing.

    Box is what each slot of the 2D array represents. Each box has a status (how many mines it is adjacent to, 9 if
    the box is a mine), and a boolean value that says if it is exposed. The status of the box will only be drawn onto
    the board if it is exposed.

    FileUtilities has methods to read and write from a file.

    LineIterator has methods for a buffered reader to read line by line

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

  No


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I did not have many methods that could possibly break encapsulation, but for setMinePositions in MineSweeper class,
  I made sure that encapsulation was implemented.
  If given a chance, I would make my code more elegant. For example, I would do for loops or helper methods instead
  of repeating code.



========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
