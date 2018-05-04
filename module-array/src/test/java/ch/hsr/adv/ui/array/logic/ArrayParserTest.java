package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.ui.array.logic.domain.ArrayElement;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JukitoRunner.class)
public class ArrayParserTest {
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ArrayParser parserUnderTest;

    private String testJson;

    @Before
    public void setUp() throws IOException {
        URL url = ArrayParserTest.class.getClassLoader()
                .getResource("session1.json");

        testJson = reader.read(new File(url.getPath()));
    }

    @Test
    public void parseSessionDetailsTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        assertEquals("TestSession", actual.getSessionName());
        assertEquals(123456, actual.getSessionId());
        assertEquals("array",actual.getModuleName());
    }

    @Test
    public void parseADVElementToArrayElementTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        ADVElement element = actual.getFirstSnapshot().getElements().get(0);
        assertEquals(ArrayElement.class, element.getClass());
        ArrayElement arrayElement = (ArrayElement) element;
        assertEquals("1", arrayElement.getContent());
    }

    @Test
    public void parseSnapshotDescriptionTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        String description1 = actual.getSnapshots().get(0)
                .getSnapshotDescription();
        assertEquals(description1, "1");
        String description2 = actual.getSnapshots().get(1)
                .getSnapshotDescription();
        assertNull(description2);
    }

    @Test
    public void parsePositionTest() throws ADVParseException {
        Session actual = parserUnderTest.parse(testJson);
        List<ADVElement> elements1 = actual.getSnapshots().get(0)
                .getElements();
        List<ADVElement> elements2 = actual.getSnapshots().get(1)
                .getElements();
        int posX1 = elements1.get(0).getFixedPosX();
        int posX2 = elements2.get(0).getFixedPosX();
        assertEquals(10, posX1);
        assertEquals(0, posX2);
    }
}