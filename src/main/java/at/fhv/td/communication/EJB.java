package at.fhv.td.communication;

import at.fhv.td.communication.IClientSessionFactory;
import at.fhv.td.communication.ejb.IClientSessionFactoryRemote;

import javax.naming.Context;
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
            props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.setProperty(Context.URL_PKG_PREFIXES, "com.sun.enterprise.naming");
            props.setProperty(Context.STATE_FACTORIES, "com.sun.corba.ee.impl.presentation.rmi.JNDIStateFactoryImpl");
            props.setProperty(Context.PROVIDER_URL, host);

            InitialContext ctx = new InitialContext(props);
            session = (IClientSessionFactoryRemote) ctx.lookup("at.fhv.td.communication.ejb.IClientSessionFactoryRemote");
//            session = (IClientSessionFactory) ctx.lookup("java:global/itb5-culture-tickets-1.0-SNAPSHOT/ClientSessionFactoryBean!at.fhv.td.communication.interfaces.IClientSessionFactory");
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return session;
    }
}