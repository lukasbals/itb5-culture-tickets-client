package at.fhv.td.communication;

import at.fhv.td.communication.ejb.IClientSessionFactoryRemote;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class EJB {
    private EJB() {
    }

    public static IClientSessionFactoryRemote setupEJB(String host) {
        IClientSessionFactoryRemote session = null;

        try {
            Properties props = new Properties();
            props.setProperty("org.omg.CORBA.ORBInitialHost", host);
            props.setProperty("org.omg.CORBA.ORBInitialPort", "3700");

            InitialContext ctx = new InitialContext(props);
            session = (IClientSessionFactoryRemote) ctx.lookup("at.fhv.td.communication.ejb.IClientSessionFactoryRemote");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return session;
    }
}