package controllers;

import config.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ResultController {

    @FXML
    private Button goToHome;
    @FXML
    private Button goToGame;
    @FXML
    private Button exitBtn;

    @FXML
    public void initialize() {
        goToHome.setOnMouseClicked(e -> SceneManager.setScene("/start.fxml"));
        goToGame.setOnMouseClicked(e -> SceneManager.setScene("/game.fxml"));
        exitBtn.setOnMouseClicked(e -> System.exit(0));
    }
}
