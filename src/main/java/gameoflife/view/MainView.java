package gameoflife.view;

import gameoflife.controller.MainViewController;
import gameoflife.utils.ResourceLoader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainView extends View {

    private final MainViewController controller;

    private final Button startButton, stopButton;
    private final Slider speedSlider, gridSizeSlider;
    private final Pane centerPane;
    private final GridPane gridPane = new GridPane();
    private final List<Node> selected = new ArrayList<>();

    public MainView(MainViewController mainViewController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("mainView.fxml")));
        this.controller = mainViewController;
        this.startButton = (Button) lookup("#startButton");
        this.stopButton = (Button) lookup("#stopButton");
        this.speedSlider = (Slider) lookup("#speedSlider");
        this.gridSizeSlider = (Slider) lookup("#gridSizeSlider");
        this.centerPane = (Pane) lookup("#centerPane");

        startButton.setOnAction((event -> {
            controller.startGame(speedSlider.getValue(), gridSizeSlider.getValue());
        }));

        stopButton.setOnAction(event -> {
            controller.stopGame();
        });
        stopButton.setDisable(true);

        gridSizeSlider.setOnMouseDragged(event -> {
            setGridSize(Math.round(gridSizeSlider.getValue()));
        });

        speedSlider.setOnMouseReleased(event -> {
            controller.changeSpeed(speedSlider.getValue());
        });

        setGridSize(gridSizeSlider.getMin());
        centerPane.getChildren().add(gridPane);
    }

    private void setGridSize(double count) {
        gridPane.getChildren().clear();
        gridPane.setAlignment(Pos.CENTER);
        for (int i = 0; i <= count; i++) {
            gridPane.addRow(i, getRow(count));
        }
        centerPane.getStyleClass().add("-fx-background-color:black");
        gridPane.setGridLinesVisible(false);
        gridPane.setGridLinesVisible(true);
    }

    private Node[] getRow(double count) {
        Node[] result = new Node[(int) Math.round(count + 1)];
        double size = Math.ceil(centerPane.getPrefHeight() / count);
        String notSelected = "-fx-background-color:white";
        String selected = "-fx-background-color:red";
        for (int i = 0; i <= count; i++) {
            Pane pane = new Pane();
            pane.setOnMouseClicked(event -> {
                if (pane.getStyle().equals(selected)) {
                    this.selected.add(pane);
                    pane.setStyle(notSelected);
                } else {
                    this.selected.remove(pane);
                    pane.setStyle(selected);
                }

            });
            pane.setStyle(notSelected);
            pane.setMinSize(size, size);
            result[i] = pane;
        }
        return result;
    }
}
