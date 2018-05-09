package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.ui.array.logic.domain.ArrayElement;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
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
public class ArrayParserTest {

    private JsonElement jsonElement;

    @Inject
    private ArrayParser sut;

    @Before
    public void setup(FileDatastoreAccess reader) throws IOException {
        URL url = getClass().getClassLoader().getResource("module-group.json");
        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        jsonElement = gson.fromJson(json, JsonElement.class);
    }

    @Test
    public void parseSessionDetailsTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);

        // THEN
        assertEquals("array", actual.getModuleName());
    }

    @Test
    public void parseADVElementToArrayElementTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);
        ADVElement element = actual.getElements().get(0);

        // THEN
        assertEquals(ArrayElement.class, element.getClass());
        ArrayElement arrayElement = (ArrayElement) element;
        assertEquals("1", arrayElement.getContent());
    }

    @Test
    public void parsePositionTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);
        List<ADVElement> elements1 = actual.getElements();
        int posX = elements1.get(0).getFixedPosX();
        int posY = elements1.get(0).getFixedPosY();

        // THEN
        assertEquals(10, posX);
        assertEquals(0, posY);
    }
}