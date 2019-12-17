package at.fhv.td.communication;

import at.fhv.td.communication.rmi.IClientSessionFactoryRMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * @author Lukas Bals
 */
public class RMI {
    private static int PORT = 25565;

    public static IClientSessionFactoryRMI setupRMI(String host) {
        try {
            Registry reg = LocateRegistry.getRegistry(host, PORT);
            return (IClientSessionFactoryRMI) reg.lookup(reg.list()[0]);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
