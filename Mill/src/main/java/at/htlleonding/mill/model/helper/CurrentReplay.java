package at.htlleonding.mill.model.helper;

import at.htlleonding.mill.repositories.ReplayRepository;
import at.htlleonding.mill.model.Replay;

import java.util.Comparator;
import java.util.List;

public class CurrentReplay {
    private Long gameId;
    private List<Replay> moves;
    private final ReplayRepository replayRepository;
    private int counter;
    private static CurrentReplay instance = null;

    private CurrentReplay() {
        replayRepository = new ReplayRepository();
        gameId = null;
        moves = null;
        counter = 0;
    }

    public static CurrentReplay getInstance() {
        if (instance == null) {
            instance = new CurrentReplay();
        }

        return instance;
    }

    public void fillMoves() {
        if (this.gameId == null) {
            return;
        }

        this.moves = this.replayRepository.getAllMovesFromGame(this.gameId);
        System.out.println(this.moves.size());
    }

    public Replay getNext() {
        Replay replay = null;

        if (this.counter < this.moves.size()) {
            replay = this.moves.get(this.counter);
        }

        this.counter = Math.min(this.counter + 1, this.moves.size());
        return replay;
    }

    public Replay getPrevious() {
        boolean isAtBeginning = this.counter - 1 == -1;
        this.counter = Math.max(this.counter - 1, 0);
        return isAtBeginning ? null : this.moves.get(this.counter);
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
        this.counter = 0;
    }

    public Long getGameId() {
        return gameId;
    }

    public int getCounter() {
        return this.counter;
    }

    public List<Replay> getMoves() {
        return moves;
    }
}