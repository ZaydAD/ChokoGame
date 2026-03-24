package JavaMVP.view.loginform;

import JavaMVP.view.UISettings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class loginformView extends BorderPane {

    private UISettings uiSettings;
    private Label title;
    private TextField username;
    private PasswordField password;
    private Button loginButton;
    private Button registerButton;
    private Button exitButton;
    private Button guestButton;

    public loginformView(UISettings uiSettings) {
        this.uiSettings = uiSettings;
        initialiseNodes();
        layoutNodes();
    }

    /**
     * Initializes all UI components for the login form.
     * Sets up labels, text fields, password fields, and buttons with their styles.
     */
    public void initialiseNodes() {
        title = new Label("Welcome to Choko, log in");
        title.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold;");

        username = new TextField();
        username.setPromptText("Username");
        username.setStyle("-fx-font-size: 16px; -fx-pref-height: 40px; -fx-pref-width: 300px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: white; -fx-background-color: #ffffff;");

        password = new PasswordField();
        password.setPromptText("Password");
        password.setStyle("-fx-font-size: 16px; -fx-pref-height: 40px; -fx-pref-width: 300px; -fx-border-radius: 15px; -fx-background-radius: 15px; -fx-border-color: white; -fx-background-color: #ffffff;");

        // Buttons
        loginButton = new Button("Login");
        registerButton = new Button("Register");
        guestButton = new Button("Continue as Guest");
        exitButton = new Button("Exit");

        // Button Style
        String buttonStyle = "-fx-background-color: white; -fx-text-fill: #003366; -fx-font-weight: bold; -fx-padding: 5px 15px; -fx-border-radius: 10px;";
        loginButton.setStyle(buttonStyle);
        registerButton.setStyle(buttonStyle);
        guestButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
    }

    /**
     * Arranges and styles the login form's components in the layout.
     * Uses a VBox for vertical arrangement of the components and sets alignment and padding.
     * Applies background color to differentiate the form's section.
     */
    public void layoutNodes() {
        VBox loginBox = new VBox(10, title, username, password, loginButton, registerButton, guestButton, exitButton);
        loginBox.setAlignment(Pos.CENTER);
        loginBox.setPadding(new Insets(20));
        loginBox.setStyle("-fx-background-color: #ed2846");

        this.setCenter(loginBox);
    }

    public TextField getUsername() {
        return username;
    }

    public PasswordField getPassword() {
        return password;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }

    public Button getExitButton() {
        return exitButton;
    }

    public Button getGuestButton() {
        return guestButton;
    }
}