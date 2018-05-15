package ch.hsr.adv.ui.core.service;

import ch.hsr.adv.ui.core.logic.ADVFlowControl;
import ch.hsr.adv.ui.core.logic.FlowControl;
import com.google.inject.Inject;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.apache.commons.io.IOUtils;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ADVConnectionTest {

    private ADVConnection sut;

    @Inject
    private ADVConnectionFactory connectionFactory;
    private Socket socketMock;
    private OutputStream outputStreamMock;

    @Before
    public void setup() throws IOException {
        outputStreamMock = Mockito.mock(OutputStream.class);
        socketMock = Mockito.mock(Socket.class);
        Mockito.when(socketMock.getOutputStream()).thenReturn(outputStreamMock);

        sut = connectionFactory.create(socketMock);
    }

    @Test
    public void processConnectionTest() throws IOException {
        // GIVEN
        InputStream inputStreamMock = IOUtils
                .toInputStream("{command:'TRANSMIT', json:''}",
                        StandardCharsets.UTF_8);
        Mockito.when(socketMock.getInputStream()).thenReturn(inputStreamMock);

        // WHEN
        sut.process();

        // THEN
        verify(outputStreamMock).write(any(), anyInt(), anyInt());
    }

    @Test
    public void processConnectionFailTest() throws IOException {
        // GIVEN
        InputStream inputStreamMock = IOUtils.toInputStream("{,malformed}",
                StandardCharsets.UTF_8);
        Mockito.when(socketMock.getInputStream()).thenReturn(inputStreamMock);

        // WHEN
        sut.process();

        // THEN
        verify(outputStreamMock).write(any(), anyInt(), anyInt());
    }

    public static class Module extends JukitoModule {
        protected void configureTest() {
            install(new FactoryModuleBuilder()
                    .build(ADVConnectionFactory.class));
            bind(FlowControl.class)
                    .toInstance(Mockito.mock(ADVFlowControl.class));
        }
    }
}
