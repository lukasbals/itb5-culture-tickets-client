package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ILoadTicket extends Remote {
    ITicketDTO[] getUnavailableTickets(long eventId) throws RemoteException;
}
