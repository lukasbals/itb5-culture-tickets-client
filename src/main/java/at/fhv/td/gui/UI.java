package at.fhv.td.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UI extends Application {
    private static Parent _root;
    private static ListEventsGuiController _listEventGuiController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Culture Tickets");
        primaryStage.setScene(new Scene(MainWindow(this)));
        primaryStage.show();
    }

    private static Parent MainWindow(Application app) throws IOException {
        FXMLLoader loader = new FXMLLoader(app.getClass().getResource("/fxml_files/listEvents.fxml"));
        _root = loader.load();
        _listEventGuiController = loader.getController();
        return _root;
    }

    public static void startUI() {
        launch();
    }
}


