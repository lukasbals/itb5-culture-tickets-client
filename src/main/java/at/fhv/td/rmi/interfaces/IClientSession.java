package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientSession extends Remote {
    ISearchEvent createSearchEvent() throws RemoteException;

    IBuyTicket createBuyTicket() throws RemoteException;

    ILoadClient createClient() throws RemoteException;

    ITicketDTO createTicketDTO() throws RemoteException;

    ILoadTicket createLoadTicket() throws RemoteException;

    IMessageFeed createMessageFeed() throws RemoteException;
}
