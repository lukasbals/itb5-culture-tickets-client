package at.fhv.td.gui;

import at.fhv.td.rmi.interfaces.ITicketDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class BuyTicketGuiController implements Initializable {
    private static ITicketDTO[] _tickets;

    @FXML
    private GridPane gridPane;

    @FXML
    private Button backButton;

    public static void setAnswer(ITicketDTO[] tickets) {
        _tickets = tickets;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            int i = 0;
            for (; i < _tickets.length; i++) {
                gridPane.add(new Label("Category: " + _tickets[i].getCategoryName() + " - "), 1, i + 2);
                gridPane.add(new Label("Ticketnumber: " + _tickets[i].getTicketNumber()), 2, i + 2);
            }
            gridPane.getChildren().remove(backButton);
            gridPane.add(backButton, 1, i + 2);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void backButtonCliched() {
        UI.changeScene("/fxml_files/listEvents.fxml");
    }
}
