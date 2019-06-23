package gameoflife.model;

import gameoflife.controller.MainViewController;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GameOfLife extends Thread {


    private final List<Node> selected;
    private final List<Node> grid;
    private final MainViewController mainViewController;
    private final int count;
    private int speed;
    private static final int MAX_TIME = 300;
    private List<Node> compressedGrid=new ArrayList<>();

    public GameOfLife(int speed, List<Node> grid, List<Node> selected, MainViewController mainViewController) {
        this.grid = grid;
        this.speed = speed;
        this.selected = selected;
        this.mainViewController = mainViewController;
        this.count = (int) Math.round(Math.sqrt(grid.size()));
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                mainViewController.setSelected(nextStep());
                Thread.sleep(MAX_TIME / speed);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private synchronized List<Node> nextStep() {
        List<Node> toRemove = new ArrayList<>();
        List<Node> toBeBorn = new ArrayList<>();
        compressedGrid = getCompressedGrid();
        compressedGrid.forEach(node -> {
            int numberOfNeighbours=getNumberOfNeighbours(node);
            System.out.println(numberOfNeighbours);
            if (!selected.contains(node) && numberOfNeighbours == 3) {
                toBeBorn.add(node);
            }
        });

        selected.forEach(node -> {
            int numberOfNeighbours = getNumberOfNeighbours(node);
            if (numberOfNeighbours > 3 || numberOfNeighbours < 2) {
                toRemove.add(node);
            }
        });
        selected.addAll(toBeBorn);
        selected.removeAll(toRemove);
        return selected;
    }

    private List<Node> getCompressedGrid() {
        compressedGrid.clear();
        for (Node node : selected) {
            int row = GridPane.getRowIndex(node);
            int column = GridPane.getColumnIndex(node);
            compressedGrid.add(node);
            try {
                for (int i = 1; i > -2; i--) {
                    compressedGrid.add(grid.get(row * count + column - count + i));
                }
                for (int i = 1; i > -2; i--) {
                    compressedGrid.add(grid.get(row * count + column + count + i));
                }
                compressedGrid.add(grid.get(row * count + column + 1));
                compressedGrid.add(grid.get(row * count + column + -1));
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }
        return compressedGrid;
    }

    private int getNumberOfNeighbours(Node node) {
        int numberOfNeighbours = 0;
        int columnIndex = 0;
        int rowIndex = 0;
        try {
            columnIndex = GridPane.getColumnIndex(node);
            rowIndex = GridPane.getRowIndex(node);
            for (Node node1 : compressedGrid) {
                int rowDifference = Math.abs(GridPane.getRowIndex(node1) - rowIndex);
                int columnDifference = Math.abs(GridPane.getColumnIndex(node1) - columnIndex);
                if (selected.contains(node1) && ((columnDifference == 1 && rowDifference < 2) || (rowDifference == 1 && columnDifference < 2))) {
                    numberOfNeighbours++;
                }
            }

        } catch (NullPointerException e) {
        //    System.out.println(columnIndex + "," + rowIndex + "=" + numberOfNeighbours);
            return numberOfNeighbours;
        }
      //  System.out.println(columnIndex + "," + rowIndex + "=" + numberOfNeighbours);
        return numberOfNeighbours;
    }

}
