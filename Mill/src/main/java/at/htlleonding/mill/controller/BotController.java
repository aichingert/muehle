package at.htlleonding.mill.controller;

import at.htlleonding.mill.bot.BruteForce;
import at.htlleonding.mill.model.*;
import at.htlleonding.mill.model.helper.CurrentGame;
import at.htlleonding.mill.model.helper.Logic;
import at.htlleonding.mill.model.helper.Position;
import at.htlleonding.mill.repositories.GameRepository;
import at.htlleonding.mill.repositories.MoveRepository;
import at.htlleonding.mill.repositories.ReplayRepository;
import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.view.GameBoard;
import at.htlleonding.mill.controller.MillController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static at.htlleonding.mill.App.loadFXML;

public class BotController {
    @FXML
    private Label lblPhase;
    @FXML
    private Label lblPlayer1;
    @FXML
    private Label lblPieces1;
    @FXML
    private Label lblPlayer2;
    @FXML
    private Label lblPieces2;
    @FXML
    private GameBoard gameBoard;
    @FXML
    private Button choseDifficultyBtn;
    @FXML
    private ComboBox<Difficulties> difficulty;

    private Mill game;
    private Player player;
    private Player bot;
    private Circle currentlySelected;
    private List<Position> takeablePieces;
    private List<Move> movesForReplay;
    private MoveRepository moveRepository;
    private ReplayRepository replayRepository;
    boolean playerOneIsWhite;

