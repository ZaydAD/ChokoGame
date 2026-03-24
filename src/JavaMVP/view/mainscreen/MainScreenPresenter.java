package JavaMVP.view.mainscreen;

import JavaMVP.model.*;
import JavaMVP.view.UISettings;
import JavaMVP.view.board.BoardScreenPresenter;
import JavaMVP.view.board.BoardScreenView;
import JavaMVP.view.leaderboard.LeaderboardPresenter;
import JavaMVP.view.leaderboard.LeaderboardView;
import JavaMVP.view.playerselection.PlayerSelectionPresenter;
import JavaMVP.view.playerselection.PlayerSelectionView;
import JavaMVP.view.rulesscreen.rulesView;
import JavaMVP.view.rulesscreen.rulesPresenter;
import javafx.event.Event;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;

public class MainScreenPresenter {

    private MVPModel model;
    private MainScreenView view;
    private UISettings uiSettings;

    public MainScreenPresenter(MVPModel model, MainScreenView view, UISettings uiSettings) {
        this.model = model;
        this.view = view;
        this.uiSettings = uiSettings;
        EventHandlers();
    }

    /**
     * Attaches event handlers to the buttons on the main screen UI.
     * Handles navigation to different screens such as player selection (New Game), leaderboard, rules, and game exit.
     */
    private void EventHandlers() {
        view.getNewGameButton().setOnAction(event -> {
            PlayerSelectionView playerSelectionView = new PlayerSelectionView();
            PlayerSelectionPresenter playerSelectionPresenter = new PlayerSelectionPresenter(playerSelectionView, model, (Stage) view.getScene().getWindow());

            view.getScene().setRoot(playerSelectionView);
        });

        view.getLeaderboardButton().setOnAction(event -> {
            // Handle leaderboard navigation
            JDBC jdbc = new JDBC();
            Leaderboard leaderboard = new Leaderboard(jdbc, model.getGameSession());
            LeaderboardView leaderboardView = new LeaderboardView(uiSettings);
            LeaderboardPresenter leaderboardPresenter = new LeaderboardPresenter(leaderboard, model, leaderboardView, uiSettings);
            view.getScene().setRoot(leaderboardView);

            try {
                leaderboardView.getScene().getStylesheets().add(uiSettings.getStyleSheetPath().toUri().toURL().toString());
            } catch (MalformedURLException ex) {
                // If stylesheet fails, the program can continue
            }

            double squareSize = uiSettings.getLowestRes() / 2.5;

            leaderboardView.getScene().getWindow().sizeToScene();
            leaderboardView.getScene().getWindow().setX((uiSettings.getResX() - squareSize - 700) / 2);
            leaderboardView.getScene().getWindow().setY((uiSettings.getResY() - squareSize - 50) / 2);
            leaderboardView.getScene().getWindow().setHeight(squareSize + 100);
            leaderboardView.getScene().getWindow().setWidth(squareSize + 750);
        });

        view.getRulesButton().setOnAction(event -> {
            // Handle rules navigation
            rulesView rulesView = new rulesView(uiSettings);
            rulesPresenter rulesPresenter = new rulesPresenter(model, rulesView, uiSettings);
            view.getScene().setRoot(rulesView);
        });

        view.getExitGameButton().setOnAction(event -> handleCloseEvent(event));
    }

    /**
     * Handles the close event for the application.
     * Displays a confirmation dialog when the user attempts to exit and takes appropriate action based on the response.
     *
     * @param event The `Event` triggered when the window close action is initiated.
     */
    private void handleCloseEvent(Event event) {
        Alert stopWindow = new Alert(Alert.AlertType.CONFIRMATION);
        stopWindow.setTitle("WARNING!");
        stopWindow.setHeaderText("You're closing the application.");
        stopWindow.setContentText("Are you sure?");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        stopWindow.getButtonTypes().setAll(yesButton, noButton);

        stopWindow.showAndWait();

        if (stopWindow.getResult() == yesButton) {
            if (view.getScene() != null && view.getScene().getWindow() != null) {
                view.getScene().getWindow().hide(); // Close the window properly
            }
        } else {
            event.consume(); // Stop the window from closing
        }
    }
}