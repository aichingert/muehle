package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.helper.LoginHelper;
import at.htlleonding.mill.model.helper.LoginResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static at.htlleonding.mill.App.loadFXML;

public class RegisterController {
    @FXML
    private Button registerBtn;
    @FXML
    private TextField username;
    @FXML
    private TextField alias;
    @FXML
    private TextField password;
    @FXML
    private Label infoLabel;

    @FXML
    private void initialize() {}

    @FXML
    private void onBtnRegister(ActionEvent actionEvent) throws IOException {
        String username = this.username.getText();
        String alias = this.alias.getText();
        String password = this.password.getText();

        if (username.isEmpty() || alias.isEmpty() || password.isEmpty()) {
            (new Alert(Alert.AlertType.INFORMATION, "You need to fill out every field!")).show();
            this.infoLabel.setText("Please fill out every field!");
            return;
        }

        LoginResult result = LoginHelper.getInstance().insertUserIfValid(username, password, alias);

        switch (result) {
            case SUCCESS -> {
                Stage stage = (Stage) registerBtn.getScene().getWindow();
                stage.setScene(new Scene(loadFXML("login"), 800, 800));
            }
            case TAKEN -> {
                (new Alert(Alert.AlertType.INFORMATION, "Username or alias is already taken")).show();
                this.infoLabel.setText("This username or alias is already taken!");
            }
            case TO_SHORT -> {
                (new Alert(Alert.AlertType.INFORMATION, "Username and alias need to be at least 3 characters long and password 8")).show();
                this.infoLabel.setText("Username and alias need to be at least 3 characters long and password 8");
            }
        }
    }

    @FXML
    private void switchToLoginPage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) registerBtn.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("login"), 800, 800));
    }
}
