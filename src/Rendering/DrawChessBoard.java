import BoardStates.Board;
import BoardStates.BoardSpace;
import Main.App;

import javax.swing.*;
import java.awt.*;

public class DrawChessBoard extends JPanel {

    public DrawChessBoard() {
        setBackground(Color.WHITE);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        BoardSpace[][] board;
        //This function seems to somehow run before the board exists, *occasionally*... which produces an error which doesn't seem to
        //stop it from working. Since the error is annoying, there is a try/catch block here, trying to fix it.

        try {
            board = App.GetBoard().board;
        } catch(Exception e) {

            //Sleep for 10 ms
            try {
                Thread.sleep(10);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            //Try again
            board = App.GetBoard().board;

        }

        //Paintbrush object to be able to call functions within this function without having to pass g, the board, the width and the height every time.
        Paintbrush brush = new Paintbrush(g, board, getWidth(), getHeight());

        brush.drawBoard();
        brush.drawPieces();
        brush.drawSelection();
        brush.drawMovementOptions();

    }
}

class Paintbrush {
    private Graphics g;
    private BoardSpace[][] board;
    private int width, height;

    Paintbrush(Graphics g, BoardSpace[][] board, int width, int height) {
        this.g = g;
        this.board = board;
        this.width = width;
        this.height = height;
    }

    void drawBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Boolean evenOrOdd = i % 2 == 0 ? j % 2 == 0 : j % 2 == 1;
                Color _c = evenOrOdd ? Color.BLACK : Color.WHITE;

                int posY = width * i / (board.length);
                int posX = height * j / (board.length);

                g.setColor(_c);
                g.fillRect(posX, posY, width / (board.length), height / (board.length));
            }
        }
    }

    void drawPieces() {

        int pieceDiameter = 20;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {

                int posY = width * i / (board.length) + (width / board.length / 2 - pieceDiameter / 2);
                int posX = height * j / (board.length) + (width / board.length / 2 - pieceDiameter / 2);


                //Detemine what happens based on board space and it's type
                switch (board[i][j].type) {
                    case EMPTY:
                        break;
                    case WHITE:
                        g.setColor(Color.WHITE);
                        g.fillOval(posX, posY, pieceDiameter, pieceDiameter);
                        break;
                    case BLACK:
                        g.setColor(new Color(120, 0, 0));
                        g.fillOval(posX, posY, pieceDiameter, pieceDiameter);
                        break;
                }
            }
        }


    }

    void drawSelection() {
        int pieceDiameter = 20;
        //Draw the selected piece
        int posY = width * Board.currentSelectionY / (board.length) + (width / board.length / 2 - pieceDiameter / 2);
        int posX = height * Board.currentSelectionX / (board.length) + (width / board.length / 2 - pieceDiameter / 2);

        if (!(board[Board.currentSelectionY][Board.currentSelectionX].type == BoardSpace.BoardType.EMPTY)) {
            Color _c = (board[Board.currentSelectionY][Board.currentSelectionX].type == BoardSpace.BoardType.WHITE) ? Color.CYAN : Color.GREEN;
            g.setColor(_c);
            g.fillOval(posX, posY, pieceDiameter, pieceDiameter);
        } else {
            g.setColor(Color.ORANGE);
            g.fillRect(posX, posY, pieceDiameter, pieceDiameter);
        }

    }

    void drawMovementOptions() {
        int pieceDiameter = 10;

        if (Board.currentMoveSelectionY == -1 || Board.currentMoveSelectionX == -1)
            return;
        int posY = width * Board.currentMoveSelectionY / (board.length) + (width / board.length / 2 - pieceDiameter / 2);
        int posX = height * Board.currentMoveSelectionX / (board.length) + (width / board.length / 2 - pieceDiameter / 2);

        BoardSpace.BoardType currentSpace = board[Board.currentMoveSelectionY][Board.currentMoveSelectionX].type;

        if (currentSpace == BoardSpace.BoardType.EMPTY) {
            g.setColor(Color.ORANGE);
            g.fillRect(posX, posY, pieceDiameter, pieceDiameter);

        } else {
            g.setColor(Color.RED);
            g.fillOval(posX, posY, pieceDiameter, pieceDiameter);
        }

    }

}