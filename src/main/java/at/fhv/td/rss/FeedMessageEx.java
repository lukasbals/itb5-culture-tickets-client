package at.fhv.td.rss;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

public class FeedMessageEx extends FeedMessage {
    private String _topic;
    private boolean _read = false;
    private ObjectMessage _message;

    public FeedMessageEx(ObjectMessage msg) {
        _message = msg;

        FeedMessage message = null;
        try {
            message = (FeedMessage) msg.getObject();
            setDescription(message.getDescription());
            setTitle(message.getTitle());
            setLink(message.getLink());
            setPublishDate(message.getPublishDate());
            setTopic(msg.getJMSDestination().toString().replace("topic://", ""));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public ObjectMessage getMessageObject() {
        return _message;
    }

    public String getTopic() {
        return _topic;
    }

    public void setTopic(String topic) {
        _topic = topic;
    }

    public boolean isRead() {
        return _read;
    }

    public void setRead(boolean read) {
        _read = read;
    }
}
