package config;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URISyntaxException;
import java.util.Objects;

public class BackgroundMediaPlayer {

    private static BackgroundMediaPlayer INSTANCE;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying;

    public static BackgroundMediaPlayer getInstance() {
        if (INSTANCE == null)
            INSTANCE = new BackgroundMediaPlayer();
        return INSTANCE;
    }

    public void initMusic() {
        try {
            String path = Objects.requireNonNull(getClass().getResource("/music/bg.wav")).toURI().toString();
            mediaPlayer = new MediaPlayer(new Media(path));
            isPlaying = true;
        } catch (URISyntaxException e) {
            System.out.println("EEROR: " + e.getMessage());
        }

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setVolume(0.5);
        mediaPlayer.setAutoPlay(true);
    }

    public void setSoundButton(Button soundBtn) {
        soundBtn.setText("Sound On");
        soundBtn.setOnMouseClicked(e -> {
            if (mediaPlayer != null) {
                if (isPlaying) {
                    mediaPlayer.pause();
                    soundBtn.setText("Sound off");
                    isPlaying = false;
                } else {
                    mediaPlayer.play();
                    soundBtn.setText("Sound On");
                    isPlaying = true;
                }
            }
        });
    }
}
