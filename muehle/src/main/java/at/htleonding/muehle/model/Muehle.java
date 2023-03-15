package at.htleonding.muehle.model;

public class Muehle {
    public final int MAX_PIECES = 9;
    public final int BOARD_SIZE = 3;
    private final int[][][] board;

    public Muehle() {
        board = new int[BOARD_SIZE][BOARD_SIZE][BOARD_SIZE];
    }

    public boolean movePiece(MoveType action, int color, int x, int y, int z) {
        if (action == MoveType.START_PHASE) {
            if (board[z][y][x] != 0) {
                return false;
            }

            board[z][y][x] = color;
            return true;
        }

        return true;
    }

    public int getValueAt(Position position) {
        return this.board[position.getZ()][position.getY()][position.getX()];
    }
}
