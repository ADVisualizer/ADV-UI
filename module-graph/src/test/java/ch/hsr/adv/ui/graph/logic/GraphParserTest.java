package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
import ch.hsr.adv.ui.graph.logic.domain.GraphElement;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class GraphParserTest {
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private GraphParser parserUnderTest;

    private String testJson;

    @Before
    public void setUp() throws IOException {
        URL url = GraphParserTest.class.getClassLoader()
                .getResource("session.json");

        testJson = reader.read(new File(url.getPath()));
    }

    @Test
    public void parseSessionDetailsTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        assertEquals("TestSession", actual.getSessionName());
        assertEquals(123456, actual.getSessionId());
        assertEquals("graph", actual.getModuleName());
    }

    @Test
    public void parseADVElementToArrayElementTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        assertEquals(5, actual.getFirstSnapshot().getElements().size());
        ADVElement element = actual.getFirstSnapshot().getElements().get(0);
        assertEquals(GraphElement.class, element.getClass());
        GraphElement graphElement = (GraphElement) element;
        assertEquals("A", graphElement.getContent());
    }

    @Test
    public void parseSnapshotDescriptionTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        String description1 = actual.getSnapshots().get(0)
                .getSnapshotDescription();
        assertEquals(description1, "Inital graph");
    }

    @Test
    public void parsePositionTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        List<ADVElement> elements = actual.getFirstSnapshot()
                .getElements();
        int posX = elements.get(0).getFixedPosX();
        int posY = elements.get(0).getFixedPosY();
        assertEquals(60, posX);
        assertEquals(50, posY);
    }
}