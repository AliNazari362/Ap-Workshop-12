package controllers;

import config.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private Button goToGame;
    @FXML
    private Button goToStart;
    @FXML
    private TextField nameInput;

    @FXML
    public void initialize() {

        goToGame.setOnMouseClicked(e ->{
            if(nameInput.getText().isBlank()) {
                nameInput.setPromptText("Fill the box...");
                return;
            }
            SceneManager.setScene("/game.fxml");
        });
        goToStart.setOnMouseClicked(e -> SceneManager.setScene("/start.fxml"));
    }
}
