package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import com.google.inject.Inject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JukitoRunner.class)
public class ModuleGroupSerializerTest {

    private static final ServiceProvider serviceProviderMock = Mockito
            .mock(ServiceProvider.class);
    private static final DefaultStringifyer defaultStringifyerMock = Mockito
            .mock(DefaultStringifyer.class);

    @Inject
    private ModuleGroupSerializer sut;


    @Test
    public void defaultStringifyerTest(ModuleGroup moduleGroupMock) {
        // GIVEN
        doReturn(defaultStringifyerMock).when(serviceProviderMock).getStringifyer(isNull());

        // WHEN
        sut.serialize(moduleGroupMock, null, null);

        // THEN
        verify(defaultStringifyerMock).stringify(any());
    }

    @Test
    public void moduleStringifyerTest(ModuleGroup moduleGroupMock,
                                      Stringifyer stringifyerMock) {
        // GIVEN
        when(serviceProviderMock.getStringifyer(any()))
                .thenReturn(stringifyerMock);

        // WHEN
        sut.serialize(moduleGroupMock, null, null);

        // THEN
        verify(stringifyerMock).stringify(any());
    }

    public static class Module extends JukitoModule {
        protected void configureTest() {
            bind(DefaultStringifyer.class).toInstance(defaultStringifyerMock);
            bind(ServiceProvider.class).toInstance(serviceProviderMock);
        }
    }

}
