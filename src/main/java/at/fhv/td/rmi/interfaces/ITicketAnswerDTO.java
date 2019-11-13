package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITicketAnswerDTO extends Remote {
    String getMessage() throws RemoteException;

    void setMessage(String message) throws RemoteException;

    ITicketDTO[] getTickets() throws RemoteException;

    void setTickets(ITicketDTO[] tickets) throws RemoteException;
}
