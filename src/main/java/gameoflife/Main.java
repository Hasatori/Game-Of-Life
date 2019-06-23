package gameoflife;

import gameoflife.controller.MainViewController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        MainViewController mainViewController = new MainViewController(primaryStage);

    }

    public static void main(String[] args) {
        Application.launch();
    }
}
