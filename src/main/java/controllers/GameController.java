package controllers;

import config.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class GameController {

    @FXML
    private Button goToHome;

    @FXML
    public void initialize() {
        goToHome.setOnMouseClicked(e -> SceneManager.setScene("/start.fxml"));
    }
}
