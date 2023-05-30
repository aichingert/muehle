package at.htlleonding.mill.controller;

import at.htlleonding.mill.bot.BruteForce;
import at.htlleonding.mill.model.GameState;
import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.Player;
import at.htlleonding.mill.model.helper.Logic;
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
    private boolean isHighlighted;

    @FXML
    private void initialize() {
        boolean playerOneIsWhite = Math.random() > 0.5;
        this.game = new Mill(new Player(playerOneIsWhite ? 1 : 2), new Player(playerOneIsWhite ? 2 : 1));
        this.currentlySelected = null;
        this.isHighlighted = false;
    }

    @FXML
    private void playerInputEvent(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        boolean isTurnToSwitch = false;

        switch (game.getGameState()) {
            case SET -> isTurnToSwitch = setPieceAtSelectedLocation(x, y);
            case MOVE -> {
                if (this.currentlySelected != null) {
                    if (moveSelectedPieceToNextPositionOrDropIt(x, y)) {
                        game.switchTurn();
                    }
                    return;
                }

                this.currentlySelected = gameBoard.getPieceFromSelectedCoordinates(x, y, game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

                if (this.currentlySelected != null) {
                    this.currentlySelected.setFill(Color.RED);
                }
            }
            case JUMP -> {

            }
            case TAKE -> {
                game.setGameState(GameState.SET);
            }
        }

        if (isTurnToSwitch) {
            game.switchTurn();
        }
    }

    private boolean setPieceAtSelectedLocation(double x, double y) {
        boolean isTurnToSwitch = false;

        if (!gameBoard.containsCoordinate(x, y)) {
            return false;
        }
        Position pos = gameBoard.convertCoordinateToPosition(x, y);

        if (game.setPiece(game.getCurrentPlayerColor(), pos)) {
            drawCircleAtPos(pos);
            if (Logic.activatesMill(game, null, pos)) {
                highlightTakeablePieces();
                game.setGameState(GameState.TAKE);
            } else {
                isTurnToSwitch = true;
                game.updateGameState();
            }
        }

        return isTurnToSwitch;
    }

    private boolean moveSelectedPieceToNextPositionOrDropIt(double x, double y) {
        this.currentlySelected.setFill(game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

        if (!gameBoard.containsCoordinate(x, y)) {
            this.currentlySelected = null;
            return false;
        }

        Position from = gameBoard.convertCoordinateToPosition(this.currentlySelected.getCenterX(), this.currentlySelected.getCenterY());
        Position to = gameBoard.convertCoordinateToPosition(x, y);

        if (!game.movePiece(game.getCurrentPlayerColor(), from, to)) {
            this.currentlySelected = null;
            return false;
        }

        gameBoard.getChildren().remove(this.currentlySelected);
        this.currentlySelected = null;
        drawCircleAtPos(to);
        return true;
    }

    private void highlightTakeablePieces() {
        Logic.getTakeablePieces(game, game.getCurrentPlayerColor() == 1 ? 2 : 1);
    }

    private void drawCircleAtPos(Position pos) {
        double boardSize = Math.min(gameBoard.getWidth(), gameBoard.getHeight()) - 2 * 50;
        double aSixth = boardSize / 6;

        double x = 50 + pos.getX() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth;
        double y = 50 + pos.getY() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth;

        if (game.getCurrentPlayerColor() == 1) {
            gameBoard.drawIntersection(x, y, Color.WHITE, 9);
        }
        else {
            gameBoard.drawIntersection(x, y, Color.GRAY, 9);
        }
    }
}