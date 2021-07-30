public class BoardSpace {

    //possible boardstates
    public enum BoardType {
        WHITE,
        BLACK,
        WHITE_KING,
        BLACK_KING,
        EMPTY
    }

    public BoardType type;
    public int gridX;
    public int gridY;

    //Default state of board: empty
    public BoardSpace(int x, int y) {
        this(BoardType.EMPTY, x, y);
    }

    public BoardSpace(BoardType type, int x, int y) {
        this.type = type;
        gridX = x;
        gridY = y;
    }

    @Override
    public String toString() {
        String _name = "";
        switch (type) {
            case EMPTY:
                _name = " ";
                break;
            case BLACK:
                _name = "B";
                break;
            case WHITE:
                _name = "W";
                break;

        }

        return _name;
    }
}
