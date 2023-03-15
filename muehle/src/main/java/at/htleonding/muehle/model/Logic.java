package at.htleonding.muehle.model;

/*
* Muehle Logic:
* Description: Implements the game logic, the moves a piece can make
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Logic {
    private static final Position[] directions = {
            new Position(0,0,1),
            new Position(0,0,-1),
            new Position(0,1,0),
            new Position(0,-1,0),
            new Position(-1,0,0),
            new Position(1,0,0)};

    /**
     * Checks the possible moves the piece could make from that position
     *
     * @param game      the current game
     * @param position  the position where we want to know possible moves from
     * @return A list of the possible positions
     */
    public static List<Position> getMoves(Muehle game, Position position) {
        List<Position> possiblePositions = new ArrayList<Position>();
        final int boardMax = 2;
        final int boardMin = 0;

        for (Position vec : directions) {
            // Checks:
            //  * if it changes it's Z value which would jump to another 3x3 board which
            //      is only possible from the middle fields and not the corner ones
            if (vec.getZ() != 0 && (position.getY() == 1 && position.getX() != 1 || position.getY() != 1 && position.getX() == 1))
                continue;

            Position cur = position.plus(vec);

            // Checks:
            //  * if we get outside the board bounds by adding a vector to our position
            //  * if it tries to go to the middle which is not possible
            //  * if the position is already taken
            if (cur.getZ() < boardMin || cur.getZ() > boardMax
                    || cur.getX() < boardMin || cur.getX() > boardMax
                    || cur.getY() < boardMin || cur.getY() > boardMax
                    || cur.getX() == 1 && cur.getY() == 1
                    || game.getValueAt(cur) != 0)
                continue;

            possiblePositions.add(cur);
        }

        return possiblePositions;
    }

    /**
     * Checks if a move from Position A -> to Position B would activate a Muehle
     *
     * @param game      the current game
     * @param from      the position where we currently are
     * @param to        the position where we would be
     * @return boolean  if the move from A -> to B activates a Muehle
     */
    public static boolean activatesMuehle(Muehle game, Position from, Position to) {
        if (game.getValueAt(from) == 0 || !Logic.getMoves(game, from).contains(to)) {
            return false;
        }

        System.out.println();

        return true;
    }
}
