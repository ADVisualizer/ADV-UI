package ch.adv.ui.logic;

import ch.adv.ui.access.FileDatastoreAccess;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JukitoRunner.class)
public class ModuleStoreTest {
    @Inject
    private ADVModule testModule;
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ModuleStore storeUnderTest;

    private String testJson;

    @Before
    public void setUp() {
        Map<String, ADVModule> modules = new HashMap<>();
        modules.put("test", testModule);
        storeUnderTest.setAvailableModules(modules);

        URL url = SessionStoreTest.class.getClassLoader()
                .getResource("session1.json");

        testJson = reader.read(new File(url.getPath()));
    }

    @Test
    public void parseKnownModuleTest() {
        ADVModule actual = storeUnderTest.parseModule(testJson);
        assertEquals(testModule, actual);
    }

    @Test
    public void parseUnknownModuleTest() {
        String testJSON = "{\"moduleName\": \"testModule\"}";
        ADVModule actual = storeUnderTest.parseModule(testJSON);
        assertNull(actual);
    }
}