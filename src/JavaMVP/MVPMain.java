package JavaMVP;

import JavaMVP.view.startscreen.*;
import JavaMVP.model.*;
import JavaMVP.view.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.stage.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MVPMain extends Application {
    //final upload version
    @Override
    public void start(Stage primaryStage) {
        UISettings uiSettings = new UISettings();
        MVPModel model = new MVPModel();
        StartScreenView view = new StartScreenView(uiSettings);

        Scene scene = new Scene(view);
        if (uiSettings.styleSheetAvailable()){
            try {
                scene.getStylesheets().add(uiSettings.getStyleSheetPath().toUri().toURL().toString());
            } catch (MalformedURLException ex) {
                // do nothing, if toURL-conversion fails, program can continue
            }
        }

        primaryStage.setScene(scene);
        primaryStage.setHeight(uiSettings.getLowestRes() / 2.5);
        primaryStage.setWidth(uiSettings.getLowestRes() / 2.5);
        primaryStage.setTitle(uiSettings.getApplicationName());
        if (Files.exists(uiSettings.getApplicationIconPath())) {
             try {
                 primaryStage.getIcons().add(new Image(uiSettings.getApplicationIconPath().toUri().toURL().toString()));
             }
             catch (MalformedURLException ex) {
                 // do nothing, if toURL-conversion fails, program can continue
             }
        } else { // do nothing, if ApplicationIcon is not available, program can continue

        }

        StartScreenPresenter presenter = new StartScreenPresenter(model, view, uiSettings);
        presenter.windowsHandler();
        primaryStage.show();
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:postgresql://10.134.178.38:5432/game", "game", "7sur7");
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        launch(args);
    }
}