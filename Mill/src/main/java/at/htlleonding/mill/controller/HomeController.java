package at.htlleonding.mill.controller;

import at.htlleonding.mill.repositories.GameRepository;
import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.model.Game;
import at.htlleonding.mill.model.User;
import at.htlleonding.mill.model.helper.LoginHelper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static at.htlleonding.mill.App.loadFXML;

public class HomeController {
    @FXML
    private ListView<Game> gamesLv;
    @FXML
    private ListView<User> leaderboardLv;
    @FXML
    private Button replayBtn;
    @FXML
    private Label aliasLabel;

    @FXML
    private void initialize() {
        replayBtn.setDisable(true);
        aliasLabel.setText(LoginHelper.getInstance().getCurrentUser());

        GameRepository gameRepository = new GameRepository();
        gamesLv.setItems(FXCollections.observableList(gameRepository.findAllGamesByUserId(LoginHelper.getInstance().getCurrentUserId())));

        UserRepository userRepository = new UserRepository();
        leaderboardLv.setItems(FXCollections.observableList(userRepository.findAll()));

    }

    @FXML
    private void onBtnPlay(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) aliasLabel.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("mill"), 800, 800));
    }

    @FXML
    private void onClickGamesLv(MouseEvent mouseEvent) {
        if (!gamesLv.getSelectionModel().isEmpty()) {
            replayBtn.setDisable(false);
        }
    }
}
