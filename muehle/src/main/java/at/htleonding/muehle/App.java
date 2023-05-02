package at.htleonding.muehle;

import at.htleonding.muehle.view.GameBoard;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(loadFXML("muehle"), 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Muehle - Nine Men's Morris");
        primaryStage.getIcons().add(loadIcon("icon"));
        primaryStage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static Image loadIcon(String iconName) {
        return new Image(Objects.requireNonNull(App.class.getResourceAsStream(iconName + ".png")));
    }

    public static void main(String[] args) {
        launch();
    }
}