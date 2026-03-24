package JavaMVP.view.mainscreen;

import JavaMVP.view.AddOns;
import JavaMVP.view.UISettings;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class MainScreenView extends BorderPane implements AddOns {

    private Label welcome;
    private Button newGameButton;
    private Button leaderboardButton;
    private Button rulesButton;
    private Button exitGameButton;
    private UISettings uiSettings;

    public MainScreenView(UISettings uiSettings) {
        this.uiSettings = uiSettings;
        initialiseNodes();
        layoutNodes();
    }

    /**
     * Initializes the UI components for the main screen.
     * Sets up labels, buttons, and their styles, and applies hover animations to buttons.
     */
    private void initialiseNodes() {

        welcome = new Label("Welcome to Choko");
        welcome.setStyle("-fx-text-fill: black; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Arial';");

        newGameButton = new Button("New Game");
        hoverAnimation(newGameButton);
        leaderboardButton = new Button("Leaderboard");
        hoverAnimation(leaderboardButton);
        rulesButton = new Button("Rules");
        hoverAnimation(rulesButton);
        exitGameButton = new Button("Exit Game");
        hoverAnimation(exitGameButton);

        String buttonStyle = "-fx-background-color: #ed2846; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px; -fx-min-width: 200px;";
        newGameButton.setStyle(buttonStyle);
        leaderboardButton.setStyle(buttonStyle);
        rulesButton.setStyle(buttonStyle);
        exitGameButton.setStyle(buttonStyle);
    }

    /**
     * Arranges the components into a structured layout.
     * Uses a stack of panes with different background colors and a centered VBox containing the welcome label and buttons.
     */
    private void layoutNodes() {
        VBox menuBox = new VBox(15, welcome, newGameButton, leaderboardButton, rulesButton, exitGameButton);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.setStyle("-fx-background-color: white; -fx-padding: 50px; -fx-border-radius: 10px; -fx-background-radius: 10px;");

        StackPane innerBluePane = new StackPane(menuBox);
        innerBluePane.setStyle("-fx-background-color: #3e8dc2; -fx-padding: 50px;");

        StackPane outerGreenPane = new StackPane(innerBluePane);
        outerGreenPane.setStyle("-fx-background-color: #38a659; -fx-padding: 40px;");

        setCenter(outerGreenPane);
    }

    /**
     * Adds hover animation to a button by changing its style when the mouse enters or exits the button area.
     * Comes from the AddOns interface.
     * @param button The `Button` to which the hover animation is applied.
     */
    @Override
    public void hoverAnimation(Button button) {
        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #c71e38; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-padding: 10px; -fx-min-width: 200px; -fx-border-radius: 5px;"));

        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #ed2846; -fx-text-fill: white; -fx-font-size: 14px; " +
                "-fx-font-weight: bold; -fx-padding: 10px; -fx-min-width: 200px; -fx-border-radius: 5px;"));
    }

    /**
     * Updates the welcome text with either a default message or a personalized message based on the provided username.
     *
     * @param username The username of the user, which is used to personalize the welcome message.
     *                 If null or empty, a default welcome message is displayed.
     */
    public void setWelcomeText(String username) {
        if (username == null || username.isEmpty()) {
            welcome.setText("Welcome to Choko");
        } else {
            welcome.setText("Welcome, " + username + "!");
        }
    }

    public Button getNewGameButton() { return newGameButton; }
    public Button getLeaderboardButton() { return leaderboardButton; }
    public Button getRulesButton() { return rulesButton; }
    public Button getExitGameButton() { return exitGameButton; }
}