package at.htleonding.muehle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("muehle"), 640, 480);
        stage.setScene(scene);
        stage.setTitle("Muehle");
        stage.getIcons().add(loadIcon("icon"));
        stage.show();
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