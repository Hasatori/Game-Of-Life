package gameoflife.view;

import gameoflife.controller.MainViewController;
import gameoflife.utils.ResourceLoader;
import javafx.application.Platform;
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
    private List<Node> selected = new ArrayList<>();
    private static final String notSelectedStyle = "-fx-background-color:white";
    private static final String selectedStyle = "-fx-background-color:red";

    public MainView(MainViewController mainViewController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("mainView.fxml")));
        this.controller = mainViewController;
        this.startButton = (Button) lookup("#startButton");
        this.stopButton = (Button) lookup("#stopButton");
        this.speedSlider = (Slider) lookup("#speedSlider");
        this.gridSizeSlider = (Slider) lookup("#gridSizeSlider");
        this.centerPane = (Pane) lookup("#centerPane");

        startButton.setOnAction((event -> {

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

        for (int i = 0; i <= count; i++) {
            Pane pane = new Pane();
            pane.setOnMouseClicked(event -> {
                if (pane.getStyle().equals(selectedStyle)) {
                    this.selected.add(pane);
                    pane.setStyle(notSelectedStyle);
                } else {
                    this.selected.remove(pane);
                    pane.setStyle(selectedStyle);
                }

            });
            pane.setStyle(notSelectedStyle);
            pane.setMinSize(size, size);
            result[i] = pane;
        }
        return result;
    }

    public void select(List<Node> selected) {
        Platform.runLater(() -> {
            this.selected.removeAll(selected);
            this.selected.forEach(node -> {
                node.setStyle(notSelectedStyle);
            });
            this.selected = selected;
        });


    }
}
