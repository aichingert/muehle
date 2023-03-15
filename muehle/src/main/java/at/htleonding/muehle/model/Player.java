package at.htleonding.muehle.model;

public class Player {
    private int pieces;
    private int color;
    private Muehle game;

    public Player(Muehle game, int color) {
        this.pieces = 9;
        this.game = game;
        this.color = color;
    }

    public boolean setPiece(int x, int y) {
        if (pieces == 0) {
            return false;
        }



        return true;
    }
}
