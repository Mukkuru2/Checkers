import java.awt.*;

public class DrawChessBoard extends Canvas {

    public DrawChessBoard() {
        setBackground(Color.CYAN);
        setSize(400, 400);
    }

    public void paint(Graphics g) {
        super.paint(g);
        BoardSpace[][] board = App.GetBoard().board;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                Boolean evenOrOdd = i % 2 == 0 ? j % 2 == 0 : j % 2 == 1;
                Color _c = evenOrOdd ? Color.BLACK : Color.WHITE;

                int posY = getWidth() * i / (board.length);
                int posX = getHeight() * j / (board[0].length);

                g.setColor(_c);
                g.fillRect(posX, posY, getWidth() / (board.length), getHeight() / (board[0].length));

                //Edit position so the oval appears in the center
                posX += 15;
                posY += 15;

                switch (board[i][j].type) {
                    case EMPTY:
                        break;
                    case WHITE:
                        g.setColor(Color.WHITE);
                        g.fillOval(posX, posY, 20, 20);
                        break;
                    case BLACK:
                        g.setColor(new Color(120, 0, 0));
                        g.fillOval(posX, posY, 20, 20);
                        break;
                    case WHITE_KING:
                        break;
                    case BLACK_KING:
                        break;

                }
            }
        }
    }

}
