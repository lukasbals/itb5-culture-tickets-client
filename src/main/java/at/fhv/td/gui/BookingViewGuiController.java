package at.fhv.td.gui;

import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class BookingViewGuiController implements Initializable {

    static IEventDetailedViewDTO _event;
    @FXML
    private Button reserve;

    @FXML
    private Button buy;

    @FXML
    private Button cancel;

    @FXML
    private ComboBox customername;

    @FXML
    private ComboBox<String> categoryname;

    @FXML
    private Label price;

    @FXML
    private ComboBox ticketnumber;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            //customername.setItems();
            categoryname.setItems(FXCollections.observableArrayList(_event.getPlaceCategories()));
            //price.setTest();
            //ticketnumber.setItems();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void buyButtonClicked() {
        UI.changeScene("/fxml_files/buyTicket.fxml");
    }

    @FXML
    public void reserveButtonClicked() {
        UI.changeScene("/fxml_files/reserveTicket.fxml");
    }

    @FXML
    public void cancelButtonClicked() {
        DetailedViewGuiController.setIEvent(_event);
        UI.changeScene("/fxml_files/detailedView.fxml");
    }

    static void setIEvent(IEventDetailedViewDTO event) {
        _event = event;
    }
}
