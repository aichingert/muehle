package at.htleonding.muehle.model;

import javafx.geometry.Pos;

import java.util.List;
import java.util.Scanner;

public class Muehle {
    public final int MAX_PIECES = 9;
    public final int BOARD_SIZE = 3;
    private final int[][][] board;

    private Player p1;
    private Player p2;

    public Muehle(Player p1, Player p2) {
        this.p1 = p1;
        this.p2 = p2;
        this.board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
    }

    public boolean setPiece(int color, Position position) {
        if (color == 1 && this.p1.getPieces() == 9 || color == 2 && p2.getPieces() == 9 || color != 1 && color != 2) {
            return false;
        }

        if ((this.board[position.getZ()][position.getY()][position.getX()] != 0) || (position.getX() == 1 && position.getY() == 1)) {
            return false;
        }

        this.board[position.getZ()][position.getY()][position.getX()] = color;

        switch (color) {
            case 1 -> p1.sP();
            case 2 -> p2.sP();
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

    public int getValueAt(Position position) {
        return this.board[position.getZ()][position.getY()][position.getX()];
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(Player p1) {
        this.p1 = p1;
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(Player p2) {
        this.p2 = p2;
    }
}
