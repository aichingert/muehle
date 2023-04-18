package at.htleonding.muehle.model;

import javafx.geometry.Pos;

import java.util.List;
import java.util.Scanner;

public class Muehle {
    public final int MAX_PIECES = 9;
    public final int BOARD_SIZE = 3;
    private final int[][][] board;
    private int piecesPlayer1;
    private int piecesPlayer2;

    public Muehle() {
        piecesPlayer1 = 0;
        piecesPlayer2 = 0;
        board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
    }

    public boolean setPiece(int color, Position position) {
        if (color == 1 && piecesPlayer1 == 9 || color == 2 && piecesPlayer2 == 9 || color != 1 && color != 2) {
            return false;
        }

        if ((board[position.getZ()][position.getY()][position.getX()] != 0) || (position.getX() == 1 && position.getY() == 1)) {
            return false;
        }

        board[position.getZ()][position.getY()][position.getX()] = color;

        switch (color) {
            case 1 -> piecesPlayer1++;
            case 2 -> piecesPlayer2++;
        }

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

        m.printBoard();
        Scanner in;
        boolean isPlayerOne = true;

        while(true) {
            System.out.println("SET PIECES:");
            System.out.println("Active Player: " + (isPlayerOne ? 1 : 2));

            System.out.print("X: ");
            in = new Scanner(System.in);
            int x = in.nextInt();

            System.out.print("Y: ");
            in = new Scanner(System.in);
            int y = in.nextInt();

            System.out.print("Z: ");
            in = new Scanner(System.in);
            int z = in.nextInt();

            boolean result = m.setPiece(isPlayerOne ? 1 : 2, new Position(x,y,z));

            if (result) {
                isPlayerOne = !isPlayerOne;
                m.printBoard();
            }

            if (m.piecesPlayer1 == 9 && m.piecesPlayer2 == 9) {
                break;
            }
        }
    }

    public int getValueAt(Position position) {
        return this.board[position.getZ()][position.getY()][position.getX()];
    }

    public int getPiecesPlayer1() {
        return piecesPlayer1;
    }

    public void setPiecesPlayer1(int piecesPlayer1) {
        this.piecesPlayer1 = piecesPlayer1;
    }

    public int getPiecesPlayer2() {
        return piecesPlayer2;
    }

    public void setPiecesPlayer2(int piecesPlayer2) {
        this.piecesPlayer2 = piecesPlayer2;
    }
}
