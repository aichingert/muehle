package at.htleonding.muehle.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LogicTest {

    @Test
    void getMoves() {
        Muehle game = new Muehle();
        game.movePiece(MoveType.START_PHASE, 1, new Position(1,0,0));

        List<Position> possibleMoves = Logic.getMoves(game, new Position(1, 0, 0));

        assert(possibleMoves.size() == 3);
    }

    @Test
    void activatesMuehle() {
    }
}