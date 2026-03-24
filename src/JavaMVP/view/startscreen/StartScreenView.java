package JavaMVP.view.startscreen;

import JavaMVP.view.UISettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class StartScreenView extends BorderPane {

    private UISettings uiSettings;
    private Label timeDisplay;
    private ProgressBar timeProgress;
    private StartScreenTransition trans;

    public StartScreenView(UISettings uiSettings) {
        this.uiSettings = uiSettings;
        initialiseNodes();
        layoutNodes();
        animate();
    }

    private void initialiseNodes() {
        this.timeDisplay = new Label("Loading: 0.0");
        this.timeProgress = new ProgressBar();
    }

    private void layoutNodes() {
        int ImageSize = uiSettings.getLowestRes() / 5;
        BorderPane progressPane = new BorderPane();
        progressPane.setRight(this.timeProgress);
        progressPane.setLeft(this.timeDisplay);
        BorderPane.setMargin(this.timeDisplay, new Insets(uiSettings.getInsetsMargin()));
        BorderPane.setMargin(this.timeProgress, new Insets(uiSettings.getInsetsMargin()));
        this.setStyle("-fx-background-color: #38a659");

        this.setBottom(progressPane);

        BorderPane blueSquare = new BorderPane();
        blueSquare.setPrefSize(300, 300);
        blueSquare.setStyle("-fx-background-color: #3e8dc2");

        Label gameTitle = new Label("Choko");
        gameTitle.setStyle("-fx-text-fill: white; -fx-font-size: 75px; -fx-font-weight: bold;");

        Label redLine = new Label();
        redLine.setPrefHeight(0.0001);
        redLine.setMaxWidth(200);
        redLine.setStyle("-fx-background-color: #ed2846;");

        // Stack the title and underline vertically
        VBox titleBox = new VBox(5); // Spacing between title and line
        titleBox.setAlignment(Pos.CENTER);
        titleBox.getChildren().addAll(gameTitle, redLine);

        blueSquare.setCenter(titleBox);
        this.setCenter(blueSquare);
    }


    Label getTimeDisplay() {
        return (timeDisplay);
    }

    ProgressBar getTimeProgress() {
        return (timeProgress);
    }

    StartScreenTransition getTransition() {
        return trans;
    }

    private void animate() {
        trans = new StartScreenTransition(this, 3);
        trans.play();
    }

}
