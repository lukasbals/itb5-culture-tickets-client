package at.fhv.td;

import at.fhv.td.gui.UI;
import at.fhv.td.rmi.interfaces.IClientSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RMI.class, UI.class})
public class MainTest {
    @Mock
    private IClientSessionFactory _sessionFactory;

    @Test
    public void getSessionFactory() {
        mockStatic(RMI.class);
        mockStatic(UI.class);
        when(RMI.setupRMI()).thenReturn(_sessionFactory);

        Main.main(new String[]{});
        assertEquals(Main.getSessionFactory(), _sessionFactory);
    }
}