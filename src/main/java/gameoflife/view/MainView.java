package gameoflife.view;

import gameoflife.controller.MainViewController;
import gameoflife.utils.ResourceLoader;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MainView extends View {

    private final MainViewController controller;

    private final Button startButton, stopButton, clearButton;
    private final Slider speedSlider, gridSizeSlider;
    private final Pane centerPane;
    private final CheckBox showGridLinesCheckBox;
    private final ComboBox<String> selectionColorComboBox;
    private final ComboBox<SelectionTemplate> selectionTemplateComboBox;
    private final GridPane gridPane = new GridPane();
    private final List<Node> selected = new ArrayList<>();
    private static final String notSelectedStyle = "-fx-background-color:white";
    private static String selectedStyle = "-fx-background-color:green";
    private int[][] grid;
    private int gridSize;
    private static final List<String> SELECTION_COLOR_LIST = new ArrayList<>();

    static {
        SELECTION_COLOR_LIST.add("red");
        SELECTION_COLOR_LIST.add("green");
        SELECTION_COLOR_LIST.add("blue");
    }

    public MainView(MainViewController mainViewController) throws IOException {
        super((Parent) FXMLLoader.load(ResourceLoader.gerResourceURL("mainView.fxml")));
        this.controller = mainViewController;
        this.startButton = (Button) lookup("#startButton");
        this.stopButton = (Button) lookup("#stopButton");
        stopButton.setDisable(true);
        this.clearButton = (Button) lookup("#clearButton");
        this.speedSlider = (Slider) lookup("#speedSlider");
        this.gridSizeSlider = (Slider) lookup("#gridSizeSlider");
        this.centerPane = (Pane) lookup("#centerPane");
        this.showGridLinesCheckBox = (CheckBox) lookup("#showGridLinesCheckBox");
        this.selectionColorComboBox = (ComboBox) lookup("#selectionColorComboBox");
        this.selectionTemplateComboBox = (ComboBox) lookup("#selectionTemplateComboBox");

        selectionTemplateComboBox.getItems().addAll(SelectionTemplate.values());
        selectionTemplateComboBox.getSelectionModel().select(SelectionTemplate.NONE);
        selectionColorComboBox.getItems().addAll(SELECTION_COLOR_LIST);
        selectionColorComboBox.getSelectionModel().select(0);
        setSelectedStyle(selectionColorComboBox.getSelectionModel().getSelectedItem());

        startButton.setOnAction((event -> {
            stopButton.setDisable(false);
            startButton.setDisable(true);
            clearButton.setDisable(true);
            selectionColorComboBox.setDisable(true);
            selectionTemplateComboBox.setDisable(true);
            gridSizeSlider.setDisable(true);
            controller.startGame(speedSlider.getValue(), grid);
        }));

        stopButton.setOnAction(event -> {
            controller.stopGame();
            startButton.setDisable(false);
            stopButton.setDisable(true);
            clearButton.setDisable(false);
            gridSizeSlider.setDisable(false);
            selectionColorComboBox.setDisable(false);
            selectionTemplateComboBox.setDisable(false);
        });
        stopButton.setDisable(true);

        clearButton.setOnAction(event -> {
            gridPane.getChildren().forEach(node -> node.setStyle(notSelectedStyle));
            for (int row = 0; row < gridSize; row++) {
                for (int col = 0; col < gridSize; col++) {
                    grid[row][col] = 0;
                }
            }
            selected.clear();
        });
        gridSizeSlider.setOnMouseDragged(event -> {
            gridSizeSlideAction();
        });
        gridSizeSlider.setOnMouseClicked(event -> {
            gridSizeSlideAction();
        });
        speedSlider.setOnMouseReleased(event -> {
            controller.changeSpeed(speedSlider.getValue());
        });

        selectionColorComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            setSelectedStyle(newValue);
            selected.forEach(node -> {
                node.setStyle(selectedStyle);
            });
        });

        selectionTemplateComboBox.setOnAction((action) -> {
            selectByTemplate(selectionTemplateComboBox.getSelectionModel().getSelectedItem());
        });

        showGridLinesCheckBox.setOnAction(event -> {
            gridPane.setGridLinesVisible(showGridLinesCheckBox.isSelected());
        });
        showGridLinesCheckBox.setSelected(true);
        setGridSize(gridSizeSlider.getMin());
        centerPane.getChildren().add(gridPane);
        speedSlider.setValue(speedSlider.getMax() / 2);

    }

    private void gridSizeSlideAction() {
        gridSize = (int) Math.round(gridSizeSlider.getValue());
        setGridSize(gridSize);
        selected.clear();
    }
    
    private void setGridSize(double count) {

        grid = new int[(int) Math.round(count)][(int) Math.round(count)];
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER);
        gridSize = (int) Math.round(count);

        for (int i = 0; i < count; i++) {
            gridPane.addRow(i, getRow(count, i));
        }
        gridPane.setGridLinesVisible(false);
        gridPane.setGridLinesVisible(showGridLinesCheckBox.isSelected());
    }

    private Node[] getRow(double count, int rowNumber) {
        Node[] result = new Node[(int) Math.round(count)];
        double size = Math.ceil(centerPane.getPrefHeight() / count);

        for (int i = 0; i < count; i++) {
            int finalI = i;
            grid[rowNumber][i] = 0;


            Pane pane = new Pane();

            pane.setOnMouseEntered(event -> {
                if (event.isAltDown() && pane.getStyle().equals(notSelectedStyle)) {
                    this.selected.add(pane);
                    grid[rowNumber][finalI] = 1;
                    pane.setStyle(selectedStyle);
                }
                if (event.isControlDown() && pane.getStyle().equals(selectedStyle)) {
                    this.selected.add(pane);
                    grid[rowNumber][finalI] = 0;
                    pane.setStyle(notSelectedStyle);
                }
            });
            pane.setOnMouseClicked(event -> {
                setPaneAction(pane, finalI, rowNumber);
            });
            pane.setStyle(notSelectedStyle);
           /* Text text = new Text(rowNumber + "," + i);
            text.setTextAlignment(TextAlignment.CENTER);
            pane.getChildren().add(text);
*/

            pane.setMinSize(size, size);
            result[i] = pane;
        }
        return result;
    }


    private void setPaneAction(Pane pane, int column, int row) {

        if (pane.getStyle().equals(selectedStyle)) {
            this.selected.remove(pane);
            pane.setStyle(notSelectedStyle);
            grid[row][column] = 0;

        } else {
            this.selected.add(pane);
            pane.setStyle(selectedStyle);
            grid[row][column] = 1;
        }
    }

    private void selectByTemplate(SelectionTemplate selectionTemplate) {
        clearButton.fire();
        selectionTemplate.getSelection(gridSize).forEach(point -> {
            Node node = gridPane.getChildren().get(point.x * gridSize + point.y);
            selected.add(node);
            node.setStyle(selectedStyle);
            grid[point.x][point.y] = 1;
        });
    }

    private void select(List<Point> selected) {

    }

    public synchronized void select(List<Point> deselected, List<Point> selected) throws InterruptedException {

        Platform.runLater(() -> {
            try {
                selected.forEach(point -> {
                    Node node = gridPane.getChildren().get(point.x * gridSize + point.y);
                    node.setStyle(selectedStyle);
                });
                deselected.forEach(point -> {
                    Node node = gridPane.getChildren().get(point.x * gridSize + point.y);
                    node.setStyle(notSelectedStyle);
                });

            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        });

    }

    private void setSelectedStyle(String color) {
        selectedStyle = String.format("-fx-background-color:%s", color);
    }
}
