package gameoflife.view;


import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum SelectionTemplate {

    NONE {
        @Override
        public List<Point> getSelection(int gridSize) {
            return new ArrayList<>();
        }
    },

    HIGH_POPULATION() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int numberOfIterations = (int) Math.pow(gridSize, 2);

            for (int i = 0; i < numberOfIterations; i++) {
                int x = RANDOM.nextInt(gridSize);
                int y = RANDOM.nextInt(gridSize);
                selection.add(new Point(x, y));
            }
            return selection;
        }
    }, MEDIUM_POPULATION() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int numberOfIterations = (int) Math.pow(gridSize, 2) / 2;
            for (int i = 0; i < numberOfIterations; i++) {
                int x = RANDOM.nextInt(gridSize);
                int y = RANDOM.nextInt(gridSize);
                selection.add(new Point(x, y));
            }
            return selection;
        }
    }, LOW_POPULATION() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int numberOfIterations = (int) Math.pow(gridSize, 2) / 4;
            for (int i = 0; i < numberOfIterations; i++) {
                int x = RANDOM.nextInt(gridSize);
                int y = RANDOM.nextInt(gridSize);
                selection.add(new Point(x, y));
            }
            return selection;
        }
    }, TEN_CELL_ROW() {
        @Override
        public List<Point> getSelection(int gridSize) {
            int count = 10;
            int offset = count / 2;
            int half = Math.round(gridSize / 2);
            List<Point> selection = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                selection.add(new Point(half, half - offset + i));
            }
            return selection;
        }
    }, EXPLORER() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int half = Math.round(gridSize / 2);

            for (int i = -2; i < 3; i++) {
                if (i == -2 || i == 2) {
                    selection.add(new Point(half + i, half));
                }
                selection.add(new Point(half + i, half - 2));
                selection.add(new Point(half - i, half + 2));
            }
            return selection;
        }
    }, LIGHTWEIGHT_SPACESHIP() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int half = Math.round(gridSize / 2);
            for (int i = -1; i < 3; i++) {
                selection.add(new Point(half - 2, half + i));
            }
            for (int i = -1; i < 1; i++) {
                selection.add(new Point(half + i, half + 2));
            }
            selection.add(new Point(half + 1, half + 1));
            selection.add(new Point(half + 1, half - 2));
            selection.add(new Point(half - 1, half - 2));
            return selection;
        }
    }, TUMBLER() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int half = Math.round(gridSize / 2);
            for (int i = -2; i < 3; i++) {
                selection.add(new Point(half + i, half - 1));
                selection.add(new Point(half + i, half + 1));
            }
            for (int i = -2; i < 0; i++) {
                selection.add(new Point(half + i, half - 2));
                selection.add(new Point(half + i, half + 2));
            }
            selection.add(new Point(half + 3, half - 2));
            selection.add(new Point(half + 3, half + 2));
            for (int i = 1; i < 4; i++) {
                selection.add(new Point(half + i, half - 3));
                selection.add(new Point(half + i, half + 3));
            }
            return selection;
        }
    }, GOSPER_GLIDER_GUN() {
        @Override
        public List<Point> getSelection(int gridSize) {
            List<Point> selection = new ArrayList<>();
            int half = Math.round(gridSize / 2);
            squareStructure(selection, half, -3, -19);
            squareStructure(selection, half, -5, 15);

            twoLStructure(selection, half - 2, half - 10);
            twoLStructure(selection, half - 4, half + 4);

            scytheStructure(selection, half, half - 2);
            scytheStructure(selection, half + 3, half + 17);

            int centerX = half + 8;
            int centerY = half + 6;

            for (int i = -1; i < 2; i++) {
                selection.add(new Point(centerX - 1, centerY + i));
            }
            selection.add(new Point(centerX, centerY - 1));
            selection.add(new Point(centerX + 1, centerY));

            return selection;
        }

        private void twoLStructure(List<Point> selection, int centerX, int centerY) {
            selection.add(new Point(centerX - 1, centerY));
            selection.add(new Point(centerX + 1, centerY));
            for (int i = -1; i < 1; i++) {
                selection.add(new Point(centerX - i, centerY - 1));
                selection.add(new Point(centerX + i, centerY + 1));
            }
        }

        private void squareStructure(List<Point> selection, int center, int xOffset, int yOffset) {
            for (int i = yOffset; i < yOffset + 2; i++) {
                selection.add(new Point(center + xOffset, center + i));
                selection.add(new Point(center + xOffset + 1, center + i));
            }
        }

        private void scytheStructure(List<Point> selection, int centerX, int centerY) {
            for (int i = -1; i < 2; i++) {
                selection.add(new Point(centerX + i, centerY - 1));
            }
            selection.add(new Point(centerX - 1, centerY));
            selection.add(new Point(centerX, centerY + 1));
        }
    };


    private static final Random RANDOM = new Random();

    public abstract List<Point> getSelection(int gridSize);

    private List<Point> filSelection(int gridSize, int numberOfIterations, List<Point> selection) {
        for (int i = 0; i < numberOfIterations; i++) {
            int x = RANDOM.nextInt(gridSize);
            int y = RANDOM.nextInt(gridSize);
            selection.add(new Point(x, y));
        }
        return selection;
    }

    @Override
    public String toString() {

        return StringUtils.capitalize(super.toString().replaceAll("_", " ").toLowerCase());
    }

    ;
}
