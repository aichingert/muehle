package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.Player;
import at.htlleonding.mill.model.helper.Position;
import at.htlleonding.mill.view.GameBoard;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class MillController {
    Mill game;

    @FXML
    private GameBoard gameBoard;
    @FXML
    private Label lblCurPlayer;

    private Circle currentlySelected;

    @FXML
    private void initialize() {
        boolean playerOneIsWhite = Math.random() > 0.5;
        this.game = new Mill(new Player(playerOneIsWhite ? 1 : 2), new Player(playerOneIsWhite ? 2 : 1));
        this.currentlySelected = null;
    }

    @FXML
    private void playerInputEvent(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        switch (game.getGameState()) {
            case SET -> {
                setPieceAtSelectedLocation(x, y);
            }
            case MOVE -> {
                if (this.currentlySelected != null) {
                    moveSelectedPieceToNextPositionOrDropIt(x, y);
                    return;
                }

                this.getPieceFromSelectedCoordinates(x, y);

                if (this.currentlySelected != null) {
                    this.currentlySelected.setFill(Color.RED);
                }
            }
            case JUMP -> {
            }
        }
    }

    private void setPieceAtSelectedLocation(double x, double y) {
        if (!gameBoard.containsCoordinate(x, y)) {
            return;
        }
        Position pos = gameBoard.convertCoordinateToPosition(x, y);

        if (game.setPiece(game.getCurrentPlayerColor(), pos)) {
            drawCircleAtPos(pos);
            game.switchTurn();
            game.updateGameState();
        }
    }

    private void moveSelectedPieceToNextPositionOrDropIt(double x, double y) {
        this.currentlySelected.setFill(game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

        if (!gameBoard.containsCoordinate(x, y)) {
            this.currentlySelected = null;
            return;
        }

        Position from = gameBoard.convertCoordinateToPosition(this.currentlySelected.getCenterX(), this.currentlySelected.getCenterY());
        Position to = gameBoard.convertCoordinateToPosition(x, y);

        if (!game.movePiece(game.getCurrentPlayerColor(), from, to)) {
            this.currentlySelected = null;
            return;
        }

        gameBoard.getChildren().remove(this.currentlySelected);
        this.currentlySelected = null;
        drawCircleAtPos(to);
        game.switchTurn();
    }

    private void getPieceFromSelectedCoordinates(double x, double y) {
        this.currentlySelected = this.gameBoard.getChildren().stream()
                .filter(e -> e.getClass().equals(Circle.class))
                .map(c -> (Circle)c)
                .filter(c -> checkIfCircleIsInBounds(c.getCenterX(), c.getCenterY(), c.getFill(), x, y))
                .findFirst().orElse(null);
    }

    private boolean checkIfCircleIsInBounds(double cX, double cY, Paint c, double x, double y) {
        return cX + GameBoard.OFFSET > x && cX - GameBoard.OFFSET < x
                && cY + GameBoard.OFFSET > y && cY - GameBoard.OFFSET < y
                && c.equals(game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);
    }

    private void drawCircleAtPos(Position pos) {
        double boardSize = Math.min(gameBoard.getWidth(), gameBoard.getHeight()) - 2 * 50;
        double aSixth = boardSize / 6;

        double x = 50 + pos.getX() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth;
        double y = 50 + pos.getY() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth;

        if (game.getCurrentPlayerColor() == 1) {
            drawIntersection(x, y, Color.WHITE);
        }
        else if (game.getCurrentPlayerColor() == 2) {
            drawIntersection(x, y, Color.GRAY);
        }
    }

    private void drawIntersection(double x, double y, Color color) {
        Circle intersection = new Circle(x, y, 9);
        intersection.setFill(color);
        intersection.setStroke(Color.BLACK);
        gameBoard.getChildren().add(intersection);
    }
}