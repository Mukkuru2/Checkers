package BoardStates;

public class BoardSpace {

    //possible boardstates. Could make it a parent class and inherit, but there don't seem to be that many traits that would
    //make that worth it. Might be worth looking at if kings (dubbele Dam) get added.
    public enum BoardType {
        WHITE,
        BLACK,
        EMPTY
    }

    public BoardType type;
    public int gridX;
    public int gridY;

    //Default state of board: empty
    public BoardSpace(int x, int y) {
        this( x, y, BoardType.EMPTY);
    }

    public BoardSpace(int x, int y, BoardType type) {
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
