public class App {

    public static Board board;
    private boolean finished = false;

    public void Start() {
        board = new Board();

        while (!finished) {
            board.selection();
        }

    }

    public static Board GetBoard() {
        return board;
    }
}
