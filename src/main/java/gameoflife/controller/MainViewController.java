package gameoflife.controller;

import gameoflife.view.MainView;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainViewController extends Controller {
    private MainView view;

    /**
     * @param stage stage
     */
    public MainViewController(Stage stage) throws IOException {
        super(stage);
        loadView();
    }

    @Override
    public void loadView() throws IOException {
        view = new MainView(this);
        stage.setScene(view);
        stage.show();
    }

    public void startGame(double speed, List<Node> selected) {

    }

    public void stopGame() {

    }

    public void changeSpeed(double speed) {

    }

    public void changeGridSize(double gridSize) {

    }

    public void setSelected(List<Node> selected) {
        view.select(selected);
    }
}
