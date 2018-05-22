package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.graph.logic.domain.GraphElement;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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

    private JsonElement jsonElement;

    @Inject
    private GraphParser sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException {
        URL url = GraphParserTest.class.getClassLoader()
                .getResource("module-group.json");

        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        jsonElement = gson.fromJson(json, JsonElement.class);
    }

    @Test
    public void parseSessionDetailsTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);

        // THEN
        assertEquals("graph", actual.getModuleName());
    }

    @Test
    public void parseADVElementToArrayElementTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);

        // THEN
        assertEquals(5, actual.getElements().size());
        ADVElement element = actual.getElements().get(0);
        assertEquals(GraphElement.class, element.getClass());
        GraphElement graphElement = (GraphElement) element;
        assertEquals("A", graphElement.getContent());
    }

    @Test
    public void parsePositionTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);
        List<ADVElement> elements = actual.getElements();
        int posX = elements.get(0).getFixedPosX();
        int posY = elements.get(0).getFixedPosY();
        assertEquals(60, posX);
        assertEquals(50, posY);
    }
}