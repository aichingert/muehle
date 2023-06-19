package at.htlleonding.mill.bot;

import at.htlleonding.mill.model.Difficulties;
import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.helper.Logic;
import at.htlleonding.mill.model.helper.Position;

import java.util.List;
import java.util.stream.IntStream;

import static at.htlleonding.mill.model.helper.Logic.directions;

public class BruteForce {
    private Difficulties difficulty;
    private static BruteForce instance = null;

    private BruteForce() {
        difficulty = Difficulties.EASY;
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

    public static Position[] nextMove(int depth, Mill game, int color) {
        /* Idea:
         * 1. Check if depth equals zero, if it does then evaluate Board
         *       for the current player and return an evaluation
         *       continue with step 2.
         * 2. Execute every Move with a copy of the board
         * 4. call the function with the given copied board
         */
        Position[] next = null;
        int evaluation = Integer.MIN_VALUE;

        for (int z = 0; z < game.BOARD_SIZE; z++) {
            for (int y = 0; y < game.BOARD_SIZE; y++) {
                for (int x = 0; x < game.BOARD_SIZE; x++) {
                    Position cur = new Position(x,y,z);

                    if (game.getValueAt(cur) != color) {
                        continue;
                    }

                    for (Position to : Logic.getMoves(game, cur)) {
                        Mill cp = game.copy();
                        cp.movePiece(color, cur, to);

                        int res = calculate(depth, cp, color);

                        if (res < evaluation) {
                            continue;
                        }

                        evaluation = res;
                        next = new Position[]{cur, to};
                    }
                }
            }
        }

        return next;
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