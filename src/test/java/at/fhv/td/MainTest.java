package at.fhv.td;

import at.fhv.td.communication.IClientSessionFactory;
import at.fhv.td.communication.RMI;
import at.fhv.td.communication.rmi.IClientSessionFactoryRMI;
import at.fhv.td.gui.UI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RMI.class, UI.class})
public class MainTest {
    @Mock
    private IClientSessionFactoryRMI _sessionFactory;

    @Test
    public void getSessionFactory() {
        mockStatic(RMI.class);
        mockStatic(UI.class);
        when(RMI.setupRMI(any(String.class))).thenReturn(_sessionFactory);

        Main.main(new String[]{});
        assertEquals(Main.getSessionFactory(), _sessionFactory);
    }
}