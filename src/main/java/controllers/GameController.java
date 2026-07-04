package controllers;

import config.BackgroundMediaPlayer;
import config.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Logic;
import logic.Notation;
import logic.ResultStatus;
import logic.TurnStatus;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;

public class GameController {

    @FXML
    private Text playerTag;
    @FXML
    private Text timerText;
    @FXML
    private Button goToHome;
    @FXML
    private GridPane gridGameBox;
    @FXML
    private HashMap<Integer, GridPane> cells;
    @FXML
    private Button soundBtn;


    private int timeReminding;
    private Logic logic;

    @FXML
    public void initialize() {
        logic = Logic.getInstance();
        logic.startGame();
        cells = new HashMap<>();
        updateText();
        BackgroundMediaPlayer.getInstance().setSoundButton(soundBtn);

        goToHome.setOnMouseClicked(e -> {
            SceneManager.setScene("/start.fxml");
            logic.restToDefault();
        });
        timer();
        for (Node node : gridGameBox.getChildren()) {
            if (node instanceof GridPane cell) {
                cell.setOnMouseClicked(e -> game(e));
                cells.put(Integer.parseInt(cell.getId().substring(1)), cell);
            }
        }
        game();
    }

    private void timer() {
        timeReminding = 60;
        timerText.setText("01:00");
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeReminding--;
            timerText.setText("00:%02d".formatted(timeReminding));

            if (timeReminding <= 0) {
                logic.setResultStatus(ResultStatus.TIME_OVER);
                goToResult();
                logic.restToDefault();
                timeReminding = 60;
                timerText.setText("01:00");
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateText() {
        playerTag.setText(logic.getPlayerName() + " play on: " + logic.getNotation() +
                "\nTurn: " + logic.getTurnStatus());
    }

    private void game() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(new Random().nextInt(5000)), e -> {
            int r = logic.step();
            if (r != -1) {
                GridPane currentCell = cells.get(r);
                setStatus(logic, currentCell);
                updateText();
                if (logic.isFinished() != ResultStatus.GAMING) goToResult();
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    private void setStatus(Logic logic, GridPane cell) {
        for (Node node : cell.getChildren()) {
            if (getNotation(logic) == Notation.CIRCLE) {
                if (node instanceof Circle circle) circle.setVisible(true);
            } else {
                if (node instanceof Line line) line.setVisible(true);
            }
        }
    }

    private Notation getNotation(Logic logic) {
        Notation userNotation = logic.getNotation();
        if (logic.getTurnStatus() == TurnStatus.YOU) {
            return userNotation;
        } else {
            if (userNotation == Notation.CIRCLE) return Notation.LINE;
            else return Notation.CIRCLE;
        }
    }

    private void goToResult() {
        SceneManager.setScene("/result.fxml");
    }

    private void game(MouseEvent e) {
        if (logic.getTurnStatus() == TurnStatus.BOT) {
            game();
            return;
        }
        int id = Integer.parseInt(((GridPane) e.getSource()).getId().substring(1));
        int r = logic.step(id / 10, id % 10);
        if (r != -1) {
            GridPane currentCell = cells.get(r);
            setStatus(logic, currentCell);
            updateText();
            if (logic.isFinished() == ResultStatus.GAMING) game();
            else goToResult();
        }
    }
}
