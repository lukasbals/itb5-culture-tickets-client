package at.fhv.td.gui;

import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class DetailedViewGuiController implements Initializable {

    static IEventDetailedViewDTO _event;
    @FXML
    private Button book;

    @FXML
    private Button cancel;

    @FXML
    private Label description;

    @FXML
    private Label category;

    @FXML
    private Label date;

    @FXML
    private Label eventlocation;

    @FXML
    private Label artist;

    @FXML
    private Label price;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            description.setText(_event.getDescription());
            category.setText(_event.getCategory());
            date.setText(_event.getDate().toString());
            eventlocation.setText(_event.getLocation());
            eventlocation.setMaxWidth(180);
            eventlocation.setWrapText(true);
            artist.setText(_event.getArtists());
            if(_event.getPrices().length > 1)  {
                price.setText("from € " + getLowestPrice().toString());
            } else {
                price.setText("€ " + getLowestPrice().toString());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Float getLowestPrice() throws RemoteException {
        Float lowestPrice = _event.getPrices()[0];
        for (Float price : _event.getPrices()) {
            if (price < lowestPrice) {
                lowestPrice = price;
            }
        }
        return lowestPrice;
    }

    @FXML
    public void cancelButtonClicked() {
        UI.changeScene("/fxml_files/listEvents.fxml");
    }

    @FXML
    public void bookButtonClicked() {
        BookingViewGuiController.setIEvent(_event);
        UI.changeScene("/fxml_files/eventBookingView.fxml");
    }

    static void setIEvent(IEventDetailedViewDTO event) {
        _event = event;
    }
}
