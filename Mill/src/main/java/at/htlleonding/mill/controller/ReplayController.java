package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.*;
import at.htlleonding.mill.model.helper.CurrentGame;
import at.htlleonding.mill.model.helper.CurrentReplay;
import at.htlleonding.mill.model.helper.LoginHelper;
import at.htlleonding.mill.repositories.GameRepository;
import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.view.GameBoard;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static at.htlleonding.mill.App.loadFXML;

public class ReplayController {
    @FXML
    private Button btnAutomatic;
    @FXML
    private Button btnStop;
    @FXML
    private Button btnPrevious;
    @FXML
    private Button btnNext;
    @FXML
    private Slider slider;
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
    private Player playerOne;
    private Player playerTwo;
    private Mill game;
    private Color currentColor;
    private Timeline timeline;
    GameRepository gameRepository = new GameRepository();
    UserRepository userRepository = new UserRepository();

    @FXML
    private void initialize() {
        currentColor = Color.WHITE;
        CurrentReplay.getInstance().fillMoves();
        timeline = new Timeline();

        Game game = gameRepository.findById(CurrentReplay.getInstance().getGameId());
        Long loggedInUserId = LoginHelper.getInstance().getCurrentUserId();
        Long otherUserId = game.getWinnerId().equals(loggedInUserId) ? game.getLoserId() : game.getWinnerId();

        boolean playerOneIsWhite = (game.isWinnerWhite() && game.getWinnerId().equals(loggedInUserId)) ||
                (!game.isWinnerWhite() && game.getLoserId().equals(loggedInUserId));
        String player1Name = userRepository.findById(loggedInUserId).getAlias();
        String player2Name = userRepository.findById(otherUserId).getAlias();

        setListenerForPlayerOrder(playerOneIsWhite, player1Name, player2Name);

        this.game = new Mill(playerOne, playerTwo);
        lblPlayer2.setDisable(true);
        lblPieces2.setDisable(true);
    }

    @FXML
    private void nextMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getNext();

        if (replay == null) {
            return;
        }

        Move move = replay.getMove();

        // Tx and Ty == 0.0 => SET
        if (move.getTx() == 0.0 && move.getTy() == 0.0) {
            gameBoard.drawIntersection(move.getFx(), move.getFy(), currentColor, 9);

            // For player order
            game.setPiece(currentColor.equals(Color.WHITE) ? 1 : 2, gameBoard.convertCoordinateToPosition(move.getFx(), move.getFy()));

        // Fx and Fy == -1 => TAKE
        } else if (move.getFx() == -1.0 && move.getFy() == -1.0) {
            gameBoard.getChildren().remove(
                    gameBoard.getPieceFromSelectedCoordinates(move.getTx(), move.getTy(), currentColor)
            );
            currentColor = inverseCurrentColor();

            // For player order
            game.switchTurn();
            game.setGameState(GameState.TAKE);
            game.removePiece(gameBoard.convertCoordinateToPosition(move.getTx(), move.getTy()), game.getCurrentPlayerColor() == 1 ? 2 : 1);

        // Normal move phase
        } else {
            gameBoard.getChildren().remove(
                    gameBoard.getPieceFromSelectedCoordinates(move.getFx(), move.getFy(),currentColor)
            );
            gameBoard.drawIntersection(move.getTx(), move.getTy(), currentColor, 9);

            // For player order
            game.switchTurn();
            game.setGameState(GameState.TAKE);
            game.removePiece(gameBoard.convertCoordinateToPosition(move.getFx(), move.getFy()), game.getCurrentPlayerColor() == 1 ? 2 : 1);
            game.setPiece(currentColor.equals(Color.WHITE) ? 1 : 2, gameBoard.convertCoordinateToPosition(move.getTx(), move.getTy()));
            game.switchTurn();
        }

        currentColor = inverseCurrentColor();

        // For player order
        game.switchTurn();
        game.updateGameState();
    }

    @FXML
    private void previousMove(ActionEvent actionEvent) {
        Replay replay = CurrentReplay.getInstance().getPrevious();

        if (replay == null) {
            return;
        }

        Move move = replay.getMove();
        currentColor = inverseCurrentColor();

        // For player order
        game.switchTurn();
        game.updateGameState();
        System.out.println(move);

        // Tx and Ty == 0.0 => SET
        if (move.getTx() == 0.0 && move.getTy() == 0.0) {
            System.out.println("SET");
            gameBoard.getChildren().remove(
                    gameBoard.getPieceFromSelectedCoordinates(move.getFx(), move.getFy(), currentColor)
            );

            // For player order
            game.switchTurn();
            game.setGameState(GameState.TAKE);
            game.removePiece(gameBoard.convertCoordinateToPosition(move.getFx(), move.getFy()), game.getCurrentPlayerColor() == 1 ? 2 : 1);
            game.switchTurn();

            // Fx and Fy == -1 => TAKE
        } else if (move.getFx() == -1.0 && move.getFy() == -1.0) {
            System.out.println("TAKE");
            currentColor = inverseCurrentColor();
            gameBoard.drawIntersection(move.getTx(), move.getTy(), currentColor, 9);

            // For player order
            game.switchTurn();
            game.setPiece(currentColor.equals(Color.WHITE) ? 1 : 2, gameBoard.convertCoordinateToPosition(move.getTx(), move.getTy()));

            // Normal move phase
        } else {
            System.out.println("NORMAL");
            gameBoard.getChildren().remove(
                    gameBoard.getPieceFromSelectedCoordinates(move.getTx(), move.getTy(), currentColor)
            );
            gameBoard.drawIntersection(move.getFx(), move.getFy(), currentColor, 9);

            // For player order
            game.switchTurn();
            game.setGameState(GameState.TAKE);
            game.removePiece(gameBoard.convertCoordinateToPosition(move.getTx(), move.getTy()), game.getCurrentPlayerColor() == 1 ? 2 : 1);
            game.setPiece(currentColor.equals(Color.WHITE) ? 1 : 2, gameBoard.convertCoordinateToPosition(move.getFx(), move.getFy()));
            game.switchTurn();
        }
    }

    private Color colorFromCounter(int nth) {
        return nth % 2 == 0 ? Color.WHITE : Color.GRAY;
    }

    private Color inverseCurrentColor() {
        return currentColor == Color.WHITE ? Color.GRAY : Color.WHITE;
    }

    private void setListenerForPlayerOrder(boolean playerOneIsWhite, String player1Name, String player2Name) {
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
    }


    @FXML
    private void onBtnBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) gameBoard.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("home"), 900, 900));
    }

    @FXML
    private void onBtnAutomatic(ActionEvent actionEvent) throws InterruptedException {
        this.btnPrevious.setDisable(true);
        this.btnNext.setDisable(true);
        this.btnStop.setDisable(false);
        this.btnAutomatic.setDisable(true);
        double milliseconds = slider.getValue() * 1000;
        System.out.println("SLIDDDDEEEEEEEEEEEERRR " + milliseconds);

        timeline = new Timeline(new KeyFrame(Duration.millis(milliseconds), ev -> {
            nextMove(null);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

    }

    @FXML
    private void onBtnStopAutomatic(ActionEvent actionEvent) {
        this.btnPrevious.setDisable(false);
        this.btnNext.setDisable(false);
        this.btnAutomatic.setDisable(false);
        this.btnStop.setDisable(true);
        this.timeline.stop();
    }
}