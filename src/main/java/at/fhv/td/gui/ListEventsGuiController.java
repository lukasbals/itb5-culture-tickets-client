package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.jms.Publisher;
import at.fhv.td.jms.Subscriber;
import at.fhv.td.rmi.interfaces.*;
import at.fhv.td.rss.FeedMessage;
import at.fhv.td.rss.FeedMessageEx;
import at.fhv.td.rss.FeedReader;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.SECONDS;

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


    @FXML
    Label title;
    @FXML
    Label description;
    @FXML
    Label publishDate;
    @FXML
    Label link;
    @FXML
    Tab _newsTab;
    @FXML
    ObservableList<FeedMessageEx> _topics;

    @FXML
    private TableView<FeedMessageEx> tableViewTopics;

    @FXML
    GridPane addNew;

    @FXML
    Button addButton;
    @FXML
    TextField feedUrlTextBox;
    @FXML
    TextField titelTextBox;
    @FXML
    TextField descriptionTextBox;
    private SimpleBooleanProperty validAdd = new SimpleBooleanProperty(true);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        _searchFieldDate.setValue(LocalDate.now());

        List<String> topics = new ArrayList<>();
        try {
            for (ITopicDTO iTopicDTO : Main.getSession().createMessageFeed().getTopics(Main.getUserName())) {
                String name = iTopicDTO.getName();
                topics.add(name);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Subscriber.createInstance(topics, Main.getUserName(), this);


        notification();
        try {
            _searchEvent = Main.getSession().createSearchEvent();
            searchEvents();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        loadTopics();
        addButton.disableProperty().bind(validAdd);
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

    @FXML
    public void selectedTopic(MouseEvent doubleClick) {
        int index = tableViewTopics.getSelectionModel().getSelectedIndex();

        _newsTab.setStyle(null);
        if (index != -1) {
            FeedMessageEx topic = _topics.get(index);
            if (doubleClick.getClickCount() == 2) {
                title.setText(topic.getTitle());

                publishDate.setText(topic.getPublishDate().toString());
                description.setText(topic.getDescription());
                link.setText(topic.getLink());

                title.setVisible(true);
                description.setVisible(true);
                link.setVisible(true);
                publishDate.setVisible(true);

                _topics.get(index).setRead(true);
                tableViewTopics.refresh();
                Subscriber.getInstance().acknowledgeMessage(_topics.get(index).getMessageObject());

            }
        }
    }

    public void notification() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable notification = new Runnable() {
            public void run() {
                boolean someOneIsUnRead = _topics.stream().anyMatch(feed -> !feed.isRead());

                if (someOneIsUnRead) {
                    if (_newsTab.getStyle() == null) {
                        _newsTab.setStyle("-fx-background-color: red;");
                    } else {
                        _newsTab.setStyle(null);
                    }
                } else {
                    _newsTab.setStyle(null);
                }
            }
        };
        final ScheduledFuture<?> notificationHandler = scheduler.scheduleAtFixedRate(notification, 3, 1, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() {
                notificationHandler.cancel(true);
            }
        }, 8760, HOURS);
    }

    public ObservableList<FeedMessageEx> getMessageList() {
        return _topics;
    }

    @FXML
    public void addButtonClicked() {
        if (isTopicSelected()) {
            Publisher publisher = new Publisher(Main.getUserName());
            if (!feedUrlTextBox.getText().equals("")) {
                List<FeedMessage> feedMessages = FeedReader.readFeed(feedUrlTextBox.getText());
                for (FeedMessage feedMessage : feedMessages) {
                    for (String topic : getTopic()) {
                        publisher.publish(topic, feedMessage);
                    }
                }
                showSuccessMessage("Feed", "Successfully send");
            } else {
                FeedMessage message = new FeedMessage();
                message.setTitle(titelTextBox.getText());
                message.setDescription(descriptionTextBox.getText());
                message.setPublishDate(new Date());
                message.setLink(" ");
                for (String topic : getTopic()) {
                    publisher.publish(topic, message);
                }
                showSuccessMessage("Message", "Successfully send");
            }
            publisher.close();
        } else {
            BookingViewGuiController.showErrorMessage("No Topic selected", "Please select a topic");
        }
    }

    private void showSuccessMessage(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<String> getTopic() {
        ObservableList<Node> elements = addNew.getChildren();
        List<String> selectedTopics = new LinkedList<>();
        for (Node node : elements) {
            if (node instanceof CheckBox) {
                CheckBox checkbox = (CheckBox) node;
                if (checkbox.isSelected()) {
                    selectedTopics.add(checkbox.getText());
                }
            }
        }
        return selectedTopics;
    }

    private void loadTopics() {
        try {
            List<ITopicDTO> topics = Main.getSession().createMessageFeed().getAllTopics();
            int rowIndex = 9;
            for (ITopicDTO topic : topics) {
                CheckBox checkbox = new CheckBox(topic.getName());
                addNew.add(checkbox, 1, rowIndex);
                rowIndex++;
            }
            addNew.getChildren().remove(addButton);
            addNew.add(addButton, 1, rowIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean isTopicSelected() {
        ObservableList<Node> elements = addNew.getChildren();
        Boolean isSelected = false;
        for (Node node : elements) {
            if (node instanceof CheckBox) {
                CheckBox checkbox = (CheckBox) node;
                if (checkbox.isSelected()) {
                    isSelected = true;
                }
            }
        }
        return isSelected;
    }

    @FXML
    public void checkAccept() {
        if (feedUrlTextBox.getText() != null) {
            validAdd.set(false);
        } else if (titelTextBox.getText() != null && descriptionTextBox.getText() != null) {
            validAdd.set(false);
        } else {
            validAdd.set(true);
        }
    }
}
