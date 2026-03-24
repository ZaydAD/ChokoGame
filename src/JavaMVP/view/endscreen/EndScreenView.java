package JavaMVP.view.endscreen;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class EndScreenView extends BorderPane {

    private VBox blueBorder;               // Blue border surrounding the white background
    private VBox whiteBackground;          // White background containing all game statistics and buttons
    private Label title;                   // Title of the end screen
    private Label winnerLabel;             // Label to display the winner of the game
    private Label totalTimeLabel;          // Label to display the total play time
    private Label totalMovesLabel;         // Label to display total moves or a custom message
    private Label avgTurnDurationLabel;    // Label to display average turn duration or a custom message
    private Label scoreLabel;              // Label to display the score or a custom message
    private Button returnToMenuButton;     // Button to return to the main menu
    private PieChart winLossChart;


    /**
     * Constructor for `EndScreenView`.
     * Initializes and arranges all UI components for the end screen display.
     */
    public EndScreenView() {
        initialiseNodes();
        layoutNodes();
    }

    /**
     * Initializes all UI components.
     * Configures the styles, alignment, and initial layout for each component.
     */
    private void initialiseNodes() {
        // Green border background
        this.setStyle("-fx-background-color: #38a659; -fx-padding: 10px;");

        // Inner Blue Border
        blueBorder = new VBox(10);
        blueBorder.setStyle("-fx-background-color: #3e8dc2; -fx-padding: 10px;");
        blueBorder.setAlignment(Pos.CENTER);

        // Inner White Background
        whiteBackground = new VBox(10);
        whiteBackground.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-spacing: 10px;");
        whiteBackground.setAlignment(Pos.CENTER);

        // Title
        title = new Label("Game Over");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-background-color: lightgray; -fx-padding: 10px; -fx-border-radius: 10px;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        // Labels for game statistics
        winnerLabel = createLabel("Winner: ");
        totalTimeLabel = createLabel("Total Play Time: ");
        totalMovesLabel = createLabel("==================================");
        avgTurnDurationLabel = createLabel("Play again and try to beat the AI!");
        scoreLabel = createLabel("==================================");

        // PieChart for win/loss
        winLossChart = new PieChart();
        winLossChart.setLabelsVisible(true);
        winLossChart.setLegendVisible(false);
        winLossChart.setClockwise(true);
        winLossChart.setStartAngle(90);

        // Return Button
        returnToMenuButton = new Button("Return to Main Menu");
        returnToMenuButton.setStyle("-fx-background-color: #ed2846; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 5px;");
        returnToMenuButton.setMaxWidth(200);

    }

    /**
     * Arranges the UI components into a structured layout.
     * Combines the white background and blue border with the title, labels, and return button.
     */
    private void layoutNodes() {
        whiteBackground.getChildren().addAll(
                title,
                winnerLabel,
                totalTimeLabel,
                totalMovesLabel,
                avgTurnDurationLabel,
                scoreLabel,
                winLossChart,
                returnToMenuButton
        );

        blueBorder.getChildren().add(whiteBackground);
        this.setCenter(blueBorder);
    }

    public void setWinLossChart(int wins, int losses) {
        winLossChart.getData().clear();
        PieChart.Data winData = new PieChart.Data("Wins", wins);
        PieChart.Data lossData = new PieChart.Data("Losses", losses);
        winLossChart.getData().addAll(winData, lossData);

        Platform.runLater(() -> {
            for (PieChart.Data data : winLossChart.getData()) {
                String label = String.format("%d %s", (int) data.getPieValue(), data.getName());
                Tooltip tooltip = new Tooltip(label);
                tooltip.setShowDelay(Duration.ZERO);
                tooltip.setHideDelay(Duration.ZERO);

                // Fully custom tooltip style – NO shadow, small size
                tooltip.setStyle(
                        "-fx-background-color: #d3d3d3;" +
                                "-fx-background: #d3d3d3;" +
                                "-fx-text-fill: black;" +
                                "-fx-font-size: 18px;" +
                                "-fx-padding: 0px 0px;" +
                                "-fx-border-color: #d3d3d3;" +
                                "-fx-border-width: 1px;" +
                                "-fx-border-radius: 2px;" +
                                "-fx-background-radius: 2px;" +
                                "-fx-effect: none;" +
                                "-fx-background-insets: 0;" +
                                "-fx-box-shadow: none;" +
                                "-fx-shape: null;" +
                                "-fx-skin: null;"
                );

                Tooltip.install(data.getNode(), tooltip);

                data.getNode().setStyle("-fx-cursor: hand;");
            }
        });

    }

    public void setWinLossTitle(int wins, int losses) {
        int total = wins + losses;
        String text = (total == 0)
                ? "No games played yet"
                : String.format("Win Ratio: %d%% (%d of %d)", (int)((wins * 100.0) / total), wins, total);
        winLossChart.setTitle(text);
    }


    /**
     * Helper method to create a styled label.
     *
     * @param text The text content of the label.
     * @return A `Label` object with the provided text and predefined style.
     */
    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        return label;
    }

    public void setWinnerLabel(String winner) {
        winnerLabel.setText("Winner: " + winner);
    }

    public void setGameDurationLabel(String duration) {
        totalTimeLabel.setText("Total Play Time: " + duration);
    }

    public void setTotalMovesLabel(String totalMoves) {
        totalMovesLabel.setText(totalMoves);
    }

    public void setAvgTurnDurationLabel(String avgTurnDuration) {
        avgTurnDurationLabel.setText(avgTurnDuration);
    }

    public void setScoreLabel(String score) {
        scoreLabel.setText(score);
    }

    public Button getReturnToMenuButton() {
        return returnToMenuButton;
    }

    /**
     * Displays the end screen in the specified stage.
     * Sets the scene dimensions and titles the stage as "Game Over".
     *
     * @param stage The stage in which the end screen will be displayed.
     */
    public void showEndScreen(Stage stage) {
        Scene scene = new Scene(this, 600, 400);
        stage.setScene(scene);
        stage.setTitle("Game Over");
        stage.show();
    }
}