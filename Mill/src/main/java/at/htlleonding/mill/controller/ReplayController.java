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
    private Color currentColor;

    @FXML
    private void initialize() {
        currentColor = Color.WHITE;
        CurrentReplay.getInstance().fillMoves();
    }

    public void nextMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getNext();

        if (replay == null) {
            return;
        }

        Move move = replay.getMove();
        System.out.println(move);

        // Tx and Ty == 0.0 => SET
        if (move.getTx() == 0.0 && move.getTy() == 0.0) {
            System.out.println("SET");
            gameBoard.drawIntersection(move.getFx(), move.getFy(), currentColor, 9);

        // Fx and Fy == -1 => TAKE
        } else if (move.getFx() == -1.0 && move.getFy() == -1.0) {
            System.out.println("TAKE");
            gameBoard.getChildren().remove(
                    gameBoard.getPieceFromSelectedCoordinates(move.getTx(), move.getTy(), currentColor)
            );
            currentColor = inverseCurrentColor();

        // Normal move phase
        } else {
            System.out.println("NORMAL");
            gameBoard.getChildren().remove(
                    gameBoard.getPieceFromSelectedCoordinates(move.getFx(), move.getFy(),currentColor)
            );
            gameBoard.drawIntersection(move.getTx(), move.getTy(), currentColor, 9);
        }

        currentColor = inverseCurrentColor();
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

    private Color inverseCurrentColor() {
        return currentColor == Color.WHITE ? Color.GRAY : Color.WHITE;
    }
}