public class BoardSpace {

    //possible boardstates
    public enum BoardType{
        WHITE,
        BLACK,
        WHITE_KING,
        BLACK_KING,
        EMPTY
    }

    public BoardType type;


    //Default state of board: empty
    public BoardSpace(){
        this(BoardType.EMPTY);
    }

    public BoardSpace(BoardType type){
        this.type = type;
    }

    @Override
    public String toString() {
        String _name = "";
        switch (type){
            case EMPTY:
                _name = "■ ";
                break;
            case BLACK:
                _name = "● ";
                break;
            case WHITE:
                _name = "◌ ";
                break;

        }

        return _name;
    }
}
