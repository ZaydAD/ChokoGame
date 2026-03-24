package JavaMVP.view.startscreen;

import JavaMVP.model.*;
import JavaMVP.view.*;
import JavaMVP.view.mainscreen.MainScreenPresenter;
import JavaMVP.view.mainscreen.MainScreenView;
import javafx.scene.control.Alert;
import JavaMVP.view.loginform.loginformPresenter;
import JavaMVP.view.loginform.loginformView;

import java.net.MalformedURLException;

public class StartScreenPresenter {

    private MVPModel model;
    private StartScreenView view;
    private UISettings uiSettings;

    public StartScreenPresenter(MVPModel model, StartScreenView view, UISettings uiSettings) {
        this.model = model;
        this.view = view;
        this.uiSettings = uiSettings;
        EventHandlers();
    }

    private void EventHandlers() {
        view.getTransition().setOnFinished(event -> {
            Login();
        });
    }

    /**
     * Switches the application from the start screen to the login form view.
     * Initializes the login form view and presenter and applies necessary user interface settings.
     */
    private void Login() {
        loginformView loginView = new loginformView(uiSettings);
        loginformPresenter loginPresenter = new loginformPresenter(loginView, model, uiSettings);
        view.getScene().setRoot(loginView);
        try {
            loginView.getScene().getStylesheets().add(uiSettings.getStyleSheetPath().toUri().toURL().toString());
        } catch (MalformedURLException ex) {
            // Do nothing, if toURL-conversion fails, program can continue
        }

        double squareSize = uiSettings.getLowestRes() / 2.5;
        loginView.getScene().getWindow().sizeToScene();
        loginView.getScene().getWindow().setX((uiSettings.getResX() - squareSize) / 2);
        loginView.getScene().getWindow().setY((uiSettings.getResY() - squareSize) / 2);
        loginView.getScene().getWindow().setHeight(squareSize);
        loginView.getScene().getWindow().setWidth(squareSize);
        loginPresenter.windowsHandler();
    }

    /**
     * Prevents the application window from being closed before the program has fully started.
     * Displays an alert message to inform the user and shows the close request event.
     */
    public void windowsHandler() {
        view.getScene().getWindow().setOnCloseRequest(event -> {
            final Alert stopWindow = new Alert(Alert.AlertType.ERROR);
            stopWindow.setHeaderText("You can not yet close the application.");
            stopWindow.setContentText("Try again after the program has started");
            stopWindow.showAndWait();
            event.consume();
        });
    }
}