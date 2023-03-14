package at.htleonding.muehle;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MuehleController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}