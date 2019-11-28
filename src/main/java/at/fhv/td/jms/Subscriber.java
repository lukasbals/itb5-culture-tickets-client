package at.fhv.td.jms;

import at.fhv.td.rss.FeedMessage;

import javax.jms.*;
import java.util.LinkedList;
import java.util.List;

public class Subscriber extends JMSClient implements MessageListener {
    private List<TopicSubscriber> _durableSubscribers = new LinkedList<>();

    public Subscriber(List<String> topics, String userId) {
        super("subscriber-" + userId, Session.CLIENT_ACKNOWLEDGE);
        initTopics(topics, userId);
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
        if (message instanceof ObjectMessage) {
            FeedMessage newMessage = null;
            try {
                newMessage = (FeedMessage) (((ObjectMessage) message).getObject());
                // TODO do something with the incoming message
                System.out.println("RECEIVED: " + newMessage.getTitle());
            } catch (JMSException e) {
                e.printStackTrace();
            }
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
