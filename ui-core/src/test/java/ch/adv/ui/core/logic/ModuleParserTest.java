package ch.adv.ui.core.logic;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.presentation.GuiceCoreModule;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(JukitoRunner.class)
@UseModules( {GuiceCoreModule.class, GuiceTestModule.class})
public class ModuleParserTest {

    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ModuleParser parserUnderTest;

    private String testJson;

    @Before
    public void setUp() throws IOException {
        URL url = SessionStoreTest.class.getClassLoader()
                .getResource("session1.json");

        testJson = reader.read(new File(url.getPath()));
    }

    @Test
    public void parseKnownModuleTest() {
        ADVModule actual = parserUnderTest.parseModule(testJson);
        assertNotNull(actual);
    }

    @Test
    public void parseUnknownModuleTest() {
        String testJSON = "{\"moduleName\": \"asdfModule\"}";
        ADVModule actual = parserUnderTest.parseModule(testJSON);
        assertNull(actual);
    }
}