package gameoflife.model;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import gameoflife.controller.MainViewController;
import javafx.scene.Node;
import javafx.scene.control.DialogPane;
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
    private List<Node> compressedGrid = new ArrayList<>();

    public GameOfLife(int speed, List<Node> grid, List<Node> selected, MainViewController mainViewController) {
        this.grid = grid;
        this.speed = speed;
        this.selected = selected;
        this.mainViewController = mainViewController;
        this.count = (int) Math.round(Math.sqrt(grid.size()));
        System.out.println(count);
    }

    @Override
    public void run() {

        try {
            while (!isInterrupted()) {
                mainViewController.setSelected(nextStep());
                Thread.sleep(MAX_TIME / speed);
            }
        } catch (InterruptedException ignored) {

        } catch (Exception e) {

        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private synchronized List<Node> nextStep() {
        List<Node> toRemove = new ArrayList<>();
        List<Node> toBeBorn = new ArrayList<>();
        System.out.println(compressedGrid.size());
        compressedGrid = getCompressedGrid();
        compressedGrid.forEach(node -> {
            int numberOfNeighbours = getNumberOfNeighbours(node);
            if (!selected.contains(node) && numberOfNeighbours == 3) {
                toBeBorn.add(node);
            }
        });
      /*  System.out.printf("To be born:");
        toBeBorn.forEach(cell -> {
            System.out.println(GridPane.getRowIndex(cell) + ", " + GridPane.getColumnIndex(cell));
        });*/
        selected.forEach(node -> {
            int numberOfNeighbours = getNumberOfNeighbours(node);
            if (numberOfNeighbours > 3 || numberOfNeighbours < 2) {
                toRemove.add(node);
            }
        });
        /*System.out.printf("To be deleted:");
        toRemove.forEach(cell -> {
            System.out.println(GridPane.getRowIndex(cell) + ", " + GridPane.getColumnIndex(cell));
        });*/
        selected.addAll(toBeBorn);
        selected.removeAll(toRemove);
        return selected;
    }

    private List<Node> getCompressedGrid() {
        compressedGrid.clear();
        for (Node node : selected) {
            // System.out.println("Selected is " + GridPane.getRowIndex(node) + ", " + GridPane.getColumnIndex(node));
            int row = GridPane.getRowIndex(node);
            int column = GridPane.getColumnIndex(node);
            if (!containsNodeAlready(compressedGrid, node)) {
                compressedGrid.add(node);
            }

            Node newNode = null;
            try {
                for (int i = 1; i > -2; i--) {
                    newNode = grid.get(row * count + column - count + i);

                    if (!containsNodeAlready(compressedGrid, newNode)) {
                        compressedGrid.add(newNode);
                    }
                }
                for (int i = 1; i > -2; i--) {
                    newNode = grid.get(row * count + column + count + i);
                    if (!containsNodeAlready(compressedGrid, newNode)) {
                        compressedGrid.add(newNode);
                    }
                }
                newNode = grid.get(row * count + column + 1);
                if (!containsNodeAlready(compressedGrid, newNode)) {
                    compressedGrid.add(newNode);
                }
                newNode = grid.get(row * count + column + -1);
                if (!containsNodeAlready(compressedGrid, newNode)) {
                    compressedGrid.add(newNode);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                break;
            }
        }
       /* compressedGrid.forEach(cell -> {
            System.out.println(GridPane.getRowIndex(cell) + ", " + GridPane.getColumnIndex(cell) + ",hash=" + cell.toString());
        });*/
        return compressedGrid;
    }

    private boolean containsNodeAlready(List<Node> list, Node node) {
        int rowNum = GridPane.getRowIndex(node);
        int columnNum = GridPane.getColumnIndex(node);
        for (Node node1 : list) {
            if (GridPane.getColumnIndex(node1) == columnNum && GridPane.getRowIndex(node1) == rowNum) {
                return true;
            }
        }
        return false;
    }

    private int getNumberOfNeighbours(Node node) {
        int numberOfNeighbours = 0;
        int columnIndex = 0;
        int rowIndex = 0;
        try {
            columnIndex = GridPane.getColumnIndex(node);
            rowIndex = GridPane.getRowIndex(node);
            //  System.out.println("Getting neighbours for " + rowIndex + ", " + columnIndex);
            for (Node node1 : compressedGrid) {
                int compareRowIndex = GridPane.getRowIndex(node1);
                int compareColumnIndex = GridPane.getColumnIndex(node1);
                int rowDifference = Math.abs(compareRowIndex - rowIndex);
                int columnDifference = Math.abs(compareColumnIndex - columnIndex);
                //  System.out.println("Is " + compareRowIndex + ", " + compareColumnIndex +" a neighbour?");
                if (containsNodeAlready(selected, node1) && ((columnDifference == 1 && rowDifference < 2) || (rowDifference == 1 && columnDifference < 2))) {
                    // System.out.println("yes");
                    numberOfNeighbours++;
                } else {
                    //System.out.println("no");
                }
            }
        } catch (NullPointerException e) {
            return numberOfNeighbours;
        } catch (ArrayIndexOutOfBoundsException e2) {

        }
        return numberOfNeighbours;
    }

}
