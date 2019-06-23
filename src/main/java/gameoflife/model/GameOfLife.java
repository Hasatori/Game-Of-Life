package gameoflife.model;

import gameoflife.controller.MainViewController;
import javafx.scene.Node;

import java.util.List;

public class GameOfLife extends Thread {


    private final List<Node> selected;
    private final List<Node> grid;
    private final MainViewController mainViewController;

    public GameOfLife(List<Node> grid, List<Node> selected, MainViewController mainViewController) {
        this.grid = grid;
        this.selected = selected;
        this.mainViewController = mainViewController;

    }

    @Override
    public void run() {
        mainViewController.setSelected(nextStep());
    }


    public List<Node> nextStep() {
        return null;
    }
}
