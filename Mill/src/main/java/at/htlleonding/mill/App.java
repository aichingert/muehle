package at.htlleonding.mill;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        Scene scene = new Scene(loadFXML("login"), 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Muehle - Nine Men's Morris");
        primaryStage.getIcons().add(loadIcon());
        primaryStage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static Image loadIcon() {
        return new Image(Objects.requireNonNull(App.class.getResourceAsStream("icon.png")));
    }

    public static void main(String[] args) {
        launch();
    }
}