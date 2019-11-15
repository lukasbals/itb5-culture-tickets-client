package at.fhv.td;

import at.fhv.td.rmi.interfaces.IClientSessionFactory;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @author Lukas Bals
 */
class RMI {
    static IClientSessionFactory setupRMI() {
        try {
            return (IClientSessionFactory) Naming.lookup("rmi://10.0.51.93/clientFactory");
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
