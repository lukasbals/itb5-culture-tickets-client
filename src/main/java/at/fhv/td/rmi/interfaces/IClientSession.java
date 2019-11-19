package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientSession extends Remote {
    public ISearchEvent createSearchEvent() throws RemoteException;

    public IBuyTicket createBuyTicket() throws RemoteException;

    public ILoadClient createClient() throws RemoteException;

    public ITicketDTO createTicketDTO() throws RemoteException;

    public ILoadTicket createLoadTicket() throws RemoteException;
}
