package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.communication.IBuyTicket;
import at.fhv.td.communication.ILoadClient;
import at.fhv.td.communication.ILoadTicket;
import at.fhv.td.communication.dto.ClientDTO;
import at.fhv.td.communication.dto.EventDetailedViewDTO;
import at.fhv.td.communication.dto.TicketDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class BookingViewGuiController implements Initializable {

    private static EventDetailedViewDTO _event;
    private SimpleBooleanProperty validRes = new SimpleBooleanProperty(true);
    private ILoadClient _loadClient;
    private TicketDTO[] _unavailableTickets;

    @FXML
    private Button reserve;

    @FXML
    private ComboBox customername;

    @FXML
    private Label price;

    @FXML
    private GridPane gridpane;

    @FXML
    private HBox buttonBox;

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

    private List<ClientDTO> clients;

    public static void showErrorMessage(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error happened.");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    static void setIEvent(EventDetailedViewDTO event) {
        _event = event;
    }

    public void initialize(URL location, ResourceBundle resources) {
        try {
            _loadClient = Main.getSession().createClient();
            ILoadTicket loadTicket = Main.getSession().createLoadTicket();
            _unavailableTickets = loadTicket.getUnavailableTickets(_event.getEventId());
            loadClients();
            loadTickets();
            loadDetails();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        reserve.disableProperty().bind(validRes);
    }

    private void loadDetails() throws RemoteException {
        description.setText(_event.getDescription());
        category.setText(_event.getCategory());
        date.setText(_event.getDate().toString());
        eventlocation.setText(_event.getLocation());
        artist.setText(_event.getArtists());
        if (_event.getPrices().length > 1) {
            price.setText("from € " + _event.getMinPrice().toString());
        } else {
            price.setText("€ " + _event.getMinPrice().toString());
        }
    }

    private void loadTickets() throws RemoteException {
        int rowIndex = 10;
        List<String> placeCategories = Arrays.asList(_event.getPlaceCategories());
        for (int i = 0; i < placeCategories.size(); i++) {
            String placeCategory = placeCategories.get(i);
            int count = _event.getPlaceCategoriesAmount()[i];
            Float categoryPrice = _event.getPrices()[i];
            Label priceLabel = new Label(categoryPrice + " €");
            gridpane.add(new Label("Preis: "), 1, rowIndex);
            gridpane.add(priceLabel, 2, rowIndex);
            gridpane.setConstraints(priceLabel, 2, rowIndex, 2, 1);
            gridpane.add(new Label(placeCategory), 0, rowIndex++);
            int colIndex = 1;
            for (int j = 1; j <= count; j++, colIndex++) {
                if ((j - 1) % 10 == 0) {
                    rowIndex++;
                    colIndex = 1;
                }
                CheckBox checkbox = new CheckBox(Integer.toString(j));
                checkbox.setDisable(isAvailable(j, placeCategory));
                if (checkbox.isDisabled()) {
                    checkbox.setSelected(true);
                }
                gridpane.add(checkbox, colIndex, rowIndex);
            }
            rowIndex += 2;
        }
        gridpane.getChildren().remove(buttonBox);
        gridpane.add(buttonBox, 0, rowIndex);
    }

    private boolean isAvailable(int number, String castegory) throws RemoteException {
        for (TicketDTO unavailableTicket : _unavailableTickets) {
            if (unavailableTicket.getCategoryName().equals(castegory) && unavailableTicket.getTicketNumber() == number) {
                return true;
            }
        }
        return false;
    }

    private void loadClients() throws RemoteException {
        clients = _loadClient.loadClients();
        List<String> clientnames = new LinkedList<>();
        for (ClientDTO client : clients) {
            clientnames.add(client.getFirstName() + " " + client.getLastName());
        }
        customername.setItems(FXCollections.observableArrayList(clientnames));
    }

    @FXML
    public void checkAccept() {
        validRes.set(customername.getSelectionModel().getSelectedItem() == null);
    }

    private HashMap<Long, Integer[]> checkCheckboxes() throws RemoteException {
        ObservableList<Node> elements = gridpane.getChildren();
        String placeCategory;
        HashMap<Long, Integer[]> ticketNumbers = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            Node node = elements.get(i);
            if (node instanceof Label) {
                Label label = (Label) node;
                if (Arrays.asList(_event.getPlaceCategories()).contains(label.getText())) {
                    placeCategory = label.getText();
                    i++;
                    node = elements.get(i);
                    int categoryNumber = -1;
                    List<Integer> tickets = new LinkedList<>();
                    while (node instanceof CheckBox) {
                        CheckBox checkbox = (CheckBox) node;
                        if (checkbox.isSelected() && !checkbox.isDisabled()) {
                            int ticketnumber = Integer.parseInt(checkbox.getText());
                            for (int j = 0; j < _event.getPlaceCategories().length; j++) {
                                if (_event.getPlaceCategories()[j].equals(placeCategory)) {
                                    categoryNumber = j;
                                    tickets.add(ticketnumber);
                                }
                            }
                        }
                        i++;
                        node = elements.get(i);
                    }
                    i++;
                    if (!tickets.isEmpty()) {
                        Integer[] numbers = new Integer[tickets.size()];
                        ticketNumbers.put(_event.getPlaceCategoriesId()[categoryNumber], tickets.toArray(numbers));
                    }
                }
            }
        }
        return ticketNumbers;
    }

    private void checkMessage(IBuyTicket buy) throws RemoteException {
        if (buy.getMessage().contains("failed")) {
            showErrorMessage("Ticket not available", buy.getMessage());
            UI.changeScene("/fxml_files/eventBookingView.fxml");
        } else {
            BuyTicketGuiController.setAnswer(buy.getTickets());
            UI.changeScene("/fxml_files/buyTicket.fxml");
        }
    }

    private TicketDTO createTicketDTO() throws RemoteException {
        TicketDTO ticket = new TicketDTO();
        int clientIndex = customername.getSelectionModel().getSelectedIndex();
        if (clientIndex != -1) {
            ClientDTO client = clients.get(clientIndex);
            ticket.setClientId(client.getId());
        }
        ticket.setEventId(_event.getEventId());
        return ticket;
    }

    @FXML
    public void buyButtonClicked() throws RemoteException {
        HashMap<Long, Integer[]> categoryAndTickets = checkCheckboxes();
        if (categoryAndTickets.size() == 0) {
            showErrorMessage("No ticket selected", "Please select a ticket");
        } else {
            TicketDTO ticket = createTicketDTO();
            IBuyTicket buy = Main.getSession().createBuyTicket();
            buy.buyTicket(ticket, categoryAndTickets);
            checkMessage(buy);
        }
    }

    @FXML
    public void reserveButtonClicked() throws RemoteException {
        HashMap<Long, Integer[]> categoryAndTickets = checkCheckboxes();
        if (categoryAndTickets.size() == 0) {
            showErrorMessage("No ticket selected", "Please select a ticket");
        } else {
            TicketDTO ticket = createTicketDTO();
            IBuyTicket buy = Main.getSession().createBuyTicket();
            buy.reserveTicket(ticket, categoryAndTickets);
            checkMessage(buy);
        }
    }

    @FXML
    public void cancelButtonClicked() {
        UI.changeScene("/fxml_files/listEvents.fxml");
    }
}
