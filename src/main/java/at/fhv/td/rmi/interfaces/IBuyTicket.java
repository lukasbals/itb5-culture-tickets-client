package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface IBuyTicket extends Remote {
    /**
     * @param ticketDto
     * @param seatPlaceReservations first integer is placeCategoryId, integer array are selected seats for purchase (ticket number are seat numbers)
     * @return all generated tickets and success message, or an empty ticket list and failure message
     * @throws RemoteException
     */
    boolean buyTicket(ITicketDTO ticketDto, Map<Long, Integer[]> seatPlaceReservations) throws RemoteException;

    ITicketDTO[] getTickets() throws RemoteException;

    String getMessage() throws RemoteException;
}
