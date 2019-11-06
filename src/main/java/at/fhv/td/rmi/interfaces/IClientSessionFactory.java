package at.fhv.td.rmi.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientSessionFactory extends Remote {
	public abstract ISearchEvent createConnection() throws RemoteException;

	public abstract ILoadClient createClient() throws RemoteException;
}
