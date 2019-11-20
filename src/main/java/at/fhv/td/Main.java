package at.fhv.td;

import at.fhv.td.gui.UI;
import at.fhv.td.rmi.interfaces.IClientSession;
import at.fhv.td.rmi.interfaces.IClientSessionFactory;

/**
 * @author Lukas Bals
 */
public class Main {
    private static IClientSessionFactory _sessionFactory = null;
    private static IClientSession _session = null;

    public static void main(String[] args) {
        String host = "10.0.51.93";
        if (args.length > 0) {
            host = args[0];
        }
        _sessionFactory = RMI.setupRMI(host);
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
}