    @FXML
    private void initialize() {
        lblPhase.setVisible(false);
        lblPlayer1.setVisible(false);
        lblPlayer2.setVisible(false);
        lblPieces1.setVisible(false);
        lblPieces2.setVisible(false);
        gameBoard.setVisible(false);

        difficulty.setItems(FXCollections.observableList(Arrays.asList(Difficulties.EASY, Difficulties.MEDIUM, Difficulties.HARD, Difficulties.CRAZY)));
        difficulty.getSelectionModel().select(Difficulties.CRAZY);

        UserRepository userRepository = new UserRepository();
        this.moveRepository   = new MoveRepository();
        this.replayRepository = new ReplayRepository();
        this.movesForReplay   = new ArrayList<>();
        String player1Name = userRepository.findById(CurrentGame.getInstance().getPlayer1Id()).getAlias();
        String player2Name = userRepository.findById(CurrentGame.getInstance().getPlayer2Id()).getAlias();

        playerOneIsWhite = Math.random() > 0.5;
        this.player = new Player(playerOneIsWhite ? 1 : 2);
        this.bot = new Player(playerOneIsWhite ? 2 : 1);

        if (playerOneIsWhite) {
            lblPlayer1.setText(player1Name);
            lblPlayer2.setText(player2Name);

            this.player.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces1.setText("Pieces on board: " + observableValue.getValue().toString());
            });
            this.bot.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces2.setText("Pieces on board: " + observableValue.getValue().toString());
            });

            this.player.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer1.setDisable(!observableValue.getValue());
                lblPieces1.setDisable(!observableValue.getValue());
            });
            this.bot.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer2.setDisable(!observableValue.getValue());
                lblPieces2.setDisable(!observableValue.getValue());
            });
        }
        else {
            lblPlayer1.setText(player2Name);
            lblPlayer2.setText(player1Name);

            this.bot.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces1.setText("Pieces on board: " + observableValue.getValue().toString());
            });
            this.player.amountOfPiecesProperty().addListener((observableValue, number, t1) -> {
                lblPieces2.setText("Pieces on board: " + observableValue.getValue().toString());
            });

            this.player.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer2.setDisable(!observableValue.getValue());
                lblPieces2.setDisable(!observableValue.getValue());
            });
            this.bot.isPlayerTurnProperty().addListener((observableValue, aBoolean, t1) -> {
                lblPlayer1.setDisable(!observableValue.getValue());
                lblPieces1.setDisable(!observableValue.getValue());
            });
        }

        this.game = new Mill(player, bot);

        if (!playerOneIsWhite) {
            Position[] next = BruteForce.getInstance().nextMove(game, game.getCurrentPlayerColor());
            game.setPiece(game.getCurrentPlayerColor(), next[0]);
            gameBoard.drawCircleAtPos(next[0], game.getCurrentPlayerColor());
            game.switchTurn();
        }
    }

    @FXML
    private void choseDifficulty(ActionEvent actionEvent) {
        BruteForce.getInstance().setDifficulty(difficulty.getSelectionModel().getSelectedItem());
        difficulty.setVisible(false);
        choseDifficultyBtn.setVisible(false);

        lblPhase.setVisible(true);
        lblPlayer1.setVisible(true);
        lblPlayer2.setVisible(true);
        lblPieces1.setVisible(true);
        lblPieces2.setVisible(true);
        gameBoard.setVisible(true);
    }

    public void playerInputEvent(MouseEvent mouseEvent) throws IOException {
        double x = mouseEvent.getX();
        double y = mouseEvent.getY();

        switch (game.getGameState()) {
            case SET  -> setPieceAtSelectedLocation(x, y);
            case MOVE, JUMP -> handleStateMoveAndJump(x, y);
            case TAKE -> removePiecePlayer(x, y);
        }

        if (this.game.getGameState().equals(GameState.OVER)) {
            handleGameStateOver();
        }

        lblPhase.setText("You can " + game.getGameState().toString());
    }

    private void playBotRound() {
        game.switchTurn();

        int self = game.getCurrentPlayerColor();
        int oppo = game.getCurrentPlayerColor() == 1 ? 2 : 1;
        Position[] nextPosition = BruteForce.getInstance().nextMove(game, self);

        switch (game.getGameState()) {
            case SET -> {
                game.setPiece(self, nextPosition[0]);
                gameBoard.drawCircleAtPos(nextPosition[0], self);

                if (Logic.activatesMill(game, null, nextPosition[0])) {
                    removePieceBot(oppo);
                }
            }
            case MOVE -> {
                game.movePiece(self, nextPosition[0], nextPosition[1]);
                double[] xy = gameBoard.positionToRaw(nextPosition[0]);
                gameBoard.getChildren().remove(
                        gameBoard.getPieceFromSelectedCoordinates(xy[0], xy[1], self == 1 ? Color.WHITE : Color.GRAY)
                );
                gameBoard.drawCircleAtPos(nextPosition[1], self);

                if (Logic.activatesMill(game, null, nextPosition[1])) {
                    removePieceBot(oppo);
                }
            }
        }

        game.switchTurn();

    }

    private void setPieceAtSelectedLocation(double x, double y) {
        if (!gameBoard.containsCoordinate(x, y)) {
            return;
        }

        Position pos = gameBoard.convertCoordinateToPosition(x, y);

        if (game.setPiece(game.getCurrentPlayerColor(), pos)) {
            double[] xy = gameBoard.positionToRaw(pos);
            this.movesForReplay.add(new Move(xy[0], xy[1], 0, 0));
            gameBoard.drawCircleAtPos(pos, game.getCurrentPlayerColor());

            if (Logic.activatesMill(game, null, pos)) {
                game.setGameState(GameState.TAKE);
                highlightTakeablePieces();
            } else {
                playBotRound();
            }
        }
    }

    private void handleStateMoveAndJump(double x, double y) {
        if (this.currentlySelected != null) {
            moveSelectedPieceToNextPositionOrDropIt(x, y);
            return;
        }

        this.currentlySelected = gameBoard.getPieceFromSelectedCoordinates(x, y, game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

        if (this.currentlySelected != null && this.game.getGameState() != GameState.TAKE) {
            this.currentlySelected.setFill(Color.RED);
        }
    }

    private void moveSelectedPieceToNextPositionOrDropIt(double x, double y) {
        this.currentlySelected.setFill(this.game.getCurrentPlayerColor() == 1 ? Color.WHITE : Color.GRAY);

        if (!this.gameBoard.containsCoordinate(x, y)) {
            this.currentlySelected = null;
            return;
        }

        Position from = this.gameBoard.convertCoordinateToPosition(this.currentlySelected.getCenterX(), this.currentlySelected.getCenterY());
        Position to   = this.gameBoard.convertCoordinateToPosition(x, y);

        if (!game.movePiece(game.getCurrentPlayerColor(), from, to)) {
            this.currentlySelected = null;
            return;
        }

        double[] fxy = gameBoard.positionToRaw(from);
        double[] txy = gameBoard.positionToRaw(to);

        this.movesForReplay.add(new Move(fxy[0], fxy[1], txy[0], txy[1]));
        this.gameBoard.getChildren().remove(this.currentlySelected);
        gameBoard.drawCircleAtPos(to, game.getCurrentPlayerColor());

        if (Logic.activatesMill(this.game, from, to)) {
            this.game.setGameState(GameState.TAKE);
            highlightTakeablePieces();
        } else {
            playBotRound();
        }
    }

    private void removePiecePlayer(double x, double y) {
        if (!gameBoard.containsCoordinate(x, y)) {
            return;
        }

        Position pos = gameBoard.convertCoordinateToPosition(x, y);

        if (!this.takeablePieces.contains(pos)) {
            return;
        }

        double[] xy = gameBoard.positionToRaw(pos);
        this.movesForReplay.add(new Move(-1, -1, xy[0], xy[1]));

        Circle circle = gameBoard.getPieceFromSelectedCoordinates(x, y, Color.RED);
        gameBoard.getChildren().remove(circle);
        game.removePiece(pos, game.getCurrentPlayerColor());
        changeColorFromHighlightedPieces(Color.RED, game.getCurrentPlayerColor() == 1 ? Color.GRAY : Color.WHITE);
        playBotRound();
    }

    private void removePieceBot(int oppo) {
        Position toTake = BruteForce.getInstance().nextTake(game, oppo);
        game.setGameState(GameState.TAKE);
        game.removePiece(toTake, oppo);
        double[] xy = gameBoard.positionToRaw(toTake);
        gameBoard.getChildren().remove(
                gameBoard.getPieceFromSelectedCoordinates(xy[0], xy[1], oppo == 1 ? Color.WHITE : Color.GRAY)
        );
    }

    private void highlightTakeablePieces() {
        this.takeablePieces = Logic.getTakeablePieces(game, game.getCurrentPlayerColor() == 1 ? 2 : 1);

        if (this.takeablePieces.isEmpty()) {
            this.game.updateGameState();
            return;
        }

        changeColorFromHighlightedPieces(this.game.getCurrentPlayerColor() == 1 ? Color.GRAY : Color.WHITE, Color.RED);
    }

    private void changeColorFromHighlightedPieces(Color fColor, Color tColor) {
        this.currentlySelected = null;
        this.takeablePieces.stream()
                .map(p -> {
                    double[] xy = gameBoard.positionToRaw(p);
                    return gameBoard.getPieceFromSelectedCoordinates(xy[0], xy[1], fColor);
                })
                .forEach(c -> {
                    if (c != null)
                        c.setFill(tColor);
                });
    }

    public void handleGameStateOver() throws IOException {
        int winnerColor = this.game.getCurrentPlayerColor() == 1 ? 2 : 1;
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Congratulations " + (winnerColor == 1 ? lblPlayer1.getText() : lblPlayer2.getText()) + ", you WON!!!");

        GameRepository gameRepository = new GameRepository();
        Game game;
        if ((winnerColor == 1 && playerOneIsWhite) || (winnerColor == 2 && !playerOneIsWhite)) {
            game = new Game(CurrentGame.getInstance().getPlayer1Id(), CurrentGame.getInstance().getPlayer2Id(), winnerColor == 1);
            System.out.println("Angemeldeter gwonna");
        }
        else {
            game = new Game(CurrentGame.getInstance().getPlayer2Id(), CurrentGame.getInstance().getPlayer1Id(), winnerColor == 1);
            System.out.println("Andere gwonna");
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
}
