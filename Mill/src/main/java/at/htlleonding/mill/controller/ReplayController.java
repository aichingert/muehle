package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.Move;
import at.htlleonding.mill.model.Player;
import at.htlleonding.mill.model.Replay;
import at.htlleonding.mill.model.helper.CurrentReplay;
import at.htlleonding.mill.view.GameBoard;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;

public class ReplayController {
    public GameBoard gameBoard;

    @FXML
    private void initialize() {
        System.out.println(CurrentReplay.getInstance().getGameId());
        CurrentReplay.getInstance().fillMoves();
    }

    public void nextMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getNext();

        System.out.println(replay);
        if (replay == null) {
            return;
        }

        Move move = replay.getMove();

        if (CurrentReplay.getInstance().getCounter() < 2 * Player.MAX_PIECES) {
            gameBoard.drawIntersection(
                    move.getFx(),
                    move.getFy(),
                    colorFromCounter(replay.getNthMove().intValue()),
                    9
            );
            return;
        }

        gameBoard.getChildren().remove(gameBoard.getPieceFromSelectedCoordinates(
                move.getFx(),
                move.getFy(),
                colorFromCounter(CurrentReplay.getInstance().getCounter())
        ));
        gameBoard.drawIntersection(move.getTx(), move.getTy(), colorFromCounter(replay.getNthMove().intValue()), 9);
    }

    public void previousMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getPrevious();

        if (replay == null) {
            return;
        }

        Move move = replay.getMove();

        if (CurrentReplay.getInstance().getCounter() < 2 * Player.MAX_PIECES) {
            gameBoard.getChildren().remove(gameBoard.getPieceFromSelectedCoordinates(
                    move.getTx(),
                    move.getTy(),
                    colorFromCounter(replay.getNthMove().intValue())
            ));
            return;
        }

        gameBoard.getChildren().remove(gameBoard.getPieceFromSelectedCoordinates(
                move.getTx(),
                move.getTy(),
                colorFromCounter(replay.getNthMove().intValue())
        ));
        gameBoard.drawIntersection(move.getFx(), move.getFy(), colorFromCounter(replay.getNthMove().intValue()), 9);
    }

    private Color colorFromCounter(int nth) {
        return nth % 2 == 0 ? Color.WHITE : Color.GRAY;
    }
}