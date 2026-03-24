package JavaMVP.view.rulesscreen;

import JavaMVP.model.MVPModel;
import JavaMVP.view.UISettings;
import JavaMVP.view.mainscreen.MainScreenPresenter;
import JavaMVP.view.mainscreen.MainScreenView;

public class rulesPresenter {

    private rulesView view;
    private MVPModel model;
    private UISettings uiSettings;

    public rulesPresenter(MVPModel model, rulesView view, UISettings uiSettings) {
        this.view = view;
        this.model = model;
        this.uiSettings = uiSettings;
        EventHandlers();
    }

    /**
     * Configures the event handlers for the rules screen.
     * Specifically handles the action when the "Back" button is pressed,
     * transitioning to the Main Screen.
     */
    private void EventHandlers() {
        view.getBackButton().setOnAction(e -> {
            MainScreen(model.getCurrentPlayer().getUsername());
        });
    }

    /**
     * Transitions the application from the rules screen back to the main screen.
     * Initializes the main screen view and presenter, sets the welcome text for the user,
     * and updates the scene root.
     *
     * @param username The username of the current player to display in the welcome message.
     */
    public void MainScreen(String username) {
        MainScreenView msView = new MainScreenView(uiSettings);
        msView.setWelcomeText(username);
        MainScreenPresenter msPresenter = new MainScreenPresenter(model, msView, uiSettings);
        view.getScene().setRoot(msView);
    }
}