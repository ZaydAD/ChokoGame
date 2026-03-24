package JavaMVP.view.endscreen;

import JavaMVP.model.GameSession;
import JavaMVP.model.Leaderboard;
import JavaMVP.model.MVPModel;
import JavaMVP.view.UISettings;
import JavaMVP.view.mainscreen.MainScreenPresenter;
import JavaMVP.view.mainscreen.MainScreenView;
import enums.GameStatus;


public class EndScreenPresenter {

    private final EndScreenView view;      // The end screen view
    private final UISettings uiSettings;  // The application's UI settings
    private final MVPModel model;         // The application's model, including the game session and player info

    public EndScreenPresenter(EndScreenView view, MVPModel model, UISettings uiSettings, GameSession ignored) {
        this.view = view;
        this.uiSettings = uiSettings;
        this.model = model;

        addEventHandlers();
        updateView();
    }

    /**
     * Adds event handlers for the end screen.
     * Configures the "Return to Main Menu" button to navigate back to the main menu.
     */
    private void addEventHandlers() {
        view.getReturnToMenuButton().setOnAction(e -> MainScreen(model.getCurrentPlayer().getUsername()));
    }

    /**
     * Transitions the player to the main menu.
     * Initializes the main screen view and presenter, and swaps the current scene to the main screen.
     *
     * @param username The username of the current player to display on the main screen.
     */
    private void MainScreen(String username) {
        MainScreenView msView = new MainScreenView(uiSettings);
        msView.setWelcomeText(username);
        MainScreenPresenter msPresenter = new MainScreenPresenter(model, msView, uiSettings);
        view.getScene().setRoot(msView);
    }

    /**
     * Updates the end screen view with game session data.
     * Displays the winner, total play time, total moves, average turn duration, and score based on the game time.
     *
     * @throws IllegalStateException if the current player is `null`.
     */
    public void updateView() {
        if (model.getCurrentPlayer() == null) {
            throw new IllegalStateException("Current player is null");
        }

        GameSession gameSession = model.getGameSession();
        GameStatus status = gameSession.getGameStatus();

        // Set the winner label based on the game's status (WON, LOST, or DRAW)
        if (status == GameStatus.WON) {
            view.setWinnerLabel(model.getCurrentPlayer().getUsername() + "!");
        } else if (status == GameStatus.LOST) {
            view.setWinnerLabel("The AI won!");
        } else {
            view.setWinnerLabel("friendship");
        }

        // Set the total game duration
        long gameDuration = gameSession.getGameDuration();
        long minutes = (gameDuration / 1000) / 60;
        long seconds = (gameDuration / 1000) % 60;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        view.setGameDurationLabel(timeString);

        // Display additional stats if the game was won
        if (status == GameStatus.WON) {
            view.setTotalMovesLabel("Total Moves: " + gameSession.getUserMoves());
            view.setAvgTurnDurationLabel("Average Turn Duration: " + gameSession.getAvgTurnDurationAsString(gameSession.getAvgTurnDuration()) + "s");
            view.setScoreLabel("Final Score: " + gameSession.getScore());
        }

        String username = model.getCurrentPlayer().getUsername();
        if (!"Guest".equalsIgnoreCase(username)) {
            Leaderboard leaderboard = new Leaderboard(model.getDatabase(), model.getGameSession());
            int[] winLoss = leaderboard.getWinsAndLosses(username);
            view.setWinLossChart(winLoss[0], winLoss[1]);
            view.setWinLossTitle(winLoss[0], winLoss[1]);
        }

    }
}