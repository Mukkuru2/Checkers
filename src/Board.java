import java.awt.*;

public class Board{

    private final int BOARD_WIDTH = 8, BOARD_HEIGHT = 8, //Final variables for the size of the board
            BLACK_ROWS = 3, WHITE_ROWS = 3;


    public BoardSpace[][] board;

    public Board() {
        board = createBoard();

        //Setup canvas
        Frame frame = new Frame("The Chessboard");
        Canvas canvas = new DrawChessBoard();
        frame.add(canvas);
        frame.pack();
        frame.setVisible(true);
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
