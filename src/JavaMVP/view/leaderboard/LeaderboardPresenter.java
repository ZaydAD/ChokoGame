package JavaMVP.view.leaderboard;

import JavaMVP.model.Leaderboard;
import JavaMVP.model.MVPModel;
import JavaMVP.model.PlayerStats;
import JavaMVP.view.UISettings;
import JavaMVP.view.mainscreen.MainScreenPresenter;
import JavaMVP.view.mainscreen.MainScreenView;
import javafx.stage.Stage;
import java.util.List;

public class LeaderboardPresenter {

    private Leaderboard model;
    private LeaderboardView view;
    private UISettings uiSettings;
    private MVPModel modelMVP;

    public LeaderboardPresenter(Leaderboard model, MVPModel modelMVP, LeaderboardView view, UISettings uiSettings) {
        this.model = model;
        this.modelMVP = modelMVP;
        this.view = view;
        this.uiSettings = uiSettings;
        addEventHandlers();
        updateLeaderboardView();
    }

    /**
     * Adds event handlers for the view.
     * Specifically handles the action for the "Go Back to Menu" button, transitioning to the main screen.
     */
    private void addEventHandlers() {
        view.getBackButton().setOnAction(event -> MainScreen(modelMVP.getCurrentPlayer().getUsername()));
    }

    /**
     * Transitions the user back to the main screen.
     * Initializes the main screen view and presenter and sets it as the root of the scene.
     *
     * @param username The username of the current player to display on the main screen.
     */
    public void MainScreen(String username) {
        MainScreenView msView = new MainScreenView(uiSettings);
        msView.setWelcomeText(username);
        MainScreenPresenter msPresenter = new MainScreenPresenter(modelMVP, msView, uiSettings);
        view.getScene().setRoot(msView);
    }

    /**
     * Updates the leaderboard view with current rankings from the model.
     * Retrieves the leaderboard data and passes it to the view for display.
     */
    private void updateLeaderboardView() {
        List<PlayerStats> rankings = model.getLeaderboardData();
        view.updateLeaderboard(rankings);
    }

}