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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import logic.Logic;
import logic.Notation;
import logic.ResultStatus;
import logic.TurnStatus;

import java.awt.*;
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
    @FXML
    private Button resetBtn;


    private Timeline timerTimeLine;
    private Timeline botTimeLine;
    private int timeReminding;
    private Logic logic;

    @FXML
    public void initialize() {
        logic = Logic.getInstance();
        logic.startGame();
        cells = new HashMap<>();
        updateText();
        BackgroundMediaPlayer.getInstance().setSoundButton(soundBtn);
        configResetBtn();

        goToHome.setOnMouseClicked(e -> {
            SceneManager.setScene("/start.fxml");
            timerTimeLine.stop();
            botTimeLine.stop();
        });
        timer();
        for (Node node : gridGameBox.getChildren()) {
            if (node instanceof GridPane cell) {
                cell.setOnMouseClicked(this::game);
                cells.put(Integer.parseInt(cell.getId().substring(1)), cell);
            }
        }
        game();
    }

    private void configResetBtn() {
        resetBtn.setOnMouseClicked(e -> {
            restToDefault();
            for (Node patentNode : gridGameBox.getChildren()) {
                if (patentNode instanceof GridPane patentGrid) {
                    for (Node node : patentGrid.getChildren()) {
                        if (node instanceof Circle circle) circle.setVisible(false);
                        if (node instanceof Line line) line.setVisible(false);
                        timeReminding = 60;
                        timerText.setText("01:00");
                        restToDefault();
                        logic.startGame();
                        updateText();
                        timer();
                        if (logic.getTurnStatus() == TurnStatus.BOT) game();
                    }
                }
            }
        });
    }

    private void timer() {
        if (timerTimeLine != null) timerTimeLine.stop();

        timeReminding = 60;
        timerText.setText("01:00");

        timerTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            timeReminding--;
            timerText.setText("00:%02d".formatted(timeReminding));

            if (timeReminding <= 0) {
                logic.setResultStatus(ResultStatus.TIME_OVER);
                timeReminding = 60;
                timerText.setText("01:00");
                goToResult();
            }
        }));
        timerTimeLine.setCycleCount(Timeline.INDEFINITE);
        timerTimeLine.play();
    }

    private void updateText() {
        playerTag.setText(logic.getPlayerName() + " play on: " + logic.getNotation() +
                "\nTurn: " + logic.getTurnStatus());
    }

    private void game() {
        if (logic.getTurnStatus() != TurnStatus.BOT) return;
        if (botTimeLine != null) botTimeLine.stop();

        botTimeLine = new Timeline(new KeyFrame(
                Duration.millis(new Random().nextInt(5000)), e -> {
            int r = logic.step();
            if (r != -1) {
                GridPane currentCell = cells.get(r);
                setStatus(currentCell);
                updateText();
                if (logic.isFinished() != ResultStatus.GAMING) goToResult();
            }
        }));
        botTimeLine.setCycleCount(1);
        botTimeLine.play();
    }

    private void setStatus(GridPane cell) {
        for (Node node : cell.getChildren()) {
            if (getNotation() == Notation.CIRCLE) {
                if (node instanceof Circle circle) circle.setVisible(true);
            } else {
                if (node instanceof Line line) line.setVisible(true);
            }
        }
    }

    private Notation getNotation() {
        Notation userNotation = logic.getNotation();
        if (logic.getTurnStatus() == TurnStatus.BOT) {
            return userNotation;
        } else {
            if (userNotation == Notation.CIRCLE) return Notation.LINE;
            else return Notation.CIRCLE;
        }
    }

    private void goToResult() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            SceneManager.setScene("/result.fxml");
            restToDefault();
        }));
        timeline.setCycleCount(1);
        timeline.play();
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
            setStatus(currentCell);
            updateText();
            if (logic.isFinished() == ResultStatus.GAMING) game();
            else goToResult();
        }
    }

    private void restToDefault() {
        logic.setResultStatus(ResultStatus.GAMING);
        logic.emptyGrid();
        timerTimeLine.stop();
        botTimeLine.stop();
    }
}
