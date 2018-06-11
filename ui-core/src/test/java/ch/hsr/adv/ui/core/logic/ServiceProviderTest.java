package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.mocks.GuiceTestModule;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;

@RunWith(JukitoRunner.class)
@UseModules( {GuiceTestModule.class})
public class ServiceProviderTest {

    @Inject
    private ServiceProvider sut;

    @Test
    public void getLayouterTest() {
        // WHEN
        Layouter layouter = sut.getLayouter("test");

        // THEN
        assertNotNull(layouter);
    }

    @Test
    public void getParserTest() {
        // WHEN
        Parser parser = sut.getParser("test");

        // THEN
        assertNotNull(parser);
    }

    @Test
    public void getStringifyerTest() {
        // WHEN
        Stringifyer stringifyer = sut.getStringifyer("test");

        // THEN
        assertNotNull(stringifyer);
    }

}
