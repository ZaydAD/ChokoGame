package JavaMVP.view.playerselection;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PlayerSelectionView extends StackPane {
    private Button blackButton;
    private Button whiteButton;
    private CheckBox dropInitiativeCheckbox;
    private Button startGameButton;

    public PlayerSelectionView() {
        initialiseNodes();
        layoutNodes();
        applyStyles();
    }

    /**
     * Initializes the UI components needed for the player selection screen.
     * This includes buttons for choosing player color, a checkbox for enabling drop initiative,
     * and a button to start the game.
     */
    private void initialiseNodes() {
        blackButton = new Button("Play as Black");
        whiteButton = new Button("Play as White");
        dropInitiativeCheckbox = new CheckBox("Enable Drop Initiative");
        startGameButton = new Button("Start Game");
    }

    /**
     * Arranges the nodes into a structured layout.
     * Creates a vertical box (VBox) for alignment, adds a title,
     * and organizes buttons and checkbox into appropriate sections.
     * Also sets up a full-screen background.
     */
    private void layoutNodes() {
        VBox menuBox = new VBox(15);
        menuBox.setAlignment(Pos.CENTER);

        Text title = new Text("Select Your Options");
        title.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        title.setFill(Color.WHITE);

        HBox colorSelection = new HBox(20, blackButton, whiteButton);
        colorSelection.setAlignment(Pos.CENTER);

        menuBox.getChildren().addAll(title, colorSelection, dropInitiativeCheckbox, startGameButton);

        // Background fill to cover full screen
        Region background = new Region();
        background.setPrefSize(800, 600); // Ensure full coverage
        background.setBackground(new Background(new BackgroundFill(Color.web("#ed2846"), CornerRadii.EMPTY, null)));

        this.getChildren().addAll(background, menuBox);
    }

    /**
     * Applies styles to the UI components.
     * Styles the buttons and checkbox using CSS-like properties,
     * ensuring a uniform and visually appealing UI.
     */
    private void applyStyles() {
        // Button styles
        String buttonStyle = "-fx-background-color: white; -fx-text-fill: black; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10px; " +
                "-fx-min-width: 200px; -fx-border-radius: 10px; -fx-background-radius: 10px;";

        blackButton.setStyle(buttonStyle);
        whiteButton.setStyle(buttonStyle);
        startGameButton.setStyle(buttonStyle);

        // Checkbox style
        dropInitiativeCheckbox.setTextFill(Color.WHITE);
        dropInitiativeCheckbox.setFont(Font.font("Courier New", FontWeight.BOLD, 14));
    }

    public Button getBlackButton() {
        return blackButton;
    }

    public Button getWhiteButton() {
        return whiteButton;
    }

    public CheckBox getDropInitiativeCheckbox() {
        return dropInitiativeCheckbox;
    }

    public Button getStartGameButton() {
        return startGameButton;
    }
}