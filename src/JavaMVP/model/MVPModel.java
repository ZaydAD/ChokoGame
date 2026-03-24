package JavaMVP.model;

import JavaMVP.view.loginform.loginformPresenter;
import JavaMVP.view.loginform.loginformView;
import JavaMVP.view.mainscreen.MainScreenPresenter;
import JavaMVP.view.mainscreen.MainScreenView;
import JavaMVP.view.registerform.registerView;
import JavaMVP.view.UISettings;
import javafx.scene.control.Alert;
import JavaMVP.model.JDBC;
import JavaMVP.model.Player;

import java.net.MalformedURLException;

public class MVPModel {

    private GameSession gameSession;
    private JDBC database;
    private UISettings uiSettings;
    private loginformView loginformView;
    private Player currentPlayer;
    private Player player;

    public MVPModel() {
         this.gameSession = new GameSession(this);
         this.database = new JDBC();
         this.uiSettings = new UISettings();
         // complete the code here
    }

    public boolean handleRegister(String username, String password, String confirmPassword) {
        //in this method we will handle the registration of the user
        //in the case the user click the register button without entering any data
        //or other error handling, we will show an alert

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Empty Fields");
            alert.setContentText("Please fill in all fields");
            alert.showAndWait();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Passwords do not match");
            alert.setContentText("Please make sure the passwords match");
            alert.showAndWait();
            return false;
        }

        //create a new player object to link to the database
        player = new Player(username, password);
        try {
            database.addNewPlayer(player); // No return value, but errors handled inside
             return true;// Redirect to login after success
        } catch (Exception e) {
            return false;
        }
    }

    public boolean handleLogin(String username, String password) {
        //check if user is guest
        if (username.equals("Guest") && password.equals("Guest")) {
            setCurrentPlayer(new Player("Guest", "Guest"));
            return true;
        }

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Empty Fields", "Please fill in all fields.");
            return false;
        }

        Player player = database.loadPlayer(username, password);

        if (player != null) {
            this.currentPlayer = player;
            return true;
        } else {
            return false;
        }
    }

    public void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handCloseEvent() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.setHeaderText("Are you sure you want to exit the game?");
        alert.showAndWait();
        if (alert.getResult().getText().equals("OK")) {
            System.exit(0);
        }
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public JDBC getDatabase() { return database; }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
