package at.htlleonding.mill.model.helper;

/*
* Mill Logic:
* Description: Implements the game logic, the moves a piece can make
 */

import at.htlleonding.mill.model.GameState;
import at.htlleonding.mill.model.Mill;

import java.sql.SQLOutput;
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

        if (game.getGameState() != GameState.SET && (game.getValueAt(from) == 0 || !Logic.getMoves(game, from).contains(to))) {
            return false;
        }

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
        int[] rows;
        int[] cols;

        for (int z = 0; z < game.BOARD_SIZE; z++) {
            rows = new int[game.BOARD_SIZE];
            cols = new int[game.BOARD_SIZE];

            for (int x = 0; x < game.BOARD_SIZE; x++) {
                for (int y = 0; y < game.BOARD_SIZE; y++) {
                    if (game.getValueAt(new Position(x, y, z)) == oppositeColor) {
                        if (y != 1)
                            rows[y] += 1;
                        if (x != 1)
                            cols[x] += 1;
                    }
                }
            }

            for (int j = 0; j < game.BOARD_SIZE; j++) {
                System.out.println(rows[j]);
                if (rows[j] != game.BOARD_SIZE && cols[j] != game.BOARD_SIZE) {
                    for (int k = 0; k < game.BOARD_SIZE; k++) {
                        Position position = new Position(k, j, z);
                        if (game.getValueAt(position) == oppositeColor) {
                            takeablePositions.add(position);
                        }
                        position = new Position(j, k, z);
                        if (game.getValueAt(position) == oppositeColor) {
                            takeablePositions.add(position);
                        }
                    }
                } else if (rows[j] != game.BOARD_SIZE) {
                    for (int k = 0; k < game.BOARD_SIZE; k++) {
                        takeablePositions.add(new Position(k, j, z));
                    }
                } else if (cols[j] != game.BOARD_SIZE) {
                    for (int k = 0; k < game.BOARD_SIZE; k++) {
                        takeablePositions.add(new Position(j, k, z));
                    }
                }
            }
        }

        rows = new int[]{0, 0};
        cols = new int[]{0,0};

        for (int z = 0; z < game.BOARD_SIZE; z++) {
            if (game.getValueAt(new Position(2, 1, z)) == oppositeColor) {
                rows[1] += 1;
            }
            if (game.getValueAt(new Position(0, 1, z)) == oppositeColor) {
                rows[0] += 1;
            }
            if (game.getValueAt(new Position(1, 2, z)) == oppositeColor) {
                cols[1] += 1;
            }
            if (game.getValueAt(new Position(1, 0, z)) == oppositeColor) {
                cols[0] += 1;
            }
        }

        for (int i = 0; i < 2; i++) {
            if (rows[i] == game.BOARD_SIZE && cols[i] == game.BOARD_SIZE) {
                for (int z = 0; z < game.BOARD_SIZE; z++) {
                    takeablePositions.add(new Position(i == 1 ? 2 : 0, 1, z));
                    takeablePositions.add(new Position(1, i == 1 ? 2 : 0, z));
                }
            } else if (rows[i] == game.BOARD_SIZE) {
                for (int z = 0; z < game.BOARD_SIZE; z++) {
                    takeablePositions.add(new Position(i == 1 ? 2 : 0, 1, z));
                }
            } else if (cols[i] == game.BOARD_SIZE) {
                for (int z = 0; z < game.BOARD_SIZE; z++) {
                    takeablePositions.add(new Position(1, i == 1 ? 2 : 0, z));
                }
            }
        }

        System.out.println("Hello");
        for (Position p : takeablePositions) {
            System.out.println(p);
        }

        return takeablePositions;
    }
}