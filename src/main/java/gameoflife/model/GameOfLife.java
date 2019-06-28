package gameoflife.model;

import gameoflife.controller.MainViewController;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends Thread {


    private final List<Point> selected = new ArrayList<>();
    private final int[][] grid;
    private final MainViewController mainViewController;
    private final int count;
    private int speed;
    private static final int MAX_TIME = 300;
    private List<Point> toBeBorn, toDie = new ArrayList<>();
    private List<Point> compressedGrid = new ArrayList<>();

    public GameOfLife(int speed, int[][] grid, MainViewController mainViewController) {
        this.grid = grid;
        this.speed = speed;
        this.mainViewController = mainViewController;
        this.count = grid.length;
        fillSelected();
    }

    private void fillSelected() {

        for (int row = 0; row < count; row++) {
            for (int col = 0; col < count; col++) {
                if (grid[row][col] == 1) {
                    selected.add(new Point(row, col));

                }
            }
        }
    }

    @Override
    public void run() {

        try {
            while (!isInterrupted()) {
                compressedGrid = getCompressedGrid();
                toBeBorn = toBeBorn();
                toDie = toDie();
                mainViewController.setSelected(toDie, toBeBorn);
                updateSelected();
                Thread.sleep(MAX_TIME / speed);
            }
        } catch (InterruptedException | ArrayIndexOutOfBoundsException ignored) {

        }
    }

    private void updateSelected() {
        selected.addAll(toBeBorn);
        selected.removeAll(toDie);
        toBeBorn.forEach(point -> {
            grid[point.x][point.y] = 1;
        });
        toDie.forEach(point -> {
            grid[point.x][point.y] = 0;
        });
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    private List<Point> toBeBorn() {
        List<Point> toBeBorn = new ArrayList<>();
        compressedGrid.forEach(point -> {
            int numberOfNeighbours = getNumberOfNeighbours(point);
            if (!selected.contains(point) && numberOfNeighbours == 3) {
                toBeBorn.add(point);
            }
        });

        return toBeBorn;
    }

    private List<Point> toDie() {
        List<Point> toDie = new ArrayList<>();
        selected.forEach(point -> {
            int numberOfNeighbours = getNumberOfNeighbours(point);
            if (numberOfNeighbours > 3 || numberOfNeighbours < 2) {
                toDie.add(point);
            }
        });
        return toDie;
    }

    private List<Point> getCompressedGrid() {
        compressedGrid.clear();
        for (Point point : selected) {
            int y = point.y;
            int x = point.x;
            addIfNotPresent(point);
            try {
                for (int i = -1; i < 2; i++) {
                    int top = grid[x - 1][y + i];
                    int bottom = grid[x + 1][y + i];

                    if (top == 0) {
                        addIfNotPresent(new Point(x - 1, y + i));
                    }
                    if (bottom == 0) {
                        addIfNotPresent(new Point(x + 1, y + i));
                    }
                }

                for (int i = -1; i < 2; i = i + 2) {
                    if (grid[x][y + i] == 0) {
                        addIfNotPresent(new Point(x, y + i));
                    }
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

    private void addIfNotPresent(Point point) {
        if (compressedGrid.stream().noneMatch(o -> o.x == point.x && o.y == point.y)) {
            compressedGrid.add(point);
        }

    }

    private int getNumberOfNeighbours(Point point) {
        int numberOfNeighbours = 0;
        int y = point.y;
        int x = point.x;
        try {
            for (int i = -1; i < 2; i++) {
                int top = grid[x - 1][y + i];
                int bottom = grid[x + 1][y + i];
                if (top == 1) {
                    numberOfNeighbours++;
                }
                if (bottom == 1) {
                    numberOfNeighbours++;
                }
            }

            for (int i = -1; i < 2; i = i + 2) {
                if (grid[x][y + i] == 1) {
                    numberOfNeighbours++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
          //  e.printStackTrace();
            return numberOfNeighbours;
        }

        return numberOfNeighbours;
    }

}
