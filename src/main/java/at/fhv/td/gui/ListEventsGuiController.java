package at.fhv.td.gui;

import at.fhv.td.Main;
import at.fhv.td.jms.Subscriber;
import at.fhv.td.rmi.interfaces.IEventDetailedViewDTO;
import at.fhv.td.rmi.interfaces.ISearchEvent;
import at.fhv.td.rmi.interfaces.ITopicDTO;
import at.fhv.td.rss.FeedMessageEx;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
}
