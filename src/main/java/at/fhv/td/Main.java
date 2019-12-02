package at.fhv.td;

import at.fhv.td.gui.UI;
import at.fhv.td.rmi.interfaces.IClientSession;
import at.fhv.td.rmi.interfaces.IClientSessionFactory;

/**
 * @author Lukas Bals
 */
public class Main {
    public static String IP_ADDRESS = "10.0.51.93";
    private static IClientSessionFactory _sessionFactory = null;
    private static IClientSession _session = null;
    private static String _userName;

    public static void main(String[] args) {
        if (args.length > 0) {
            IP_ADDRESS = args[0];
        }
        _sessionFactory = RMI.setupRMI(IP_ADDRESS);

        UI.startUI();

    }

    public static IClientSessionFactory getSessionFactory() {
        return _sessionFactory;
    }

    public static IClientSession getSession() {
        return _session;
    }

    public static void setSession(IClientSession session) {
        _session = session;
    }

    public static String getUserName() {
        return _userName;
    }

    public static void setUserName(String userName) {
        _userName = userName;
    }
}
