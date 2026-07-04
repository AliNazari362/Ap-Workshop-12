package controllers;

import config.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import logic.Logic;
import logic.ResultStatus;

public class ResultController {

    @FXML
    private Button goToHome;
    @FXML
    private Button goToGame;
    @FXML
    private Button exitBtn;
    @FXML
    private Text textResult;


    @FXML
    public void initialize() {
        Logic logic = Logic.getInstance();
        ResultStatus status = logic.getResultStatus();
        switch (status) {
            case WIN -> setText("You win!", "yellow");
            case LOSE -> setText("You lose!", "red");
            case NO_RESULT -> setText("No Win, No lose!", "green");
        }
        goToHome.setOnMouseClicked(e -> {
            SceneManager.setScene("/start.fxml");
            logic.restToDefault();
        });
        goToGame.setOnMouseClicked(e -> {
            SceneManager.setScene("/game.fxml");
            logic.restToDefault();
        });
        exitBtn.setOnMouseClicked(e -> System.exit(0));
    }

    private void setText(String text, String color) {
        textResult.setText(text);
        textResult.setFill(Paint.valueOf(color));
    }
}
