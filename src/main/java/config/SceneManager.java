package config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {

    private static Stage stage;

    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void setScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}