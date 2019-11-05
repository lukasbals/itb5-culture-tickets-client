package at.fhv.td;

import at.fhv.td.gui.UI;
import at.fhv.td.rmi.interfaces.IClientSessionFactory;

/**
 * @author Lukas Bals
 */
public class Main {
    private static IClientSessionFactory _sessionFactory = null;

    public static void main(String[] args) {
        _sessionFactory = RMI.setupRMI();
        UI.startUI();
    }

    public static IClientSessionFactory getSessionFactory() {
        return _sessionFactory;
    }
}
