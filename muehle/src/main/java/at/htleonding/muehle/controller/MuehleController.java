package at.htleonding.muehle.controller;

import at.htleonding.muehle.model.Logic;
import at.htleonding.muehle.model.Muehle;
import at.htleonding.muehle.model.Position;
import at.htleonding.muehle.view.GameBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MuehleController {
    private static final double OFFSET = 10.0;

    int currentColor;
    Muehle game;

    @FXML
    private TextField textFieldX;
    @FXML
    private TextField textFieldY;
    @FXML
    private TextField textFieldZ;
    @FXML
    private GameBoard gameBoard;

    private Circle currentlySelected;

    @FXML
    private void initialize() {
        this.game = new Muehle(null, null);
        this.currentlySelected = null;
        this.currentColor = 1;
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

    public void methode1(MouseEvent mouseEvent) {
        if (this.currentlySelected != null) {

            return;
        }

        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        this.currentlySelected = this.gameBoard.getChildren()
                .stream()
                .filter(e -> e.getClass().equals(Circle.class))
                .map(c -> (Circle)c)
                .filter(c ->
                        c.getCenterX() + OFFSET > x && c.getCenterX() - OFFSET < x
                        && c.getCenterY() + OFFSET > y && c.getCenterY() - OFFSET < y)
                .findFirst().orElse(null);

    }
}