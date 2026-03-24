package JavaMVP.view.leaderboard;

import JavaMVP.model.PlayerStats;
import JavaMVP.view.AddOns;
import JavaMVP.view.UISettings;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class LeaderboardView extends BorderPane implements AddOns {

    private VBox blueBorder;
    private VBox whiteBackground;
    private Label title;
    private GridPane headerGrid;
    private VBox leaderboardEntries;
    private ScrollPane scrollPane;
    private Button backButton;

    public LeaderboardView(UISettings uiSettings) {
        initialiseNodes();
        layoutNodes();
    }

    /**
     * Initializes all UI components needed for the leaderboard.
     * Configures styles and layouts for labels, buttons, and grid layouts.
     */
    private void initialiseNodes() {
        this.setStyle("-fx-background-color: #38a659; -fx-padding: 10px;");

        blueBorder = new VBox();
        blueBorder.setStyle("-fx-background-color: #3e8dc2; -fx-padding: 10px;");

        whiteBackground = new VBox();
        whiteBackground.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-spacing: 10px;");

        title = new Label("Leaderboard");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-alignment: center; " +
                "-fx-background-color: lightgray; -fx-padding: 10px; -fx-border-radius: 10px;");
        title.setMaxWidth(Double.MAX_VALUE);
        title.setAlignment(Pos.CENTER);

        headerGrid = createLeaderboardGrid();
        headerGrid.setStyle("-fx-background-color: black;");

        String[] headers = {"Rank", "Name", "# Played", "# Won", "# Lost", "Win %", "Avg. # Turns", "Avg. Turn Duration"};
        for (int i = 0; i < headers.length; i++) {
            Label headerLabel = new Label(headers[i]);
            headerLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-padding: 5px; -fx-font-family: 'Californian FB'; -fx-font-size: 15px;");
            headerLabel.setMaxWidth(Double.MAX_VALUE);
            headerLabel.setAlignment(Pos.CENTER);
            GridPane.setHalignment(headerLabel, HPos.CENTER);
            headerGrid.add(headerLabel, i, 0);
        }

        leaderboardEntries = new VBox(5);
        leaderboardEntries.setPadding(new Insets(10));

        scrollPane = new ScrollPane(leaderboardEntries);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-border-color: transparent;");

        backButton = new Button("Go Back to Menu");
        backButton.setStyle("-fx-background-color: #ed2846; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 5px;");
        backButton.setMaxWidth(Double.MAX_VALUE);
        hoverAnimation(backButton);
    }

    /**
     * Arranges the leaderboard's components into a structured layout.
     * Combines the blue border, white background, title, header, and scrollable leaderboard entries into the `BorderPane`.
     * Places the back button at the bottom.
     */
    private void layoutNodes() {
        whiteBackground.getChildren().addAll(title, headerGrid, scrollPane);
        blueBorder.getChildren().add(whiteBackground);
        this.setCenter(blueBorder);
        this.setBottom(backButton);
    }

    /**
     * Creates a standard `GridPane` for the leaderboard, configuring column widths, spacing, and padding.
     *
     * @return A `GridPane` object configured for leaderboard data.
     */
    private GridPane createLeaderboardGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setPadding(new Insets(5));
        grid.setAlignment(Pos.CENTER);
        grid.setPrefWidth(800);

        for (int i = 0; i < 8; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(12.5);
            grid.getColumnConstraints().add(col);
        }
        return grid;
    }

    /**
     * Updates the leaderboard with a list of player statistics.
     * Clears previous entries and populates new entries from the rankings list.
     *
     * @param rankings A list of `PlayerStats` containing player ranking details.
     */
    public void updateLeaderboard(List<PlayerStats> rankings) {
        leaderboardEntries.getChildren().clear();

        for (PlayerStats player : rankings) {
            leaderboardEntries.getChildren().add(createLeaderboardEntry(
                    "#" + player.getRank(),
                    player.getPlayerName(),
                    String.valueOf(player.getGamesPlayed()),
                    String.valueOf(player.getGamesWon()),
                    String.valueOf(player.getGamesLost()),
                    player.getWinPercentage() + "%",
                    String.format("%.2f", player.getAvgTurnsPerGame()),
                    String.format("%.2f", player.getAvgTurnDuration())
            ));
        }
    }

    /**
     * Creates a `GridPane` representing a single leaderboard entry.
     *
     * @param rank The rank of the player.
     * @param name The player's name.
     * @param played The number of games played by the player.
     * @param won The number of games won by the player.
     * @param lost The number of games lost by the player.
     * @param winPercent The player's win percentage.
     * @param avgTurns The average number of turns per game.
     * @param avgTime The average turn duration for the player.
     * @return A `GridPane` object displaying the player's stats.
     */
    private GridPane createLeaderboardEntry(String rank, String name, String played, String won, String lost, String winPercent, String avgTurns, String avgTime) {
        GridPane entryGrid = createLeaderboardGrid();
        entryGrid.setStyle("-fx-background-color: #ed2846; -fx-padding: 5px; -fx-border-radius: 5px;");

        String[] values = {rank, name, played, won, lost, winPercent, avgTurns, avgTime};
        for (int i = 0; i < values.length; i++) {
            Label entryLabel = new Label(values[i]);
            entryLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            entryLabel.setAlignment(Pos.CENTER);
            GridPane.setHalignment(entryLabel, HPos.CENTER);
            entryGrid.add(entryLabel, i, 0);
        }
        return entryGrid;
    }

    @Override
    public void hoverAnimation(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c71e38; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 5px;"));

        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #ed2846; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-padding: 10px; -fx-border-radius: 5px;"));
    }

    public Button getBackButton() {
        return backButton;
    }

    public void showLeaderboard(Stage stage) {
        Scene scene = new Scene(this, 800, 600);
        stage.setScene(scene);
        stage.setTitle("Leaderboard");
        stage.show();
    }
}