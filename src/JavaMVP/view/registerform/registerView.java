package JavaMVP.view.registerform;

import javafx.scene.layout.BorderPane;
import JavaMVP.view.UISettings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.VBox;

public class registerView extends BorderPane {

    private UISettings uiSettings;
    private Label titleLabel;
    private TextField usernameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;
    private Button backButton;


    public registerView(UISettings uiSettings) {
        this.uiSettings = uiSettings;
        initialiseNodes();
        layoutNodes();
    }

    /**
     * Initializes the UI components for the registration form.
     * This includes labels, input fields for username and passwords, and buttons for actions.
     */
    private void initialiseNodes() {
        // Title Label
        titleLabel = new Label("Create an Account");
        titleLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-font-family: 'Arial';");

        // Input Fields
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-font-size: 16px; -fx-pref-height: 40px; -fx-pref-width: 300px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: white; -fx-background-color: #ffffff;");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-font-size: 16px; -fx-pref-height: 40px; -fx-pref-width: 300px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: white; -fx-background-color: #ffffff;");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setStyle("-fx-font-size: 16px; -fx-pref-height: 40px; -fx-pref-width: 300px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: white; -fx-background-color: #ffffff;");

        // Buttons
        registerButton = new Button("Register");
        backButton = new Button("Back to Login");

        // Styling Buttons
        String buttonStyle = "-fx-background-color: white; -fx-text-fill: #003366; -fx-font-weight: bold; -fx-padding: 10px 20px; -fx-border-radius: 10px; -fx-background-radius: 10px; -fx-font-size: 14px;";
        registerButton.setStyle(buttonStyle);
        backButton.setStyle(buttonStyle);
    }

    /**
     * Lays out the UI components in a structured format.
     * Organizes the title, input fields, and buttons into a vertical box (VBox),
     * applies padding, alignment, and styles for the layout.
     */
    private void layoutNodes() {
        VBox formBox = new VBox(15);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: #b5121b; -fx-padding: 40px; -fx-border-radius: 20px; -fx-background-radius: 20px;");

        VBox inputFields = new VBox(12);
        inputFields.setAlignment(Pos.CENTER);
        inputFields.getChildren().addAll(usernameField, passwordField, confirmPasswordField);

        VBox buttonBox = new VBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(registerButton, backButton);

        formBox.getChildren().addAll(titleLabel, inputFields, buttonBox);
        this.setCenter(formBox);
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getBackButton() {
        return backButton;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public PasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }
}