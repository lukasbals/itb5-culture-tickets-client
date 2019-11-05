package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import at.fhv.td.rmi.interfaces.ISearchEvent;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
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
    private TableView<IEventDetailedViewDTO> tableViewEvents;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            _searchEvent = Main.getSessionFactory().createConnection();
            this.loadEvents();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void loadEvents() throws RemoteException {
        _events.clear();
        List<IEventDetailedViewDTO> events = _searchEvent.searchForEvents("", "", "", LocalDate.now());
        _events.addAll(events);
    }

    public void searchTickets() {
        // TODO: Implement the search logic
    }

    @FXML
    public void selectedEvent(MouseEvent doubleClick) {
        int index = tableViewEvents.getSelectionModel().getSelectedIndex();
        IEventDetailedViewDTO event = _events.get(index);
        if (doubleClick.getClickCount() == 2) {
            DetailedViewGuiController.setIEvent(event);
            UI.changeScene("/fxml_files/detailedView.fxml");
        }
    }
}
