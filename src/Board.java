public class Board {

    private final int BOARD_WIDTH = 10, BOARD_HEIGHT = 10, //Final variables for the size of the board
    BLACK_ROWS = 3, WHITE_ROWS = 3;


    public BoardSpace[][] board;

    public Board(){
        board = new BoardSpace[BOARD_WIDTH][BOARD_HEIGHT];

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {

                //current cell based on i and j values, plus 1 because arrays start at zero and counting cells starts at one
                int cell = i * 10 + j + 1;

                //Cells if they are above or below a certain row, and if they are on an even square
                Boolean whiteSpace = cell % 2 == 0 && cell <= WHITE_ROWS * BOARD_WIDTH;
                Boolean blackSpace = cell % 2 == 0 && cell >= BOARD_HEIGHT * BOARD_WIDTH - BLACK_ROWS * BOARD_WIDTH;

                //Create empty board space if not a white or black space
                if (!whiteSpace && !blackSpace){
                    board[i][j] = new BoardSpace();
                }
                //or place a piece when needed
                else {
                    BoardSpace.BoardType cellSpace = whiteSpace ? BoardSpace.BoardType.WHITE : BoardSpace.BoardType.BLACK;
                    board[i][j] = new BoardSpace(cellSpace);
                }
            }
        }

    }




}
