package gameoflife.view;

import gameoflife.controller.MainViewController;
import gameoflife.utils.ResourceLoader;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainView extends View {

    private final MainViewController controller;

    private final Button startButton, stopButton, clearButton;
    private final Slider speedSlider, gridSizeSlider;
    private final Pane centerPane;
    private final GridPane gridPane = new GridPane();
    private final List<Node> selected = new ArrayList<>();
    private static final String notSelectedStyle = "-fx-background-color:white";
    private static final String selectedStyle = "-fx-background-color:red";

    public MainView(MainViewController mainViewController) throws IOException {
        super(FXMLLoader.load(ResourceLoader.gerResourceURL("mainView.fxml")));
        this.controller = mainViewController;
        this.startButton = (Button) lookup("#startButton");
        this.stopButton = (Button) lookup("#stopButton");
        this.clearButton = (Button) lookup("#clearButton");
        this.speedSlider = (Slider) lookup("#speedSlider");
        this.gridSizeSlider = (Slider) lookup("#gridSizeSlider");
        this.centerPane = (Pane) lookup("#centerPane");

        startButton.setOnAction((event -> {
            stopButton.setDisable(false);
            startButton.setDisable(true);
            clearButton.setDisable(true);
            controller.startGame(speedSlider.getValue(), gridPane.getChildren(), selected);
        }));

        stopButton.setOnAction(event -> {
            controller.stopGame();
            startButton.setDisable(false);
            stopButton.setDisable(true);
            clearButton.setDisable(false);
        });
        stopButton.setDisable(true);

        clearButton.setOnAction(event -> {
            gridPane.getChildren().forEach(node -> node.setStyle(notSelectedStyle));
            selected.clear();
        });
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
        for (int i = 0; i < count; i++) {
            gridPane.addRow(i, getRow(count,i));
        }
        centerPane.getStyleClass().add("-fx-background-color:black");
        gridPane.setGridLinesVisible(false);
        gridPane.setGridLinesVisible(true);
    }

    private Node[] getRow(double count,int columnNumber) {
        Node[] result = new Node[(int) Math.round(count)];
        double size = Math.ceil(centerPane.getPrefHeight() / count);

        for (int i = 0; i < count; i++) {
            Pane pane = new Pane();
            pane.setOnMouseEntered(event -> {
                if(event.isAltDown()){
                    setPaneAction(pane);
                }
            });
            pane.setOnMouseClicked(event -> {
                setPaneAction(pane);
            });
            pane.setStyle(notSelectedStyle);
           // pane.getChildren().add(new Text(i+","+columnNumber));
            pane.setMinSize(size, size);
            result[i] = pane;
        }
        return result;
    }

    private void setPaneAction(Pane pane) {
        if (pane.getStyle().equals(selectedStyle)) {
            this.selected.remove(pane);
            pane.setStyle(notSelectedStyle);
        } else {
            this.selected.add(pane);
            pane.setStyle(selectedStyle);
        }
    }

    public void select(List<Node> selected) {
        Platform.runLater(() -> {
            gridPane.getChildren().forEach(node -> {
                if (node.getStyle().equals(selectedStyle) && !selected.contains(node)) {
                    node.setStyle(notSelectedStyle);
                }
            });
        });
        selected.forEach(node -> {
            node.setStyle(selectedStyle);
        });
    }
}
