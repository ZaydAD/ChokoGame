package JavaMVP.view.registerform;

import JavaMVP.model.MVPModel;
import JavaMVP.view.UISettings;
import javafx.scene.control.Alert;
import JavaMVP.view.loginform.loginformView;
import JavaMVP.view.loginform.loginformPresenter;

public class registerPresenter {

    private registerView view;
    private MVPModel model;
    private UISettings uiSettings;

    public registerPresenter(registerView view, MVPModel model, UISettings uiSettings) {
        this.view = view;
        this.model = model;
        this.uiSettings = uiSettings;
        EventHandlers();
    }

    /**
     * Sets up event handlers for the registration view.
     * Handles actions for the "Back to Login" button and the "Register" button:
     * - Navigates to the login screen when the back button is clicked.
     * - Handles user registration when the register button is clicked by using the model.
     * - Displays appropriate alerts for successful or failed registration.
     */
    public void EventHandlers() {
        view.getBackButton().setOnAction(e -> {
            GoToLogin();
        });

        view.getRegisterButton().setOnAction(e -> {
            boolean registered = model.handleRegister(view.getUsernameField().getText(),
                    view.getPasswordField().getText(),
                    view.getConfirmPasswordField().getText());
            if (registered) {
                model.showAlert(Alert.AlertType.INFORMATION, "Success", "Registration Complete", "Your account has been created successfully.");
                GoToLogin();
            } else {
                model.showAlert(Alert.AlertType.ERROR, "Error", "Registration Failed", "Username already exists or another error occurred.");
            }
        });
    }

    /**
     * Navigates from the registration screen to the login screen.
     * Creates a new instance of `loginformView` and `loginformPresenter` and sets it as the new scene root.
     */
    private void GoToLogin() {
        loginformView loginView = new loginformView(uiSettings);
        loginformPresenter loginPresenter = new loginformPresenter(loginView, model, uiSettings);
        view.getScene().setRoot(loginView);
    }
}