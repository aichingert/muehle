package at.htleonding.muehle.model;

/*
* Muehle Logic:
* Description: Implements the game logic, the moves a piece can make
 */

import javafx.geometry.Pos;

import java.util.ArrayList;
import java.util.List;

public class Logic {
    private static final Position[] directions = {
            new Position(0,0,1),
            new Position(0,0,-1),
            new Position(0,1,0),
            new Position(0,-1,0),
            new Position(-1,0,0),
            new Position(1,0,0)
    };

    /**
     * Check if jump to another board is possible from the current position
     *
     * @param position  the position where we want to know if we can switch boards
     * @return if it is possible
     */
    public static boolean isAbleToSwitchDimensions(Position position) {
        return position.getY() == 1 && position.getX() != 1 || position.getY() != 1 && position.getX() == 1;
    }

    /**
     * Checks the possible moves the piece could make from that position
     *
     * @param game      the current game
     * @param position  the position where we want to know possible moves from
     * @return A list of the possible positions
     */
    public static List<Position> getMoves(Muehle game, Position position) {
        List<Position> possiblePositions = new ArrayList<>();
        final int boardMax = 2;
        final int boardMin = 0;

        for (Position vec : directions) {
            // If we try to switch dimensions we can only do that from the middle fields and not the corners
            if (vec.getZ() != 0 && !isAbleToSwitchDimensions(position))
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
     * @return true if the move activates a muehle
     */
    public static boolean activatesMuehle(Muehle game, Position from, Position to) {
        int color = game.getValueAt(from);
        boolean[] doesActivate = {true,true,true};

        if (game.getValueAt(from) == 0 || !Logic.getMoves(game, from).contains(to)) {
            return false;
        }

        // If we are able to switch the dimension then check if we get a muehle there
        if (isAbleToSwitchDimensions(to)) {
            for (int i = 0; i < game.BOARD_SIZE; i++) {
                if (game.getValueAt(new Position(to.getX(), to.getY(), i)) != color) {
                    doesActivate[0] = false;
                }
            }
        }

        // Check for default Y and X if we get a muehle
        for (int i = 0; i < game.BOARD_SIZE; i++) {
            if (game.getValueAt(new Position(to.getX(), i, to.getZ())) != color) {
                doesActivate[1] = false;
            }

            if (game.getValueAt(new Position(i, to.getY(), to.getZ())) != color) {
                doesActivate[2] = false;
            }
        }

        return doesActivate[0] || doesActivate[1] || doesActivate[2];
    }
}