package at.htlleonding.mill.controller;

import at.htlleonding.mill.bot.BruteForce;
import at.htlleonding.mill.model.Difficulties;
import at.htlleonding.mill.model.Game;
import at.htlleonding.mill.model.Mill;
import at.htlleonding.mill.model.Player;
import at.htlleonding.mill.model.helper.CurrentGame;
import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.view.GameBoard;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;

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
    private Player playerOne;
    private Player playerTwo;
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
        difficulty.getSelectionModel().selectFirst();

        UserRepository userRepository = new UserRepository();
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

    public void playerInputEvent(MouseEvent mouseEvent) {

    }
}
