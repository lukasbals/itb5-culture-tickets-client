package at.fhv.td.jms;

import at.fhv.td.rss.FeedMessage;

import javax.jms.*;

public class Publisher extends JMSClient {

    public Publisher(String clientId) {
        super("publisher-" + clientId, Session.AUTO_ACKNOWLEDGE);
    }

    public void publish(String topic, FeedMessage message) {
        try {
            Topic destinationTopic = _session.createTopic(topic);
            ObjectMessage msg = _session.createObjectMessage(message);
            TopicPublisher publisher = _session.createPublisher(destinationTopic);
            publisher.send(msg, javax.jms.DeliveryMode.PERSISTENT, javax.jms.Message.DEFAULT_PRIORITY, Message.DEFAULT_TIME_TO_LIVE);
            publisher.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
