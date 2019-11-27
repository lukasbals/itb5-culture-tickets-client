package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author Lukas Bals
 */
public interface IMessageFeed extends Remote {
    List<ITopicDTO> getTopics(String username) throws RemoteException;
}
