package gameoflife.view;

import com.sun.org.apache.regexp.internal.RE;

import java.awt.Point;
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
    },MEDIUM_POPULATION() {
         @Override
         public List<Point> getSelection(int gridSize) {
             List<Point> selection = new ArrayList<>();
             int numberOfIterations = (int) Math.pow(gridSize, 2)/2;
             for (int i = 0; i < numberOfIterations; i++) {
                 int x = RANDOM.nextInt(gridSize);
                 int y = RANDOM.nextInt(gridSize);
                 selection.add(new Point(x, y));
             }
             return selection;
         }
     },LOW_POPULATION() {
         @Override
         public List<Point> getSelection(int gridSize) {
             List<Point> selection = new ArrayList<>();
             int numberOfIterations = (int) Math.pow(gridSize, 2)/4;
             for (int i = 0; i < numberOfIterations; i++) {
                 int x = RANDOM.nextInt(gridSize);
                 int y = RANDOM.nextInt(gridSize);
                 selection.add(new Point(x, y));
             }
             return selection;
         }
     };;



    private static final Random RANDOM = new Random();

    public abstract List<Point> getSelection(int gridSize);
     private List<Point> filSelection(int gridSize,int numberOfIterations,List<Point> selection){
         for (int i = 0; i < numberOfIterations; i++) {
             int x = RANDOM.nextInt(gridSize);
             int y = RANDOM.nextInt(gridSize);
             selection.add(new Point(x, y));
         }
         return selection;
     }
}
