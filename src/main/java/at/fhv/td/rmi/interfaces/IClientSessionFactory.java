package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientSessionFactory extends Remote {
    public IClientSession login(String userName, String password, boolean encrypted) throws RemoteException;
}
