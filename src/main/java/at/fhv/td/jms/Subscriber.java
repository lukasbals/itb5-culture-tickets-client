package at.fhv.td.jms;

import at.fhv.td.gui.ListEventsGuiController;
import at.fhv.td.rss.FeedMessageEx;

import javax.jms.*;
import java.util.LinkedList;
import java.util.List;

public class Subscriber extends JMSClient implements MessageListener {
    private static Subscriber _instance;
    private List<TopicSubscriber> _durableSubscribers = new LinkedList<>();
    private ListEventsGuiController _controller;

    private Subscriber(List<String> topics, String userId, ListEventsGuiController controller) {
        super("subscriber-" + userId, Session.CLIENT_ACKNOWLEDGE);
        _controller = controller;
        _instance = this;
        initTopics(topics, userId);
    }

    public static Subscriber createInstance(List<String> topics, String userId, ListEventsGuiController controller) {
        _instance = new Subscriber(topics, userId, controller);
        return _instance;
    }

    public static Subscriber getInstance() {
        return _instance;
    }

    private void initTopics(List<String> topics, String userId) {
        try {
            for (String topic : topics) {
                Topic subscribingTopic = _session.createTopic(topic);
                TopicSubscriber subscriber = _session.createDurableSubscriber(subscribingTopic, userId);
                subscriber.setMessageListener(this);
                _durableSubscribers.add(subscriber);
            }
            _con.start();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage(Message message) {
        synchronized (this) {
            if (message instanceof ObjectMessage) {
                FeedMessageEx newMessage = new FeedMessageEx((ObjectMessage) message);
                _controller.getMessageList().add(newMessage);
            }
        }
    }

    public void acknowledgeMessage(Message message) {
        try {
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            for (TopicSubscriber subscriber : _durableSubscribers) {
                subscriber.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            super.close();
        }
    }
}
