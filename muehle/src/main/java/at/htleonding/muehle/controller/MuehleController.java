package at.htleonding.muehle.controller;

import at.htleonding.muehle.model.Muehle;
import at.htleonding.muehle.model.Position;
import at.htleonding.muehle.view.GameBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MuehleController {

    int currentColor;
    Muehle game;

    @FXML
    private TextField textFieldX;
    @FXML
    private TextField textFieldY;
    @FXML
    private TextField textFieldZ;
    @FXML
    private TextField welcomeText;
    @FXML
    private GameBoard gameBoard;

    @FXML
    private void initialize() {
        game = new Muehle();
        currentColor = 1;
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void onBtnSetPiece(ActionEvent actionEvent) {
        int x = Integer.parseInt(textFieldX.getText());
        int y = Integer.parseInt(textFieldY.getText());
        int z = Integer.parseInt(textFieldZ.getText());
        Position pos = new Position(x, y, z);
        System.out.println("onBtnSetPiece " + currentColor);

        if (game.setPiece(currentColor, pos)) {
            drawCircleAtPos(pos);
            currentColor = currentColor == 1 ? 2 : 1;
        }

    }

    private void drawCircleAtPos(Position pos) {
        double boardSize = Math.min(gameBoard.getWidth(), gameBoard.getHeight()) - 2 * 50;
        double aSixth = boardSize / 6;
        System.out.println("Jz: " + gameBoard.getWidth());
        System.out.println("Jz: " + gameBoard.getHeight());
        System.out.println("Jz: " + boardSize);

        double x = 50 + pos.getX() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth;
        double y = 50 + pos.getY() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth;

        if (currentColor == 1) {
            drawIntersection(x, y, Color.GRAY);
        }
        else if (currentColor == 2) {
            drawIntersection(x, y, Color.WHITE);
        }
    }

    private void drawIntersection(double x, double y, Color color) {
        Circle intersection = new Circle(x, y, 9);
        intersection.setFill(color);
        gameBoard.getChildren().add(intersection);
    }
}