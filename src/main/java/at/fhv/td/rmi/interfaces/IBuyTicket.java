package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IBuyTicket extends Remote {
    boolean buyTicket(ITicketDTO ticketDto, Map<Long, Integer[]> seatPlaceReservations) throws RemoteException;

    boolean reserveTicket(ITicketDTO ticketDto, Map<Long, Integer[]> seatPlaceReservations) throws RemoteException;

    ITicketDTO[] getTickets() throws RemoteException;

    String getMessage() throws RemoteException;
}
