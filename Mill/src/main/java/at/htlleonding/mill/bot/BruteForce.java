package at.htlleonding.mill.bot;

import at.htlleonding.mill.model.Difficulties;
import at.htlleonding.mill.model.GameState;
import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.helper.Logic;
import at.htlleonding.mill.model.helper.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static at.htlleonding.mill.model.helper.Logic.directions;

public class BruteForce {
    private Difficulties difficulty;
    private static BruteForce instance = null;

    private BruteForce() {
        difficulty = Difficulties.CRAZY;
    }

    public static BruteForce getInstance() {
        if (instance == null) {
            instance = new BruteForce();
        }

        return instance;
    }

    public void setDifficulty(Difficulties difficulty) {
        this.difficulty = difficulty;
    }

    public Position[] nextMove(Mill game, int color) {
        Position[] next = null;

        switch (this.difficulty) {
            case CRAZY -> next = getNextCrazyMove(game, color);
            case EASY -> next = null;
            case MEDIUM -> next = null;
            case HARD -> next = null;
        }

        return next;
    }

    public Position nextTake(Mill game, int color) {
        Position next = null;

        switch (this.difficulty) {
            case CRAZY -> next = getNextCrazyTake(game, color);
            case EASY -> next = null;
            case MEDIUM -> next = null;
            case HARD -> next = null;
        }

        return next;
    }

    private static Position[] getNextCrazyMove(Mill game, int color) {
        Random index = new Random();
        List<Position> possible = null;

        if (game.getGameState().equals(GameState.SET)) {
            possible = Logic.getMoves(game, null);
            return new Position[]{possible.get(index.nextInt(possible.size())), null};
        }

        List<Position> positions = new ArrayList<>();

        for (int z = 0; z < game.BOARD_SIZE; z++) {
            for (int y = 0; y < game.BOARD_SIZE; y++) {
                for (int x = 0; x < game.BOARD_SIZE; x++) {
                    Position cur = new Position(x,y,z);

                    if (game.getValueAt(cur) == color) {
                        positions.add(cur);
                    }
                }
            }
        }

        int nextIndex = index.nextInt(positions.size());
        Position from = null;

        while (possible == null || possible.isEmpty()) {
            from = positions.remove(nextIndex);
            possible = Logic.getMoves(game, from);
        }

        Position to   = possible.get(index.nextInt(possible.size()));

        return new Position[]{from, to};
    }

    private static Position getNextCrazyTake(Mill game, int color) {
        Random index = new Random();
        List<Position> positions = Logic.getTakeablePieces(game, color);
        return positions.get(index.nextInt(positions.size()));
    }

    private static int calculate(int depth, Mill game, int color) {
        if (depth == 0) {
            // Eval here
            return 0;
        }

        int avg = 0;

        for (int z = 0; z < game.BOARD_SIZE; z++) {
            for (int y = 0; y < game.BOARD_SIZE; y++) {
                for (int x = 0; x < game.BOARD_SIZE; x++) {
                    Position cur = new Position(x,y,z);

                    if (game.getValueAt(cur) != color) {
                        continue;
                    }

                    List<Position> moves = Logic.getMoves(game, cur);
                    for (int i = 0; i < moves.size(); i++) {
                        Mill cp = game.copy();
                        cp.movePiece(color, cur, moves.get(i));

                        avg += calculate(depth, cp, color) / (i + 1);
                    }
                }
            }
        }

        return avg;
    }
}