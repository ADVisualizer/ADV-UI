package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.commons.array.logic.domain.ArrayElement;
import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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
        ADVElement<?> element1 = actual.getElements().get(0);
        ADVElement<?> element2 = actual.getElements().get(1);

        // THEN
        assertEquals(ArrayElement.class, element1.getClass());
        ArrayElement arrayElement1 = (ArrayElement) element1;
        assertNull(arrayElement1.getContent());
        ArrayElement arrayElement2 = (ArrayElement) element2;
        assertEquals("2", arrayElement2.getContent());
    }
}