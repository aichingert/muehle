package at.htleonding.muehle.model;

public class Muehle {
    public final int MAX_PIECES = 9;
    public final int BOARD_SIZE = 3;
    private final int[][][] board;

    public Muehle() {
        board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
    }

    public boolean movePiece(MoveType action, int color, Position position) {
        if (board[position.getZ()][position.getY()][position.getX()] != 0) {
            return false;
        }
        // 0 0 0
        // 0   0
        // 0 0 0

        if (action == MoveType.START_PHASE) {
            board[position.getZ()][position.getY()][position.getX()] = color;
            return true;
        }

        if (action == MoveType.UP) {
            //TODO:
        }

        return true;
    }

    public int getValueAt(Position position) {
        return this.board[position.getZ()][position.getY()][position.getX()];
    }
}
