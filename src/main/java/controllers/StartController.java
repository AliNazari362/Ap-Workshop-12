package controllers;

import config.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class StartController {

    @FXML
    private Button goToLogin;
    @FXML
    private Button exitBtn;

    @FXML
    public void initialize() {

        goToLogin.setOnMouseClicked(e -> SceneManager.setScene("/login.fxml"));
        exitBtn.setOnMouseClicked(e -> System.exit(0));
    }
}
