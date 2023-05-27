package at.htlleonding.mill.model.helper;

import at.htlleonding.mill.db.ReplayRepository;
import at.htlleonding.mill.model.Replay;

import java.util.Comparator;
import java.util.List;

public class CurrentReplay {
    private Long gameId;
    private List<Replay> moves;
    private int counter;
    private int currentPlayerColor;
    private static CurrentReplay instance = null;

    private CurrentReplay() {
        gameId = null;
        moves = null;
        counter = 0;
        currentPlayerColor = -1;
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
        if (this.counter != this.moves.size()-1) {
            this.currentPlayerColor += 1;
        }

        Replay replay = this.moves.get(this.counter);
        this.counter = Math.min(this.counter + 1, this.moves.size()-1);
        return replay;
    }

    public Replay getPrevious() {
        if (this.counter != 0 || this.currentPlayerColor == -1) {
            this.currentPlayerColor -= 1;
        }

        Replay replay = this.moves.get(this.counter);
        this.counter = Math.max(this.counter - 1, 0);
        return replay;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
        this.counter = 0;
        this.currentPlayerColor = -1;
    }

    public int getCounter() {
        return this.counter;
    }

    public int getCurrentPlayerColor() {
        return currentPlayerColor;
    }
}
