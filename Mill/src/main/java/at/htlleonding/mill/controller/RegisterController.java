package at.htlleonding.mill.controller;

import at.htlleonding.mill.model.helper.LoginHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
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

        System.out.println(username);
        System.out.println(password);

        if (username.isEmpty() || username.isBlank() ||
            alias.isEmpty() || alias.isBlank() ||
            password.isEmpty() || password.isBlank()) {
            this.infoLabel.setText("Please fill out every field!");
        }
        else if (LoginHelper.getInstance().doesUsernameExist(username)) {
            this.infoLabel.setText("This username is already taken!");
        }
        else {
            LoginHelper.getInstance().insertUser(username, alias, password);
            Stage stage = (Stage) registerBtn.getScene().getWindow();
            stage.setScene(new Scene(loadFXML("mill"), 800, 800));
        }
    }

    @FXML
    private void switchToLoginPage(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) registerBtn.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("login"), 800, 800));
    }
}
