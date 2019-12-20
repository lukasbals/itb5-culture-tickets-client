package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.communication.IMessageFeed;
import at.fhv.td.communication.ISearchEvent;
import at.fhv.td.communication.dto.EventDetailedViewDTO;
import at.fhv.td.communication.dto.TopicDTO;
import at.fhv.td.jms.Publisher;
import at.fhv.td.jms.Subscriber;
import at.fhv.td.rss.FeedMessage;
import at.fhv.td.rss.FeedMessageEx;
import at.fhv.td.rss.FeedReader;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;

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
    ObservableList<EventDetailedViewDTO> _events;
    @FXML
    TextField _searchFieldEvent;
    @FXML
    TextField _searchFieldArtist;
    @FXML
    TextField _searchFieldLocation;
    @FXML
    DatePicker _searchFieldDate;
    @FXML
    Label _title;
    @FXML
    WebView _descriptionWeb;
    @FXML
    Label _publishDate;
    @FXML
    Label _link;
    @FXML
    Tab _newsTab;
    @FXML
    Tab _createFeedTab;
    @FXML
    ObservableList<FeedMessageEx> _topics;
    @FXML
    GridPane _addNew;
    @FXML
    Button _addButton;
    @FXML
    TextField _feedUrlTextBox;
    @FXML
    TextField _titleTextBox;
    @FXML
    TextArea _descriptionTextArea;
    @FXML
    private TableView<EventDetailedViewDTO> _tableViewEvents;
    @FXML
    private TableView<FeedMessageEx> _tableViewTopics;
    private SimpleBooleanProperty validAdd = new SimpleBooleanProperty(true);
    private IMessageFeed _messageFeed = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initPermissions();
        initUICustomization();
        startNotificationWatcher();
        initSubscriber();
        initSearch();
    }

    private void initSubscriber() {
        if (Subscriber.getInstance() == null) {
            List<String> topics = new ArrayList<>();
            try {
                for (TopicDTO iTopicDTO : _messageFeed.getTopics(Main.getUserName())) {
                    String name = iTopicDTO.getName();
                    topics.add(name);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Subscriber.createInstance(topics, Main.getUserName(), this);
        }
    }

    private void initSearch() {
        try {
            _searchFieldDate.setValue(LocalDate.now());
            _searchEvent = Main.getSession().createSearchEvent();
            searchEvents();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        loadTopics();
        _addButton.disableProperty().bind(validAdd);
    }

    public void searchEvents() throws RemoteException {
        _events.clear();
        List<EventDetailedViewDTO> events = _searchEvent.searchForEvents(
                _searchFieldEvent.getText(),
                _searchFieldArtist.getText(),
                _searchFieldLocation.getText(),
                _searchFieldDate.getValue()
        );
        _events.addAll(events);
    }

    @FXML
    public void selectedEvent(MouseEvent doubleClick) {
        int index = _tableViewEvents.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            EventDetailedViewDTO event = _events.get(index);
            if (doubleClick.getClickCount() == 2) {
                BookingViewGuiController.setIEvent(event);
                UI.changeScene("/fxml_files/eventBookingView.fxml");
            }
        }
    }

    @FXML
    public void selectedTopic(MouseEvent doubleClick) {
        int index = _tableViewTopics.getSelectionModel().getSelectedIndex();

        _newsTab.setStyle(null);
        if (index != -1) {
            FeedMessageEx topic = _topics.get(index);
            if (doubleClick.getClickCount() == 2) {
                _title.setText(topic.getTitle());

                _publishDate.setText(topic.getPublishDate().toString());
                String descriptionStyle = "<div style=\"font-family:Arial;font-size:12px;background-color:#f5f5f5;margin:-7px;\">";
                _descriptionWeb.getEngine().loadContent(descriptionStyle + topic.getDescription() + "</div>");
                _descriptionWeb.autosize();
                _link.setText(topic.getLink());


                _title.setVisible(true);
                _descriptionWeb.setVisible(true);
                _link.setVisible(true);
                _publishDate.setVisible(true);

                _topics.get(index).setRead(true);
                _tableViewTopics.refresh();
                Subscriber.getInstance().acknowledgeMessage(_topics.get(index).getMessageObject());
            }
        }
    }

    private void initUICustomization() {
        _tableViewTopics.setRowFactory(tv -> new TableRow<FeedMessageEx>() {
            @Override
            public void updateItem(FeedMessageEx item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !item.isRead()) {
                    setStyle("-fx-background-color: lightgrey;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    private void initPermissions() {
        try {
            _messageFeed = Main.getSession().createMessageFeed();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (_messageFeed != null) {
            _createFeedTab.setDisable(false);
        }
    }

    private void startNotificationWatcher() {
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        final Runnable notification = () -> Platform.runLater(() -> {
            boolean unreadMessages = _topics.stream().anyMatch(msg -> !msg.isRead());
            int unreadMessagesCount = (int) _topics.stream().filter(msg -> !msg.isRead()).count();
            _newsTab.setText("News (" + unreadMessagesCount + ")");

            if (unreadMessages) {
                if (_newsTab.getStyle() == null) {
                    _newsTab.setStyle("-fx-background-color: dodgerblue;");
                } else {
                    _newsTab.setStyle(null);
                }
            } else {
                _newsTab.setStyle(null);
            }
        });
        final ScheduledFuture<?> notificationHandler = scheduler.scheduleAtFixedRate(notification, 3, 1, SECONDS);
        scheduler.schedule(
                () -> notificationHandler.cancel(true),
                8760,
                HOURS);
    }

    public ObservableList<FeedMessageEx> getMessageList() {
        return _topics;
    }

    @FXML
    public void addButtonClicked() {
        if (isTopicSelected()) {
            Publisher publisher = new Publisher(Main.getUserName());
            if (!_feedUrlTextBox.getText().equals("")) {
                List<FeedMessage> feedMessages = FeedReader.readFeed(_feedUrlTextBox.getText());
                for (FeedMessage feedMessage : feedMessages) {
                    for (String topic : getTopic()) {
                        publisher.publish(topic, feedMessage);
                    }
                }
                showSuccessMessage("Feed", "Successfully send");
            } else {
                FeedMessage message = new FeedMessage();
                message.setTitle(_titleTextBox.getText());
                message.setDescription(_descriptionTextArea.getText());
                message.setPublishDate(new Date());
                message.setLink(" ");
                for (String topic : getTopic()) {
                    publisher.publish(topic, message);
                }
                showSuccessMessage("Message", "Successfully send");
            }
            publisher.close();

            _titleTextBox.clear();
            _descriptionTextArea.clear();
            _feedUrlTextBox.clear();
            for (Node node : _addNew.getChildren()) {
                if (node instanceof CheckBox) {
                    ((CheckBox) node).setSelected(false);
                }
            }
        } else {
            BookingViewGuiController.showErrorMessage("No Topic selected", "Please select a topic");
        }
    }

    private void showSuccessMessage(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private List<String> getTopic() {
        ObservableList<Node> elements = _addNew.getChildren();
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
            List<TopicDTO> topics = _messageFeed.getAllTopics();
            int rowIndex = 9;
            for (TopicDTO topic : topics) {
                CheckBox checkbox = new CheckBox(topic.getName());
                _addNew.add(checkbox, 1, rowIndex);
                rowIndex++;
            }
            _addNew.getChildren().remove(_addButton);
            _addNew.add(_addButton, 1, rowIndex);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean isTopicSelected() {
        ObservableList<Node> elements = _addNew.getChildren();
        boolean isSelected = false;
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
        validAdd.set(!(_feedUrlTextBox.getText() != null || (_titleTextBox.getText() != null && _descriptionTextArea.getText() != null)));
    }
}
