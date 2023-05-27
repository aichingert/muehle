package at.htlleonding.mill.controller;

import at.htlleonding.mill.view.GameBoard;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;

public class ReplayController {

    public GameBoard gameBoard;

    @FXML
    private void initialize() {
        gameBoard.drawIntersection(10,10, Color.GRAY, 9);
    }
}
