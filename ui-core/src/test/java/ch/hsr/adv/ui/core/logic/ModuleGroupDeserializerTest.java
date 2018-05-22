package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import com.google.gson.JsonObject;
import com.google.inject.Inject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class ModuleGroupDeserializerTest {

    private static final ServiceProvider serviceProviderMock = Mockito
            .mock(ServiceProvider.class);

    @Inject
    private ModuleGroupDeserializer sut;


    @Test
    public void moduleStringifyerTest(Parser parserMock)
            throws ADVParseException {

        // GIVEN
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("moduleName", "test");
        when(serviceProviderMock.getParser(any())).thenReturn(parserMock);

        // WHEN
        sut.deserialize(jsonObject, null, null);

        // THEN
        verify(parserMock).parse(any());
    }

    public static class Module extends JukitoModule {
        protected void configureTest() {
            bind(ServiceProvider.class).toInstance(serviceProviderMock);
        }
    }

}
