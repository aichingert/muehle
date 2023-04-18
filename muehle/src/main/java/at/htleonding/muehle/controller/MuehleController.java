package at.htleonding.muehle.controller;

import at.htleonding.muehle.view.GameBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MuehleController {
    @FXML
    private Label welcomeText;
    @FXML
    private GameBoard gameBoard;

    @FXML
    private void initialize() {
        gameBoard = new GameBoard();

    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}