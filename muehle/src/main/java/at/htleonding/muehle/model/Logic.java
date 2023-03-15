package at.htleonding.muehle.model;

/*
* Muehle Logic:
* Description: Implements the game logic, the moves a piece can make
 */

import java.util.ArrayList;
import java.util.List;

public class Logic {
    private static final Position[] directions = {
            new Position(1,0),
            new Position(0,1),
            new Position(-1, 0),
            new Position(0, -1)};

    // Will search for possible moves for the player
    public static List<Position> getMoves(Muehle game, Position position) {
        List<Position> possiblePositions = new ArrayList<Position>();

        for (Position vec : directions) {
            Position cur = position.plus(vec);

            if (cur.getX() == 1 && cur.getY() == 1 || game.getValueAt(cur) != 0)
                continue;

            possiblePositions.add(cur);
        }

        return possiblePositions;
    }

    // Will check if the current move will activate a muehle
    public static void activatesMuehle(/*board: depending, move: Move */) {}
}
