package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ISearchEvent extends Remote {
    public List<IEventDetailedViewDTO> searchForEvents(String eventName, String artistName, String location, LocalDate eventDate) throws RemoteException;
}
