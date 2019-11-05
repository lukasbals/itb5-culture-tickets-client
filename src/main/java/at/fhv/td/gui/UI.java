package at.fhv.td.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UI extends Application {
    private static Stage _primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        _primaryStage = primaryStage;
        _primaryStage.setTitle("Culture Tickets");
        _primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/fxml_files/listEvents.fxml"))));
        _primaryStage.show();
    }

    Stage getPrimaryStage() {
        return _primaryStage;
    }

    // This method can be used everywhere to switch the scene
    //        UI.changeScene("/fxml_files/buyTicket.fxml");
    public static void changeScene(String fxml) {
        Parent pane = null;
        try {
            pane = FXMLLoader.load(UI.class.getResource(fxml));
            _primaryStage.getScene().setRoot(pane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void startUI() {
        launch();
    }
}


