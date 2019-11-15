package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.rmi.interfaces.*;
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
import java.util.List;

public class BookingViewGuiController implements Initializable {

    private SimpleBooleanProperty validRes = new SimpleBooleanProperty(true);
    private static IEventDetailedViewDTO _event;
    private ILoadClient _loadClient;
    private ITicketDTO[] _unavailableTickets;

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

    private List<IClientDTO> clients;

    public void initialize(URL location, ResourceBundle resources) {
        try {
            _loadClient = Main.getSessionFactory().createClient();
            ILoadTicket loadTicket = Main.getSessionFactory().createLoadTicket();
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
        List<String>  placeCategories = Arrays.asList(_event.getPlaceCategories());
        placeCategories.sort(Comparator.naturalOrder());
        for (int i = 0; i < placeCategories.size(); i++) {
            String category = placeCategories.get(i);
            int count = _event.getPlaceCategoriesAmount()[i];
            Float price = _event.getPrices()[i];
            Label priceLabel = new Label(price + " €");
            gridpane.add(new Label("Preis: "), 1, rowIndex);
            gridpane.add(priceLabel, 2, rowIndex);
            gridpane.setConstraints(priceLabel, 2, rowIndex, 2, 1);
            gridpane.add(new Label(category), 0, rowIndex++);
            int colIndex = 1;
            for (int j = 1; j <= count; j++, colIndex++) {
                if ((j - 1) % 10 == 0) {
                    rowIndex++;
                    colIndex = 1;
                }
                CheckBox checkbox = new CheckBox(Integer.toString(j));
                checkbox.setDisable(isAvailable(j, category));
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
        for (int i = 0; i < _unavailableTickets.length; i++) {
            if (_unavailableTickets[i].getCategoryName().equals(castegory) && _unavailableTickets[i].getTicketNumber() == number) {
                return true;
            }
        }
        return false;
    }

    private void loadClients() throws RemoteException {
        clients = _loadClient.loadClients();
        List<String> clientnames = new LinkedList<>();
        for (IClientDTO client : clients) {
            clientnames.add(client.getFirstName() + " " + client.getLastName());
        }
        customername.setItems(FXCollections.observableArrayList(clientnames));
    }

    @FXML
    public void checkAccept() {
        if ((customername.getSelectionModel().getSelectedItem() != null)) {
            validRes.set(false);
        } else {
            validRes.set(true);
        }
    }

    private HashMap<Long, Integer[]> checkCheckboxes() throws RemoteException {
        ObservableList<Node> elements = gridpane.getChildren();
        String category = new String();
        HashMap<Long, Integer[]> ticketNumbers = new HashMap<>();
        for (int i = 0; i < elements.size(); i++) {
            Node node = elements.get(i);
            if (node instanceof Label) {
                Label label = (Label) node;
                if (Arrays.asList(_event.getPlaceCategories()).contains(label.getText())) {
                    category = label.getText();
                    i++;
                    node = elements.get(i);
                    int categoryNumber = -1;
                    List<Integer> tickets = new LinkedList<>();
                    while (node instanceof CheckBox) {
                        CheckBox checkbox = (CheckBox) node;
                        if (checkbox.isSelected() && !checkbox.isDisabled()) {
                            int ticketnumber = Integer.parseInt(checkbox.getText());
                            for (int j = 0; j < _event.getPlaceCategories().length; j++) {
                                if (_event.getPlaceCategories()[j].equals(category)) {
                                    categoryNumber = j;
                                    tickets.add(ticketnumber);
                                }
                            }
                        }
                        i++;
                        node = elements.get(i);
                    }
                    i++;
                    if (tickets.size() > 0) {
                        Integer[] numbers = new Integer[tickets.size()];
                        ticketNumbers.put(_event.getPlaceCategoriesId()[categoryNumber], tickets.toArray(numbers));
                    }
                }
            }
        }
        return ticketNumbers;
    }

    private void showErrorMessage(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error happened.");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void checkMessage(IBuyTicket buy) throws RemoteException{
        if (buy.getMessage().contains("failed")) {
            showErrorMessage("Ticket not available", buy.getMessage());
            UI.changeScene("/fxml_files/eventBookingView.fxml");
        } else {
            BuyTicketGuiController.SetAnswer(buy.getTickets());
            UI.changeScene("/fxml_files/buyTicket.fxml");
        }
    }

    private ITicketDTO createTicketDTO () throws RemoteException {
        ITicketDTO ticket = Main.getSessionFactory().createTicketDTO();
        int clientIndex = customername.getSelectionModel().getSelectedIndex();
        if (clientIndex != -1) {
            IClientDTO client = clients.get(clientIndex);
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
            ITicketDTO ticket = createTicketDTO();
            IBuyTicket buy = Main.getSessionFactory().createBuyTicket();
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
            ITicketDTO ticket = createTicketDTO();
            IBuyTicket buy = Main.getSessionFactory().createBuyTicket();
            buy.reserveTicket(ticket, categoryAndTickets);
            checkMessage(buy);
        }
    }

    @FXML
    public void cancelButtonClicked() {
        UI.changeScene("/fxml_files/listEvents.fxml");
    }

    static void setIEvent(IEventDetailedViewDTO event) {
        _event = event;
    }
}
