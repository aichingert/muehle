package at.htlleonding.mill.model.helper;

/*
* Mill Logic:
* Description: Implements the game logic, the moves a piece can make
 */

import at.htlleonding.mill.model.GameState;
import at.htlleonding.mill.model.Mill;

import java.util.ArrayList;
import java.util.List;

public class Logic {
    public static final Position[] directions = {
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
    public static List<Position> getMoves(Mill game, Position position) {
        List<Position> possiblePositions = new ArrayList<>();

        if (game.getGameState().equals(GameState.JUMP) || game.getGameState().equals(GameState.SET)) {
            for (int i = 0; i < game.BOARD_SIZE; i++) {
                for (int j = 0; j < game.BOARD_SIZE; j++) {
                    for (int k = 0; k < game.BOARD_SIZE; k++) {
                        Position pos = new Position(i, j, k);

                        if (!(i == 1 && j == 1) && game.getValueAt(pos) == 0) {
                            possiblePositions.add(pos);
                        }
                    }
                }
            }

            return possiblePositions;
        }

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
     * Checks if a move from Position A -> to Position B would activate a mill
     *
     * @param game      the current game
     * @param from      the position where we currently are
     * @param to        the position where we would be
     * @return true if the move activates a mill
     */
    public static boolean activatesMill(Mill game, Position from, Position to) {
        int color = game.getCurrentPlayerColor();
        boolean[] doesActivate = {true,true,true};

        // If we are able to switch the dimension then check if we get a mill there
        if (isAbleToSwitchDimensions(to)) {
            for (int i = 0; i < game.BOARD_SIZE; i++) {
                if (game.getValueAt(new Position(to.getX(), to.getY(), i)) != color) {
                    doesActivate[0] = false;
                }
            }
        } else {
            doesActivate[0] = false;
        }

        // Check for default Y and X if we get a mill
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

    /**
     * @param game
     * @return
     */
    public static List<Position> getTakeablePieces(Mill game, int oppositeColor) {
        List<Position> takeablePositions = new ArrayList<>();
        
        for (int z = 0; z < game.BOARD_SIZE; z++) {
            for (int y = 0; y < game.BOARD_SIZE; y++) {
                for (int x = 0; x < game.BOARD_SIZE; x++) {
                    if ((y == 1 && x == 1) || game.getValueAt(new Position(x, y, z)) != oppositeColor)
                        continue;
                    
                    if (x == 1) {
                        if (game.getValueAt(new Position(0, y, z)) == oppositeColor && game.getValueAt(new Position(2, y, z)) == oppositeColor)
                            continue;

                        isMill(game, oppositeColor, takeablePositions, z, y, x);
                    } else if (y == 1) {
                        if (game.getValueAt(new Position(x, 0, z)) == oppositeColor && game.getValueAt(new Position(x, 2, z)) == oppositeColor)
                            continue;

                        isMill(game, oppositeColor, takeablePositions, z, y, x);
                    } else {
                        boolean row = true;
                        boolean col = true;

                        for (int i = 0; i < game.BOARD_SIZE; i++) {
                            Position r = new Position(i, y, z);
                            Position c = new Position(x, i, z);

                            if (game.getValueAt(r) != oppositeColor)
                                row = false;
                            if (game.getValueAt(c) != oppositeColor)
                                col = false;
                        }

                        if (!(row || col))
                            takeablePositions.add(new Position(x, y, z));
                    }
                }
            }
        }

        return takeablePositions;
    }

    private static void isMill(Mill game, int oppositeColor, List<Position> takeablePositions, int z, int y, int x) {
        for (int i = 0; i < game.BOARD_SIZE; i++) {
            Position position = new Position(x, y, i);

            if (game.getValueAt(position) != oppositeColor) {
                takeablePositions.add(new Position(x, y, z));
                break;
            }
        }
    }
}