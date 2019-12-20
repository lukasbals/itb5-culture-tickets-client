package at.fhv.td.jms;

import at.fhv.td.Main;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;

abstract class JMSClient {
    protected TopicConnectionFactory _conFac;
    protected TopicConnection _con;
    protected TopicSession _session;
    protected String _clientId;

    JMSClient(String clientId, int acknowledgeType) {
        try {
            _conFac = new ActiveMQConnectionFactory("tcp://" + Main.IP_ADDRESS + ":61617");
            _con = _conFac.createTopicConnection();
            _clientId = "culture-tickets-" + clientId;
            _con.setClientID(_clientId);
            _session = _con.createTopicSession(false, acknowledgeType);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            if (_session != null) {
                _session.close();
            }
            if (_con != null) {
                _con.close();
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
