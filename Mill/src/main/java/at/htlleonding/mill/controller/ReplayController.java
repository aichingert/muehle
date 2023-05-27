package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.Move;
import at.htlleonding.mill.model.Replay;
import at.htlleonding.mill.model.helper.CurrentReplay;
import at.htlleonding.mill.view.GameBoard;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.shape.Circle;

import java.sql.SQLOutput;

public class ReplayController {
    public GameBoard gameBoard;

    @FXML
    private void initialize() {
        CurrentReplay.getInstance().setGameId(1L);
        CurrentReplay.getInstance().fillMoves();
    }

    public void nextMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getNext();

        if (replay == null) {
            return;
        }

        Move move = replay.getMove();

        if (CurrentReplay.getInstance().getCounter() < 18) {
            gameBoard.drawIntersection(
                    move.getTx(),
                    move.getTy(),
                    colorFromCounter(CurrentReplay.getInstance().getCounter()),
                    9
            );
            return;
        }

        gameBoard.getChildren().remove(gameBoard.getPieceFromSelectedCoordinates(
                        move.getFx(),
                        move.getFy(),
                        colorFromCounter(CurrentReplay.getInstance().getCurrentPlayerColor())
                ));
        gameBoard.drawIntersection(move.getTx(), move.getTy(), colorFromCounter(CurrentReplay.getInstance().getCurrentPlayerColor()), 9);
    }

    public void previousMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getPrevious();

        if (replay == null) {
            return;
        }

        Move move = replay.getMove();

        if (CurrentReplay.getInstance().getCounter() < 18) {
            gameBoard.getChildren().remove(gameBoard.getPieceFromSelectedCoordinates(
                    move.getTx(),
                    move.getTy(),
                    colorFromCounter(CurrentReplay.getInstance().getCurrentPlayerColor())
            ));
            return;
        }

        gameBoard.getChildren().remove(gameBoard.getPieceFromSelectedCoordinates(
                move.getTx(),
                move.getTy(),
                colorFromCounter(CurrentReplay.getInstance().getCurrentPlayerColor())
        ));
        gameBoard.drawIntersection(move.getFx(), move.getFy(), colorFromCounter(CurrentReplay.getInstance().getCurrentPlayerColor()), 9);
    }

    private Color colorFromCounter(int counter) {
        return counter % 2 == 0 ? Color.WHITE : Color.GRAY;
    }
}
