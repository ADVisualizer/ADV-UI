package ch.hsr.adv.ui.queue.logic;

import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.queue.logic.domain.QueueElement;
import ch.hsr.adv.commons.stack.logic.domain.StackElement;
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

import static org.junit.Assert.*;
@RunWith(JukitoRunner.class)
public class QueueParserTest {

    private JsonElement jsonElement;

    @Inject
    private QueueParser sut;

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
        assertEquals("queue", actual.getModuleName());
    }

    @Test
    public void parseADVElementToStackElementTest() throws ADVParseException {
        // WHEN
        ModuleGroup actual = sut.parse(jsonElement);
        ADVElement element = actual.getElements().get(0);

        // THEN
        assertEquals(QueueElement.class, element.getClass());
        QueueElement queueElement = (QueueElement) element;
        assertEquals("1", queueElement.getContent());
    }

}