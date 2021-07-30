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

    //Vector for which piece should be removed
    public static int hitSelectionX = -1;
    public static int hitSelectionY = -1;

    public static boolean turn = true;
    public static boolean selecting = true;
    public static boolean hittingPiece = false;
    public static boolean canHitEnemy = false;

    public BoardSpace[][] board;
    private JFrame frame;
    private JPanel canvas;

    public Board() {
        board = createBoard();

        setupFrame();
    }

    private void setupFrame() {
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
                    //Move current selection down if inside field
                    currentSelectionY -= currentSelectionY > 1 ? 2 : 0;
                    break;
                case 40: //Down
                case 83: //S
                    //Move current selection up if inside field
                    currentSelectionY += currentSelectionY < 6 ? 2 : 0;
                    break;
                case 13: //Enter/return
                case 32: //Spacebar

                    //Select piece if there is not an empty space there
                    if (board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.EMPTY)
                        break;

                    //Check for turn
                    if ((turn && board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.BLACK)
                            || (!turn && board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.WHITE))
                        break;

                    selecting = false;
                    moveSelection(1);

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
        System.out.println(selecting);
        switch (lastKeyPressed) {
            case 37: //Left
            case 65: //A
                moveSelection(-1);
                break;
            case 39: //Right
            case 68: //D
                moveSelection(1);
                break;
            case 13: //Enter/return
            case 32: //Spacebar
                if (board[currentMoveSelectionY][currentMoveSelectionX].type == BoardSpace.BoardType.EMPTY) {
                    board[currentMoveSelectionY][currentMoveSelectionX].type = board[currentSelectionY][currentSelectionX].type;

                    board[currentSelectionY][currentSelectionX].type = BoardSpace.BoardType.EMPTY;

                    currentMoveSelectionX = -1;
                    currentMoveSelectionY = -1;

                    if (hittingPiece) {
                        board[hitSelectionY][hitSelectionX].type = BoardSpace.BoardType.EMPTY;
                    }

                    selecting = true;
                    turn = !turn; //Set turn to it's opposite value, to get the other turn.
                }
                break;
            case 67: //C: cancel
                currentMoveSelectionX = -1;
                currentMoveSelectionY = -1;
                selecting = true;
                break;
        }

    }

    private void moveSelection(int dir) { //1 is right, -1 is left. Moves the direction of the direction you want to move towards.

        if (currentSelectionX <= 0 && dir == -1 || currentSelectionX >= 7 && dir == 1)
            return;

        //Place selection visual either at the right of the currently selected piece, or the left, depending on if the right is out of screen.
        currentMoveSelectionX = currentSelectionX + dir;

        //Place the y coordinate up or below
        if (board[currentSelectionY][currentSelectionX].type == BoardSpace.BoardType.BLACK) {
            currentMoveSelectionY = currentSelectionY - 1;
        } else {
            currentMoveSelectionY = currentSelectionY + 1;
        }

        hittingPiece = checkIfCanHit();
    }

    private boolean checkIfCanHit() {

        int _dY = currentMoveSelectionY - currentSelectionY;
        int _dX = currentMoveSelectionX - currentSelectionX;

        if (board[currentSelectionY][currentSelectionX].type != board[currentMoveSelectionY][currentMoveSelectionX].type
                && board[currentMoveSelectionY][currentMoveSelectionX].type != BoardSpace.BoardType.EMPTY
                && board[currentMoveSelectionY + _dY][currentMoveSelectionX + _dX].type == BoardSpace.BoardType.EMPTY) {

            hitSelectionX = currentMoveSelectionX;
            hitSelectionY = currentMoveSelectionY;

            currentMoveSelectionY += _dY;
            currentMoveSelectionX += _dX;
            return true;
        }
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
                    _board[i][j] = new BoardSpace(j, i);
                }
                //or place a piece when needed
                else {
                    BoardSpace.BoardType cellSpace = whiteSpace ? BoardSpace.BoardType.WHITE : BoardSpace.BoardType.BLACK;
                    _board[i][j] = new BoardSpace(cellSpace, j, i);
                }
            }
        }
        return _board;
    }
}
