package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientDTO extends Remote {
    String getFirstName()throws RemoteException;

    void setFirstName(String firstName)throws RemoteException;

    String getLastName()throws RemoteException;

    void setLastName(String lastName)throws RemoteException;

    String getAddress()throws RemoteException;

    void setAddress(String address)throws RemoteException;

    Long getId()throws RemoteException;
}
