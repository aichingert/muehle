package at.htlleonding.mill.model;

public class Replay {
    private Long nthMove;
    private Long gameId;
    private Move move;

    public Replay(Long nthMove, Long gameId, Move move) {
        this.nthMove = nthMove;
        this.gameId = gameId;
        this.move = move;
    }

    public Long getNthMove() {
        return nthMove;
    }
    public Long getGameId() {
        return gameId;
    }
    public Move getMove() {
        return move;
    }
    public void setMove(Move move) {
        this.move = move;
    }
}
