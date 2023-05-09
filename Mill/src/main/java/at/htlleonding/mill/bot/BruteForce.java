package at.htlleonding.mill.bot;

import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.helper.Logic;
import at.htlleonding.mill.model.helper.Position;

import java.util.stream.IntStream;

import static at.htlleonding.mill.model.helper.Logic.directions;

public class BruteForce {
    public static Position nextMove(int depth, Mill game, Position cur) {
        return calculate(depth, 1, game, cur);
    }

    private static Position calculate(int depth, int curEval, Mill game, Position cur) {
        /* Idea:
        * 1. Check if depth equals zero, if it does then evaluate Board
        *       for the current player and return an evaluation
        *       continue with step 2.
        * 2. Execute every Move with a copy of the board
        * 4. call the function with the given copied board

         */
        Mill[] deepGames = IntStream.range(0, directions.length).mapToObj(i -> game.copy()).toArray(Mill[]::new);

        for (Position pos : Logic.getMoves(game, cur)) {
            System.out.println(pos);
        }

        return new Position(0,0,0);
    }
}