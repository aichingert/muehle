package at.htleonding.muehle.model;

import javafx.geometry.Pos;

import java.util.List;

public class Muehle {
    public final int MAX_PIECES = 9;
    public final int BOARD_SIZE = 3;
    private final int[][][] board;

    public Muehle() {
        board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
    }

    public boolean setPiece(int color, Position position) {
        if ((board[position.getZ()][position.getY()][position.getX()] != 0) || (position.getX() == 1 && position.getY() == 1)) {
            return false;
        }

        board[position.getZ()][position.getY()][position.getX()] = color;
        return true;
    }

    public boolean movePiece(int color, Position from, Position to) {
        if (board[from.getZ()][from.getY()][from.getX()] != color || from.equals(to)) {
            return false;
        }

        // 0 0 0
        // 0   0
        // 0 0 0

        List<Position> possibleMoves = Logic.getMoves(this, from);

        if (!possibleMoves.contains(to)) {
            return false;
        }

        board[from.getZ()][from.getY()][from.getX()] = 0;
        board[to.getZ()][to.getY()][to.getX()] = color;
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                for (int k = 0; k < BOARD_SIZE; k++) {
                    System.out.printf("%d", board[i][j][k]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Muehle m = new Muehle();
        boolean result = m.setPiece(1, new Position(0,2,1));
        System.out.println(result);
        m.printBoard();
        System.out.println("=========");
        m.movePiece(1, new Position(-1,2,1), new Position(-1,2,1));
        m.printBoard();
    }

    public int getValueAt(Position position) {
        return this.board[position.getZ()][position.getY()][position.getX()];
    }
}
