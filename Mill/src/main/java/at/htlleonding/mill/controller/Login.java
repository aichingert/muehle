package at.htlleonding.mill.controller;

import at.htlleonding.mill.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;

import static at.htlleonding.mill.App.loadFXML;

public class Login {
    public Button changeBtn;
    public TextField username;
    public TextField password;

    @FXML
    private void initialize() {}

    public void switchToHomePage(ActionEvent actionEvent) throws IOException {
        String username = this.username.getText();
        String password = this.password.getText();

        System.out.println(username);
        System.out.println(password);

        Stage stage = (Stage) changeBtn.getScene().getWindow();
        stage.setScene(new Scene(loadFXML("mill"), 800, 800));
    }
}
