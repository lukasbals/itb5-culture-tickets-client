package at.fhv.td;

import at.fhv.td.communication.EJB;
import at.fhv.td.communication.IClientSession;
import at.fhv.td.communication.IClientSessionFactory;
import at.fhv.td.communication.RMI;
import at.fhv.td.gui.UI;

/**
 * @author Lukas Bals
 */
public class Main {
    public static String IP_ADDRESS = "10.0.51.93";
    private static String CONNECTION_TYPE = "rmi";
    private static IClientSessionFactory _sessionFactory = null;
    private static IClientSession _session = null;
    private static String _userName;

    public static void main(String[] args) {
        try {
            if (args.length > 0) {
                IP_ADDRESS = args[0];
                if (args.length > 1) {
                    CONNECTION_TYPE = args[1];
                }
            }

            switch (CONNECTION_TYPE) {
                default:
                case "rmi":
                    _sessionFactory = RMI.setupRMI(IP_ADDRESS);
                    break;
                case "ejb":
                    _sessionFactory = EJB.setupEJB(IP_ADDRESS);
                    break;
            }

            UI.startUI();
        } catch (Exception e) {
            printHelp();
        }
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

    private static void printHelp() {
        System.out.println("Usage:");
        System.out.println(" - options: java -jar itb5-culture-tickets-client-1.0-SNAPSHOT.jar [ip/localhost] [rmi/ejb]");
        System.out.println(" - default: java -jar itb5-culture-tickets-client-1.0-SNAPSHOT.jar 10.0.51.93 rmi");
    }
}