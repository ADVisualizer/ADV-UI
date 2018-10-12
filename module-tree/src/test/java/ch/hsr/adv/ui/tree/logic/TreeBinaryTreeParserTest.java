package ch.hsr.adv.ui.tree.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class TreeBinaryTreeParserTest {

    private static final double DOUBLE_ACCURACY = 0.00001;

    private JsonElement jsonElement;

    @Inject
    private TreeBinaryTreeParser sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException {
        URL url = TreeBinaryTreeParserTest.class.getClassLoader()
                .getResource("module-group.json");

        if (url == null) {
            throw new FileNotFoundException();
        }

        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        jsonElement = gson.fromJson(json, JsonElement.class);
    }

    @Test
    public void parseModuleNameTest() throws ADVParseException {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals("tree-binary", actual.getModuleName());
    }

    @Test
    public void parseElementsTest() throws ADVParseException {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals(6, actual.getElements().size());
    }

    @Test
    public void parseRelationsTest() throws ADVParseException {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals(5, actual.getRelations().size());
    }

    @Test
    public void parseElementTypeTest() throws ADVParseException {
        ModuleGroup actual = sut.parse(jsonElement);

        assertTrue(actual.getElements().get(2) instanceof TreeNodeElement);
    }

    @Test
    public void parseRelationTypeTest() throws ADVParseException {
        ModuleGroup actual = sut.parse(jsonElement);

        assertTrue(actual.getRelations().get(1) instanceof TreeNodeRelation);
    }

    @Test
    public void parseADVStyleTest() throws ADVParseException {
        ModuleGroup actual = sut.parse(jsonElement);

        ADVStyle parsedStyle = actual.getElements().get(3).getStyle();
        assertEquals(13888729, parsedStyle.getFillColor());
        assertEquals(7324547, parsedStyle.getStrokeColor());
        assertEquals("solid", parsedStyle.getStrokeStyle());
        assertEquals(2, parsedStyle.getStrokeThickness(), DOUBLE_ACCURACY);
    }
}
