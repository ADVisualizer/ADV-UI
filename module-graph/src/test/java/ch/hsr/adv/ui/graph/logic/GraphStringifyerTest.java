package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
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
public class GraphStringifyerTest {

    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private GraphParser testParser;
    @Inject
    private GraphStringifyer stringifyerUnderTest;

    private Session testSession;

    @Before
    public void setUp() throws IOException, ADVParseException {
        URL url = GraphStringifyerTest.class.getClassLoader()
                .getResource("session.json");

        String testJson = reader.read(new File(url.getPath()));
        testSession = testParser.parse(testJson);
    }

    @Test
    public void stringifyTest() throws ADVParseException {
        String actual = stringifyerUnderTest.stringify(testSession);
        Session actualSession = testParser.parse(actual);
        assertEquals(testSession, actualSession);
    }

}