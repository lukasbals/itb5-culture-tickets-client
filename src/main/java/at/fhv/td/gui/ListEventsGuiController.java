package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import at.fhv.td.rmi.interfaces.ISearchEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ListEventsGuiController implements Initializable {
    ISearchEvent _searchEvent;
    @FXML
    ObservableList<IEventDetailedViewDTO> _events;
    @FXML
    TextField _searchFieldEvent;
    @FXML
    TextField _searchFieldArtist;
    @FXML
    TextField _searchFieldLocation;
    @FXML
    DatePicker _searchFieldDate;

    @FXML
    private TableView<IEventDetailedViewDTO> tableViewEvents;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _searchFieldDate.setValue(LocalDate.now());

        try {
            _searchEvent = Main.getSession().createSearchEvent();
            searchEvents();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void searchEvents() throws RemoteException {
        _events.clear();
        List<IEventDetailedViewDTO> events = _searchEvent.searchForEvents(
                _searchFieldEvent.getText(),
                _searchFieldArtist.getText(),
                _searchFieldLocation.getText(),
                _searchFieldDate.getValue()
        );
        _events.addAll(events);
    }

    @FXML
    public void selectedEvent(MouseEvent doubleClick) {
        int index = tableViewEvents.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            IEventDetailedViewDTO event = _events.get(index);
            if (doubleClick.getClickCount() == 2) {
                BookingViewGuiController.setIEvent(event);
                UI.changeScene("/fxml_files/eventBookingView.fxml");
            }
        }
    }
}
