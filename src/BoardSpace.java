public class BoardSpace {

    //possible boardstates
    public enum BoardType{
        WHITE,
        BLACK,
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
}
