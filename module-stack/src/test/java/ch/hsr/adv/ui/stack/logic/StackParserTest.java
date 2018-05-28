package ch.hsr.adv.ui.stack.logic;

import ch.hsr.adv.commons.array.logic.domain.ArrayElement;
import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.stack.logic.domain.StackElement;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import ch.hsr.adv.ui.stack.logic.StackParser;
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

@RunWith(JukitoRunner.class)
public class StackParserTest {

    private JsonElement jsonElement;

    @Inject
    private StackParser sut;

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
        assertEquals("stack", actual.getModuleName());
    }

    @Test
    public void parseADVElementToStackElementTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);
        ADVElement element1 = actual.getElements().get(0);
        ADVElement element2 = actual.getElements().get(1);

        // THEN
        assertEquals(StackElement.class, element1.getClass());
        StackElement stackElement1 = (StackElement) element1;
        assertEquals(null, stackElement1.getContent());
        StackElement stackElement2 = (StackElement) element2;
        assertEquals("2", stackElement2.getContent());
    }
}