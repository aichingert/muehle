package at.htlleonding.mill.model.helper;

import at.htlleonding.mill.repositories.ReplayRepository;
import at.htlleonding.mill.model.Replay;

import java.util.Comparator;
import java.util.List;

public class CurrentReplay {
    private Long gameId;
    private List<Replay> moves;
    private int counter;
    private static CurrentReplay instance = null;

    private CurrentReplay() {
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

        ReplayRepository replayRepository = new ReplayRepository();
        this.moves = replayRepository.getAllMovesFromGame(this.gameId);
        this.moves.sort(Comparator.comparingLong(Replay::getNthMove));
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
        this.counter = Math.max(this.counter - 1, 0);
        return this.moves.get(this.counter);
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
        this.counter = 0;
    }

    public int getCounter() {
        return this.counter;
    }
}