package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.helper.LoginHelper;
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
            (new Alert(Alert.AlertType.INFORMATION, "Please fill out every field!")).show();
            this.infoLabel.setText("Please fill out every field!");
            return;
        }

        if (LoginHelper.getInstance().isValidUser(username, password)) {
            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(loadFXML("mill"), 800, 800));
        } else {
            (new Alert(Alert.AlertType.INFORMATION, "Incorrect username or password")).show();
            this.infoLabel.setText("Invalid username or password!");
        }
    }

    @FXML
    private void switchToRegisterPage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("register"), 800, 800));
    }
}
