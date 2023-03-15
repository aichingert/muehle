package at.htleonding.muehle.model;

public class Muehle {
    private final int[][] board;
    private final int MAX_PIECES = 9;

    public Muehle() {
        board = new int[3][3];
    }

    public boolean movePiece(MoveType action, int color, int x, int y) {
        if (action == MoveType.START_PHASE) {
            if (board[y][x] != 0) {
                return false;
            }

            board[y][x] = color;
            return true;
        }

        return true;
    }

    public int getValueAt(Position position) {
        return this.board[position.getY()][position.getX()];
    }
}
