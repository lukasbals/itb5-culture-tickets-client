package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.rmi.interfaces.IClientSessionFactory;
import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import at.fhv.td.rmi.interfaces.ISearchEvent;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Main.class)
public class ListEventsGuiControllerTest {
    @Mock
    private URL _url;
    @Mock
    private ResourceBundle _resourceBundle;
    @Mock
    private IClientSessionFactory _clientSessionFactory;
    @Mock
    private ISearchEvent _searchEvent;
    @Mock
    private List<IEventDetailedViewDTO> _mockEventDetailedViewDTO;
    @Mock
    private ObservableList<IEventDetailedViewDTO> _mockObservableEventDetailedViewDTO;

    @Before
    public void before() throws RemoteException {
        mockStatic(Main.class);
        when(Main.getSessionFactory()).thenReturn(_clientSessionFactory);
        when(_clientSessionFactory.createConnection()).thenReturn(_searchEvent);
        when(_searchEvent.searchForEvents("", "", "", null)).thenReturn(_mockEventDetailedViewDTO);
    }

    @Test
    public void initialize() {
        ListEventsGuiController listEventsGuiController = new ListEventsGuiController();
        listEventsGuiController._events = _mockObservableEventDetailedViewDTO;
        listEventsGuiController.initialize(_url, _resourceBundle);
        assertEquals(listEventsGuiController._searchEvent, _searchEvent);
        assertEquals(listEventsGuiController._events, _mockObservableEventDetailedViewDTO);
    }

    @Test
    public void searchTickets() {
        // TODO: Test searchTickets method if it is implemented
    }
}