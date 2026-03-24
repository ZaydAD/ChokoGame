package JavaMVP.view.rulesscreen;

import JavaMVP.view.UISettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

public class rulesView extends BorderPane {
    private Text titleText;
    private Text rulesContent;
    private Button backButton;
    private UISettings uiSettings;


    public rulesView(UISettings uiSettings) {
        this.uiSettings = uiSettings;
        initialiseNodes();
        layoutNodes();
    }

    /**
     * Initializes the UI components (nodes) for the rules screen.
     * This includes creating and styling the title, rules text, and back button.
     */

    private void initialiseNodes() {
        titleText = new Text("Choko - Game Rules\n\n");
        titleText.setFont(Font.font("Arial", 28));
        titleText.setStyle("-fx-fill: white; -fx-font-weight: bold;");

        rulesContent = new Text(
                "\nChoko is a 2-player game that focuses on strategy, speed, and competitiveness.\n\n" +
                        "🎯 The goal of the game is to capture all of your opponent's pieces!\n" +
                        "🧩 Each player starts with 12 pawns.\n" +
                        "🔄 Pawns can move one square at a time in any direction except diagonally.\n" +
                        "⚔️ To capture an opponent's pawn, jump over it with your own.\n" +
                        "🏆 The first to capture all of the opponent's pieces wins.\n\n" +
                        "May the best player win!"
        );
        rulesContent.setFont(Font.font("Arial", 24));
        rulesContent.setStyle("-fx-fill: white;");

        backButton = new Button("Back to Main Menu");
        backButton.setStyle("-fx-background-color: white; -fx-text-fill: #003366; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-font-size: 14px;");
    }

    /**
     * Configures and arranges the layout of the nodes in the view.
     * Places the components into a vertical box (VBox) and sets alignment, padding, and background styles.
     */

    private void layoutNodes() {
        TextFlow textFlow = new TextFlow(titleText, rulesContent);
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setPadding(new Insets(20));
        textFlow.setLineSpacing(5);
        textFlow.setMaxWidth(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(textFlow);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        VBox contentBox = new VBox(20, scrollPane, backButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(30));
        contentBox.setStyle("-fx-background-color: #38a659; -fx-padding: 40px; -fx-border-radius: 20px; -fx-background-radius: 20px;");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.setStyle("-fx-background-color: #ed2846;");
        this.setCenter(contentBox);
    }

    public Button getBackButton() {
        return backButton;
    }
}