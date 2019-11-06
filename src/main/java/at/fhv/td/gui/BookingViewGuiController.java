package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.rmi.interfaces.IClientDTO;
import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import at.fhv.td.rmi.interfaces.ILoadClient;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class BookingViewGuiController implements Initializable {

    private SimpleBooleanProperty valid = new SimpleBooleanProperty(true);
    static IEventDetailedViewDTO _event;
    ILoadClient _loadClient;

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
            _loadClient = Main.getSessionFactory().createClient();
            LoadClients();
            LoadCategories();
            if (_event.isSeatReservationPossible()) {
                //ticketnumber.setItems();
            } else {
                ticketnumber.setDisable(!ticketnumber.isDisabled());
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        reserve.disableProperty().bind(valid);
        buy.disableProperty().bind(valid);
    }

    private void LoadClients() throws RemoteException {
        List<IClientDTO> clients = _loadClient.loadClients();
        List<String> clientnames = new LinkedList<>();
        for (IClientDTO client: clients) {
            clientnames.add(client.getFirstName() + " " + client.getLastName());
        }
        customername.setItems(FXCollections.observableArrayList(clientnames));
    }

    private void LoadCategories() throws RemoteException {
        categoryname.setItems(FXCollections.observableArrayList(_event.getPlaceCategories()));
        categoryname.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                try {
                    price.setText(_event.getPrices()[categoryname.getSelectionModel().getSelectedIndex()].toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void checkAccept() {
        if ((categoryname.getSelectionModel().getSelectedItem() != null) && (customername.getSelectionModel().getSelectedItem() != null)) {
            valid.set(false);
        } else {
            valid.set(true);
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
