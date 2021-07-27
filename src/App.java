public class App {

    public static Board board;

    public void Start(){
        board = new Board();

    }

    public static Board GetBoard(){
        return board;
    }
}
