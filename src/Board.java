import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class Board{

    private final int BOARD_WIDTH = 8, BOARD_HEIGHT = 8, //Final variables for the size of the board
            BLACK_ROWS = 3, WHITE_ROWS = 3;

    public static int lastKeyPressed;
    public static int currentSelectionX = 0;
    public static int currentSelectionY = 0;

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
        frame.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                System.exit(0);
            }
        });

        //Add method to read when key is pressed, stores it into Board class.
        frame.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                Board.lastKeyPressed = e.getKeyCode();
            }
        });
    }

    public void selection(){
        //Wait 10 ms when there is no input
        if (lastKeyPressed == 0){
            try {
                Thread.sleep(10);
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        switch (lastKeyPressed){
            case 37: //Left
            case 65: //A
                currentSelectionX -= currentSelectionX > 0 ? 1 : 0;
                break;
            case 39: //Right
            case 68: //D
                currentSelectionX += currentSelectionX < 7 ? 1 : 0;;
                break;
            case 38: //Up
            case 87: //W
                currentSelectionY -= currentSelectionY > 0 ? 2 : 0;;
                break;
            case 40: //Down
            case 83: //S
                currentSelectionY += currentSelectionY < 6 ? 2 : 0;;
                break;
        }

        //Reset input and repaint
        lastKeyPressed = 0;
        canvas.repaint();
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
