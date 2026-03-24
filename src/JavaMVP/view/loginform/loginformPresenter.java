package JavaMVP.view.loginform;

import JavaMVP.model.MVPModel;
import JavaMVP.view.UISettings;
import JavaMVP.view.mainscreen.MainScreenPresenter;
import JavaMVP.view.mainscreen.MainScreenView;
import javafx.scene.control.Alert;
import JavaMVP.view.registerform.registerView;
import JavaMVP.view.registerform.registerPresenter;

public class loginformPresenter {

    private loginformView view;
    private MVPModel model;
    private UISettings uiSettings;

    public loginformPresenter(loginformView view, MVPModel model, UISettings uiSettings) {
        this.view = view;
        this.model = model;
        this.uiSettings = uiSettings;
        EventHandlers();
    }


    /**
     * Binds event handlers to the buttons in the login form.
     * Handles operations such as logging in, entering guest mode, registering a new user, and exiting the application.
     */
    private void EventHandlers() {
        view.getGuestButton().setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Guest Mode");
            alert.setHeaderText("You are now entering Guest Mode");
            alert.showAndWait();
            model.handleLogin("Guest", "Guest");
            MainScreen("Guest");
        });

        // Exit the game from the login menu
        view.getExitButton().setOnAction(e -> {
            model.handCloseEvent();
        });

        view.getRegisterButton().setOnAction(e -> {
            GoToRegister();
        });

        view.getLoginButton().setOnAction(e -> {
            boolean loggedIn = model.handleLogin(view.getUsername().getText(), view.getPassword().getText());

            if (loggedIn) {
                model.showAlert(Alert.AlertType.INFORMATION, "Login Successful",
                        "Welcome, " + view.getUsername().getText() + "!",
                        "You have successfully logged in.");

                MainScreen(view.getUsername().getText());
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login Failed");
                alert.setContentText("Username or password is incorrect");
                alert.showAndWait();
            }
        });
    }

    /**
     * Navigates the user to the registration form.
     * Initializes the register view and presenter, and sets it as the root of the scene.
     */
    public void GoToRegister() {
        registerView regView = new registerView(uiSettings);
        registerPresenter regPresenter = new registerPresenter(regView, model, uiSettings);
        view.getScene().setRoot(regView);
    }

    /**
     * Handles window events such as closing the login form.
     * Ensures proper termination of the application when the window is closed.
     */
    public void windowsHandler() {
        view.getScene().getWindow().setOnCloseRequest(event -> {
            System.exit(0);
        });
    }

    /**
     * Navigates the user to the main screen.
     * Initializes the main screen view and presenter, updates the welcome message with the username, and sets the view as the root of the scene.
     *
     * @param username The username to display in the main screen's welcome message.
     */
    public void MainScreen(String username) {
        MainScreenView msView = new MainScreenView(uiSettings);
        msView.setWelcomeText(username);
        MainScreenPresenter msPresenter = new MainScreenPresenter(model, msView, uiSettings);
        view.getScene().setRoot(msView);
    }
}