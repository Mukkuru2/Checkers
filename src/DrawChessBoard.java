import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class DrawChessBoard extends JPanel {

    public DrawChessBoard() {
        setBackground(Color.CYAN);
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

        //Draw the selected piece

        int posY = getWidth() * Board.currentSelectionY / (board.length) + 15;
        int posX = getHeight() * Board.currentSelectionX / (board[0].length) + 15;

        if (!(board[Board.currentSelectionY][Board.currentSelectionX].type == BoardSpace.BoardType.EMPTY)){
            g.setColor(Color.CYAN);
            g.fillOval(posX, posY, 20, 20);
        } else if (!(board[Board.currentSelectionY + 1][Board.currentSelectionX].type == BoardSpace.BoardType.EMPTY)){
            g.setColor(Color.CYAN);
            g.fillOval(posX, posY + 50, 20, 20);
        }
        else {
            Boolean evenOrOdd = Board.currentSelectionY % 2 == 0 ? Board.currentSelectionX % 2 == 0 : Board.currentSelectionX % 2 == 1;
            g.setColor(Color.ORANGE);
            posY += evenOrOdd ? 0 : 50;
            g.fillRect(posX, posY, 20, 20);
        }
    }
}
