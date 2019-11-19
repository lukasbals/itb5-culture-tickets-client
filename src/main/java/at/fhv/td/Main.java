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
        _sessionFactory = RMI.setupRMI();
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
