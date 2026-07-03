import config.SceneManager;
import config.State;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private State gameState = State.START;

    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneManager.init(primaryStage);
        SceneManager.setScene("/start.fxml");
        primaryStage.setTitle("TicToc");
        primaryStage.setResizable(false);
        primaryStage.setMinHeight(720);
        primaryStage.setMinWidth(1280);
        primaryStage.show();
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
