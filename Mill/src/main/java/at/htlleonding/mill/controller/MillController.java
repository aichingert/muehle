package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.*;
import at.htlleonding.mill.model.helper.CurrentGame;
import at.htlleonding.mill.model.helper.Logic;
import at.htlleonding.mill.model.helper.Position;
import at.htlleonding.mill.repositories.GameRepository;
import at.htlleonding.mill.repositories.MoveRepository;
import at.htlleonding.mill.repositories.ReplayRepository;
import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.view.GameBoard;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static at.htlleonding.mill.App.loadFXML;

public class MillController {
    Mill game;

    @FXML
    private GameBoard gameBoard;
    @FXML
    private Label lblPlayer1;
    @FXML
    private Label lblPlayer2;
    @FXML
    private Label lblPieces1;
    @FXML
    private Label lblPieces2;
    @FXML
    private Label lblPhase;

    private Circle currentlySelected;
    private List<Position> takeablePieces;
    private List<Move> movesForReplay;
    private Player playerOne;
    private Player playerTwo;
    boolean playerOneIsWhite;
    private MoveRepository moveRepository;
    private ReplayRepository replayRepository;

    @FXML
    private void initialize() {
        UserRepository userRepository = new UserRepository();
        this.moveRepository = new MoveRepository();
        this.replayRepository = new ReplayRepository();
        String player1Name = userRepository.findById(CurrentGame.getInstance().getPlayer1Id()).getAlias();
        String player2Name = userRepository.findById(CurrentGame.getInstance().getPlayer2Id()).getAlias();

        playerOneIsWhite = Math.random() > 0.5;
        this.playerOne = new Player(playerOneIsWhite ? 1 : 2);
        this.playerTwo = new Player(playerOneIsWhite ? 2 : 1);

        if (playerOneIsWhite) {
            lblPlayer1.setText(player1Name);
            lblPlayer2.setText(player2Name);

            this.playerOne.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces1.setText("Pieces on board: " + observableValue.getValue().toString());
            });
            this.playerTwo.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces2.setText("Pieces on board: " + observableValue.getValue().toString());
            });

            this.playerOne.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer1.setDisable(!observableValue.getValue());
                lblPieces1.setDisable(!observableValue.getValue());
            });
            this.playerTwo.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer2.setDisable(!observableValue.getValue());
                lblPieces2.setDisable(!observableValue.getValue());
            });
        }
        else {
            lblPlayer1.setText(player2Name);
            lblPlayer2.setText(player1Name);

            this.playerTwo.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces1.setText("Pieces on board: " + observableValue.getValue().toString());
            });
            this.playerOne.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces2.setText("Pieces on board: " + observableValue.getValue().toString());
            });

            this.playerOne.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer2.setDisable(!observableValue.getValue());
                lblPieces2.setDisable(!observableValue.getValue());
            });
            this.playerTwo.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer1.setDisable(!observableValue.getValue());
                lblPieces1.setDisable(!observableValue.getValue());
            });
        }

        this.game = new Mill(playerOne, playerTwo);
        this.currentlySelected = null;
        this.takeablePieces = null;
        this.movesForReplay = new ArrayList<Move>();

        lblPlayer2.setDisable(true);
        lblPieces2.setDisable(true);
    }

    @FXML
    private void playerInputEvent(MouseEvent mouseEvent) throws IOException {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();
        boolean isTurnToSwitch = false;

        switch (game.getGameState()) {
            case SET  -> isTurnToSwitch = setPieceAtSelectedLocation(x, y);
            case MOVE, JUMP -> handleStateMoveAndJump(x, y);
            case TAKE -> {
                if (removeHighlightedPiece(x, y)) {
                    changeColorFromHighlightedPieces(Color.RED, this.game.getCurrentPlayerColor() == 1 ? Color.GRAY : Color.WHITE);
                    isTurnToSwitch = true;
                }
            }
        }

        if (isTurnToSwitch) {
            this.game.switchTurn();
        }

        if (this.game.getGameState().equals(GameState.OVER)) {
            handleGameStateOver();
        }

        lblPhase.setText("You can " + game.getGameState().toString());
    }

    public void handleGameStateOver() throws IOException {
        int winnerColor = this.game.getCurrentPlayerColor() == 1 ? 2 : 1;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Congratulations " + (winnerColor == 1 ? lblPlayer1.getText() : lblPlayer2.getText()) + ", you WON!!!");

        GameRepository gameRepository = new GameRepository();
        Game game;
        if (winnerColor == 1 && playerOneIsWhite) {
            game = new Game(CurrentGame.getInstance().getPlayer1Id(), CurrentGame.getInstance().getPlayer2Id());
            System.out.println("Links gwonna");
        }
        else {
            game = new Game(CurrentGame.getInstance().getPlayer2Id(), CurrentGame.getInstance().getPlayer1Id());
            System.out.println("Rechts gwonna");
        }
        gameRepository.insert(game);

        for (int i = 0; i < movesForReplay.size(); i++) {
            moveRepository.save(movesForReplay.get(i));
            replayRepository.insert(new Replay((long) i, game.getGameId(), movesForReplay.get(i)));
        }

        alert.showAndWait();
        Stage stage = (Stage) lblPhase.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("home"), 800, 800));
    }

    private void handleStateMoveAndJump(double x, double y) {
        if (this.currentlySelected != null && moveSelectedPieceToNextPositionOrDropIt(x, y)) {
            game.switchTurn();
            return;
        }

        this.currentlySelected = gameBoard.getPieceFromSelectedCoordinates(x, y, game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

        if (this.currentlySelected != null && this.game.getGameState() != GameState.TAKE) {
            this.currentlySelected.setFill(Color.RED);
        }
    }

    private boolean moveSelectedPieceToNextPositionOrDropIt(double x, double y) {
        this.currentlySelected.setFill(this.game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

        if (!this.gameBoard.containsCoordinate(x, y)) {
            this.currentlySelected = null;
            return false;
        }

        Position from = this.gameBoard.convertCoordinateToPosition(this.currentlySelected.getCenterX(), this.currentlySelected.getCenterY());
        Position to   = this.gameBoard.convertCoordinateToPosition(x, y);

        if (!game.movePiece(game.getCurrentPlayerColor(), from, to)) {
            this.currentlySelected = null;
            return false;
        }

        double[] fxy = positionToRaw(from);
        double[] txy = positionToRaw(to);

        this.movesForReplay.add(new Move(fxy[0], fxy[1], txy[0], txy[1]));
        this.gameBoard.getChildren().remove(this.currentlySelected);
        drawCircleAtPos(to);

        if (Logic.activatesMill(this.game, from, to)) {
            this.game.setGameState(GameState.TAKE);
            highlightTakeablePieces();
            return false;
        }

        return true;
    }

    private boolean highlightTakeablePieces() {
        this.takeablePieces = Logic.getTakeablePieces(game, game.getCurrentPlayerColor() == 1 ? 2 : 1);

        if (this.takeablePieces.isEmpty()) {
            this.game.updateGameState();
            return true;
        }

        changeColorFromHighlightedPieces(this.game.getCurrentPlayerColor() == 1 ? Color.GRAY : Color.WHITE, Color.RED);
        return false;
    }

    private boolean removeHighlightedPiece(double x, double y) {
        if (!gameBoard.containsCoordinate(x, y)) {
            return false;
        }

        Position pos = gameBoard.convertCoordinateToPosition(x, y);

        if (this.takeablePieces.contains(pos)) {
            double[] xy = positionToRaw(pos);
            Circle circle = gameBoard.getPieceFromSelectedCoordinates(x, y, Color.RED);
            this.movesForReplay.add(new Move(-1, -1, xy[0], xy[1]));
            gameBoard.getChildren().remove(circle);
            game.removePiece(pos);
            return true;
        }

        return false;
    }

    private boolean setPieceAtSelectedLocation(double x, double y) {
        boolean isTurnToSwitch = false;

        if (!gameBoard.containsCoordinate(x, y)) {
            return false;
        }
        Position pos = gameBoard.convertCoordinateToPosition(x, y);

        if (game.setPiece(game.getCurrentPlayerColor(), pos)) {
            double[] xy = positionToRaw(pos);
            this.movesForReplay.add(new Move(xy[0], xy[1], 0, 0));
            drawCircleAtPos(pos);

            if (Logic.activatesMill(game, null, pos)) {
                game.setGameState(GameState.TAKE);
                if (highlightTakeablePieces()) {
                    isTurnToSwitch = true;
                }
            } else {
                isTurnToSwitch = true;
                game.updateGameState();
            }
        }

        return isTurnToSwitch;
    }

    private void changeColorFromHighlightedPieces(Color fColor, Color tColor) {
        this.currentlySelected = null;
        this.takeablePieces.stream()
                .map(p -> {
                    double[] xy = positionToRaw(p);
                    Circle c = gameBoard.getPieceFromSelectedCoordinates(xy[0], xy[1], fColor);
                    return c;
                })
                .forEach(c -> {
                    if (c != null)
                        c.setFill(tColor);
                });
    }

    private void drawCircleAtPos(Position pos) {
        double[] xy = positionToRaw(pos);

        if (game.getCurrentPlayerColor() == 1) {
            gameBoard.drawIntersection(xy[0], xy[1], Color.WHITE, 9);
        }
        else {
            gameBoard.drawIntersection(xy[0], xy[1], Color.GRAY, 9);
        }
    }

    private double[] positionToRaw(Position pos) {
        double boardSize = Math.min(gameBoard.getWidth(), gameBoard.getHeight()) - 2 * 50;
        double aSixth = boardSize / 6;

        return new double[]{50 + pos.getX() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth, 50 + pos.getY() * ((3 - pos.getZ()) * aSixth) + pos.getZ() * aSixth};
    }
}