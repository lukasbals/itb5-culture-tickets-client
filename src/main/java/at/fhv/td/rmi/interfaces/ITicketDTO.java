package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ITicketDTO extends Remote {

    Long getEventId() throws RemoteException;

    void setEventId(Long eventId) throws RemoteException;

    int getTicketNumber() throws RemoteException;

    void setTicketNumber(int ticketNumber) throws RemoteException;

    String getCategoryName() throws RemoteException;

    void setCategoryName(String categoryName) throws RemoteException;

    Long getCategoryId() throws RemoteException;

    void setCategoryId(Long categoryId) throws RemoteException;

    Long getClientId() throws RemoteException;

    void setClientId(Long client) throws RemoteException;

    Float getPrice() throws RemoteException;

    void setPrice(Float price) throws RemoteException;

    Long getId() throws RemoteException;

    void setId(Long id) throws RemoteException;

    int getSold() throws RemoteException;

    void setSold(int sold) throws RemoteException;
}