package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.Player;
import at.htlleonding.mill.model.helper.Position;
import at.htlleonding.mill.view.GameBoard;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MillController {
    Mill game;

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
        boolean playerOneIsWhite = Math.random() > 0.5;
        this.game = new Mill(new Player(playerOneIsWhite ? 1 : 2), new Player(playerOneIsWhite ? 2 : 1));
        this.currentlySelected = null;
    }

    @FXML
    private void onBtnSetPiece(ActionEvent actionEvent) {
        int x = Integer.parseInt(textFieldX.getText());
        int y = Integer.parseInt(textFieldY.getText());
        int z = Integer.parseInt(textFieldZ.getText());
        Position pos = new Position(x, y, z);
        System.out.println("onBtnSetPiece " + game.getCurrentPlayerColor());

        if (game.setPiece(game.getCurrentPlayerColor(), pos)) {
            drawCircleAtPos(pos);
            game.switchTurn();
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
        gameBoard.getChildren().add(intersection);
    }

    public void playerInputEvent(MouseEvent mouseEvent) {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        if (this.currentlySelected != null) {
            moveSelectedPieceToNextPositionOrDropIt(x, y);
            return;
        }

        this.currentlySelected = this.gameBoard.getChildren()
                .stream()
                .filter(e -> e.getClass().equals(Circle.class))
                .map(c -> (Circle)c)
                .filter(c ->
                        c.getCenterX() + GameBoard.OFFSET > x && c.getCenterX() - GameBoard.OFFSET < x
                                && c.getCenterY() + GameBoard.OFFSET > y && c.getCenterY() - GameBoard.OFFSET < y
                                && (c.getFill().equals(Color.GRAY) || c.getFill().equals(Color.WHITE)))
                .findFirst().orElse(null);

        if (this.currentlySelected != null) {
            // TODO: Highlight the currentlySelected piece later after we implemented the setPiece event with the mouse
            //this.currentlySelected.setFill(Color.RED);
        }

        System.out.println(this.currentlySelected);
    }

    private void moveSelectedPieceToNextPositionOrDropIt(double x, double y) {
        if (!gameBoard.containsCoordinate(x, y)) {
            //this.currentlySelected.setFill(game.getCurrentPlayerColor() == 1 ? Color.GRAY : Color.WHITE);
            this.currentlySelected = null;
            return;
        }

        Position p = gameBoard.convertCoordinateToPosition(x,y);
        System.out.println(p);

        System.out.println("");
    }
}
