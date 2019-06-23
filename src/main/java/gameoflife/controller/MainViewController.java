package gameoflife.controller;

import gameoflife.model.GameOfLife;
import gameoflife.view.MainView;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainViewController extends Controller {
    private MainView view;
    private GameOfLife gameOfLife;

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

    public void startGame(double speed, List<Node> grid, List<Node> selected) {
        this.gameOfLife = new GameOfLife((int) Math.round(speed), grid, selected, this);
        gameOfLife.start();
    }

    public void stopGame() {
        if (gameOfLife != null) {
            gameOfLife.interrupt();
        }
    }

    public void changeSpeed(double speed) {
        if (gameOfLife != null) {
            gameOfLife.setSpeed((int) Math.round(speed));
        }
    }

    public void changeGridSize(double gridSize) {

    }

    public void setSelected(List<Node> selected) {
        view.select(selected);
    }
}
