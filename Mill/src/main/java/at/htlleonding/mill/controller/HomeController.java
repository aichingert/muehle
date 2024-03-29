package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.helper.CurrentGame;
import at.htlleonding.mill.model.helper.CurrentReplay;
import at.htlleonding.mill.repositories.GameRepository;
import at.htlleonding.mill.repositories.UserRepository;
import at.htlleonding.mill.model.Game;
import at.htlleonding.mill.model.User;
import at.htlleonding.mill.model.helper.LoginHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static at.htlleonding.mill.App.loadFXML;

public class HomeController {
    @FXML
    private Label player1Label;
    @FXML
    private ComboBox<String> comboBoxPlayer2;
    @FXML
    private Label currentPos;
    @FXML
    private ListView<Game> gamesLv;
    @FXML
    private ListView<User> leaderboardLv;
    @FXML
    private Button replayBtn;
    @FXML
    private Label aliasLabel;
    @FXML
    private TextField searchTextField;
    private FilteredList<String> aliasesFilteredList;
    private UserRepository userRepository;

    @FXML
    private void initialize() {
        userRepository = new UserRepository();
        searchTextField.setPromptText("Search");

        //aliasLabel.getScene().getStylesheets().add("style.css");
        gamesLv.setOnKeyPressed(k -> {
            if (k.getCode() == KeyCode.ESCAPE) {
                replayBtn.setDisable(true);
                CurrentReplay.getInstance().setGameId(null);
                gamesLv.getSelectionModel().clearSelection();
            }
        });

        String currentUserAlias = userRepository.findById(LoginHelper.getInstance().getCurrentUserId()).getAlias();

        aliasesFilteredList = new FilteredList<>(FXCollections.observableList(userRepository
                .getAllAliases()
                .stream()
                .filter(a -> !a.equals(currentUserAlias))
                .toList()));
        comboBoxPlayer2.getItems().addAll(aliasesFilteredList);

        replayBtn.setDisable(true);
        aliasLabel.setText(LoginHelper.getInstance().getCurrentUser());
        player1Label.setText(currentUserAlias);

        GameRepository gameRepository = new GameRepository();
        List<Game> allGames = gameRepository.findAllGamesByUserId(LoginHelper.getInstance().getCurrentUserId());
        gamesLv.setItems(FXCollections.observableList(allGames));

        if (allGames.isEmpty()) {
            gamesLv.setStyle("-fx-background-color: #20262b;");
        }

        List<User> allUsers = userRepository.findAll();
        User currentUser = allUsers.stream().filter(u -> u.getId().equals(LoginHelper.getInstance().getCurrentUserId())).findFirst().get();
        List<User> topThree = new ArrayList<>(allUsers.stream().limit(3).toList());
        currentPos.setText(currentUser.toString());
        leaderboardLv.setItems(FXCollections.observableList(topThree));
    }

    @FXML
    private void onBtnVs(ActionEvent actionEvent) throws IOException {
        Long player1Id = LoginHelper.getInstance().getCurrentUserId();

        String player2Alias = comboBoxPlayer2.getSelectionModel().getSelectedItem();
        User player2 = userRepository.findByAlias(player2Alias);

        if (player2 == null || player2Alias.isEmpty() || player2Alias.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an enemy!");
            alert.showAndWait();
        }
        else {
            CurrentGame.getInstance().setPlayer1Id(player1Id);
            CurrentGame.getInstance().setPlayer2Id(player2.getId());

            Stage stage = (Stage) aliasLabel.getScene().getWindow();
            stage.setScene(new Scene(loadFXML("mill"), 800, 800));
        }
    }

    @FXML
    private void onClickGamesLv(MouseEvent mouseEvent) {
        if (!gamesLv.getSelectionModel().isEmpty()) {
            replayBtn.setDisable(false);
            CurrentReplay.getInstance().setGameId(gamesLv.getSelectionModel().getSelectedItem().getGameId());
        }
    }

    @FXML
    private void onBtnLogout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) aliasLabel.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("login"), 800, 800));
    }

    @FXML
    private void onClickLeaderboardLv(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        DialogPane dialogPane = alert.getDialogPane();

        List<User> allUsers = userRepository.findAll();
        ListView<User> lv = new ListView<>();
        lv.setItems(FXCollections.observableList(allUsers));

        alert.setTitle("Leaderboard");
        dialogPane.setContent(lv);
        dialogPane.getStylesheets().add("style.css");

        alert.show();
    }

    @FXML
    private void onBtnReplay(ActionEvent actionEvent) throws IOException {
        if (CurrentReplay.getInstance().getGameId() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a game!");
            alert.showAndWait();
        }
        else {
            Stage stage = (Stage) aliasLabel.getScene().getWindow();
            stage.setScene(new Scene(loadFXML("replay"), 800, 800));
        }
    }

    @FXML
    private void onKeyTypedSearchField(KeyEvent keyEvent) {
        aliasesFilteredList.setPredicate(null);
        comboBoxPlayer2.show();
        aliasesFilteredList.setPredicate(a -> a.toLowerCase().contains(searchTextField.getText().toLowerCase()));
        comboBoxPlayer2.getItems().clear();
        comboBoxPlayer2.getItems().addAll(aliasesFilteredList);
        comboBoxPlayer2.show();
    }

    public void onBtnBot(ActionEvent actionEvent) throws IOException {
        CurrentGame.getInstance().setPlayer1Id(LoginHelper.getInstance().getCurrentUserId());
        CurrentGame.getInstance().setPlayer2Id(userRepository.findByAlias("Bot").getId());

        Stage stage = (Stage) aliasLabel.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("bot"), 800, 800));
    }
}
