package ch.adv.ui.array;

import ch.adv.ui.array.logic.ArrayParser;
import ch.adv.ui.array.logic.ArrayStringifyer;
import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.logic.domain.Session;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class ArrayStringifyerTest {

    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ArrayParser testParser;
    @Inject
    private ArrayStringifyer stringifyerUnderTest;

    private Session testSession;

    @Before
    public void setUp() throws IOException {
        URL url = ArrayStringifyerTest.class.getClassLoader()
                .getResource("session1.json");

        String testJson = reader.read(new File(url.getPath()));
        testSession = testParser.parse(testJson);
    }

    @Test
    public void stringifyTest() {
        String actual = stringifyerUnderTest.stringify(testSession);
        Session actualSession = testParser.parse(actual);
        assertEquals(testSession, actualSession);

        String[] lines = actual.split(System.getProperty("line.separator"));
        assertEquals("  \"snapshots\": [", lines[1]);
    }

}