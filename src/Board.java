import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board {

    private final int BOARD_WIDTH = 8, BOARD_HEIGHT = 8, //Final variables for the size of the board
            BLACK_ROWS = 3, WHITE_ROWS = 3;

    public static int lastKeyPressed;

    //Vector for the location of the current selection in grid
    public static int currentSelectionX = 0;
    public static int currentSelectionY = 0;

    //Vector for the currently selected move for a piece
    public static int currentMoveSelectionX = -1;
    public static int currentMoveSelectionY = -1;

    public static boolean selecting = true;

    public BoardSpace[][] board;
    private JFrame frame;
    private JPanel canvas;

    public Board() {
        board = createBoard();

        //Setup canvas
        frame = new JFrame("The Chessboard");
        canvas = new DrawChessBoard();
        frame.add(canvas);
        frame.getContentPane().setPreferredSize(new Dimension(400, 400));
        frame.pack();
        frame.setVisible(true);

        //Add method to close the window
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Add method to read when key is pressed, stores it into Board class.
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Board.lastKeyPressed = e.getKeyCode();
            }
        });
    }

    public void selection() {
        //Wait 10 ms when there is no input
        if (lastKeyPressed == 0) {
            try {
                Thread.sleep(10);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Slightly messy input handling, since there is no real game loop. If that were the case you could look at which key changed each tick,
        //and base input handling off of that (i.e. loop through all keys and see if they have been pressed, and act accordingly).

        //This is the input handling during the selection phase of the game, where the player selects which piece to do something with.
        if (selecting) {
            switch (lastKeyPressed) {
                case 37: //Left
                case 65: //A
                    if (currentSelectionX > 0) {
                        currentSelectionX -= 1;

                        //The y coordinate get's changed to stay on the black squares.
                        currentSelectionY += currentSelectionY % 2 == 0 ? 1 : -1;
                    }
                    break;
                case 39: //Right
                case 68: //D
                    if (currentSelectionX < 7) {
                        currentSelectionX += 1;

                        //The y coordinate get's changed to stay on the black squares.
                        currentSelectionY += currentSelectionY % 2 == 0 ? 1 : -1;
                    }
                    break;
                case 38: //Up
                case 87: //W
                    currentSelectionY -= currentSelectionY > 1 ? 2 : 0;
                    break;
                case 40: //Down
                case 83: //S
                    currentSelectionY += currentSelectionY < 6 ? 2 : 0;
                    break;
                case 13: //Enter/return
                case 32: //Spacebar
                    if (board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.EMPTY)
                        break;

                    selecting = false;

                    //Give a default selection visual
                    currentMoveSelectionX = currentSelectionX > 0 ? currentSelectionX - 1 : currentSelectionX + 1;
                    if (board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.BLACK) {
                        currentMoveSelectionY = currentSelectionY - 1;
                    } else {
                        currentMoveSelectionY = currentSelectionY + 1;
                    }

                    //reset input to prevent spacebar action from triggering twice
                    lastKeyPressed = 0;
                    selectPieceMovement();
                    break;
            }
        } else {
            selectPieceMovement();
        }

        //Reset input and repaint
        lastKeyPressed = 0;
        canvas.repaint();
    }

    private void selectPieceMovement() {
        //
        switch (lastKeyPressed) {
            case 37: //Left
            case 65: //A

                //Place selection visual either at the left of the currently selected piece, or the right, depending on if the left is out of screen.
                currentMoveSelectionX = currentSelectionX > 0 ? currentSelectionX - 1 : currentSelectionX + 1;

                //Place the selection right in front of the player
                if (board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.BLACK) {
                    currentMoveSelectionY = currentSelectionY - 1;
                } else {
                    currentMoveSelectionY = currentSelectionY + 1;
                }

                //if (checkIfCanHit()){
                //}
                break;
            case 39: //Right
            case 68: //D

                //Place selection visual either at the right of the currently selected piece, or the left, depending on if the right is out of screen.
                currentMoveSelectionX = currentSelectionX < 7 ? currentSelectionX + 1 : currentSelectionX - 1;

                if (board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.BLACK) {
                    currentMoveSelectionY = currentSelectionY - 1;
                } else {
                    currentMoveSelectionY = currentSelectionY + 1;
                }
                break;
            case 13: //Enter/return
            case 32: //Spacebar
                if (board[currentMoveSelectionY][currentMoveSelectionX].type == BoardSpace.BoardType.EMPTY) {
                    board[currentMoveSelectionY][currentMoveSelectionX].type = board[currentSelectionY][currentSelectionX].type;

                    board[currentSelectionY][currentSelectionX].type = BoardSpace.BoardType.EMPTY;

                    currentMoveSelectionX = -1;
                    currentMoveSelectionY = -1;

                    selecting = true;
                }
                break;
            case 67: //C: cancel
                currentMoveSelectionX = -1;
                currentMoveSelectionY = -1;
                selecting = true;

        }

    }

    private boolean checkIfCanHit() {

        int _dY = currentSelectionY - currentMoveSelectionY;
        int _dX = currentSelectionX - currentMoveSelectionX;

        if (board[currentSelectionY][currentSelectionX].type != board[currentMoveSelectionY][currentMoveSelectionX].type
                && board[currentMoveSelectionY + _dY][currentMoveSelectionX + _dX].type == BoardSpace.BoardType.EMPTY)
            return true;
        return false;
    }

    //Function that creates a start position board
    private BoardSpace[][] createBoard() {
        BoardSpace[][] _board = new BoardSpace[BOARD_WIDTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {

                //current cell based on i and j values
                int cell = i * BOARD_WIDTH + j;

                //Depending on the row, the pieces are on even or odd cells. On the first row (row 0, thus even) the pieces are placed
                //on the even columns (0, 2, 6, etc). On the odd rows the pieces are placed on the odd cells.
                Boolean evenOrOdd = i % 2 == 0 ? j % 2 == 0 : j % 2 == 1;

                //Cells if they are above or below a certain row, and if they are on an even square
                Boolean whiteSpace = evenOrOdd && cell <= WHITE_ROWS * BOARD_WIDTH;
                Boolean blackSpace = evenOrOdd && cell >= BOARD_HEIGHT * BOARD_WIDTH - BLACK_ROWS * BOARD_WIDTH;

                //Create empty board space if not a white or black space
                if (!whiteSpace && !blackSpace) {
                    _board[i][j] = new BoardSpace();
                }
                //or place a piece when needed
                else {
                    BoardSpace.BoardType cellSpace = whiteSpace ? BoardSpace.BoardType.WHITE : BoardSpace.BoardType.BLACK;
                    _board[i][j] = new BoardSpace(cellSpace);
                }
            }
        }
        return _board;
    }
}
