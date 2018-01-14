package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Starts the application and sets default window properties
 */
public class Main extends Application {
    /**
     * Sets default window properties
     * @param primaryStage main window of app
     * @throws Exception exceptions of whole application
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Robot");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.getIcons().add(new Image("file:./images/ico.png"));
    }

    /**
     * Starts the application
     * @param args additional args. Our app isn't using them
     */
    public static void main(String[] args) {
        launch(args);
    }
}
