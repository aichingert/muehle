package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.helper.LoginHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

import static at.htlleonding.mill.App.loadFXML;

public class LoginController {
    @FXML
    private Button loginBtn;
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private Button registerBtn;
    @FXML
    private Label infoLabel;

    @FXML
    private void initialize() {}

    @FXML
    private void onLoginBtn(ActionEvent actionEvent) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();

        if (username.isEmpty() || password.isEmpty()) {
            (new Alert(Alert.AlertType.WARNING, "Please fill out every field!")).show();
            this.infoLabel.setText("Please fill out every field!");
            return;
        }

        if (LoginHelper.getInstance().isValidUser(username, password)) {
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(loadFXML("home"), 900, 900));
        } else {
            (new Alert(Alert.AlertType.WARNING, "Incorrect username or password")).show();
            this.infoLabel.setText("Invalid username or password!");
        }
    }

    @FXML
    private void switchToRegisterPage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("register"), 800, 800));
    }

    @FXML
    private void onKeyPressedLogin(KeyEvent keyEvent) throws IOException {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            if (!username.getText().isEmpty() && password.getText().isEmpty()) {
                password.requestFocus();
            }
            else if (username.getText().isEmpty() && !password.getText().isEmpty()) {
                username.requestFocus();
            }
            else if (!username.getText().isEmpty() && !password.getText().isEmpty()) {
                onLoginBtn(null);
            }
        }
    }
}
