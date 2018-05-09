package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
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

import static org.junit.Assert.assertNotNull;

@RunWith(JukitoRunner.class)
public class GraphStringifyerTest {

    private ModuleGroup moduleGroup;

    @Inject
    private GraphStringifyer sut;

    @Before
    public void setup(FileDatastoreAccess reader, GraphParser testParser)
            throws ADVParseException, IOException {

        URL url = getClass().getClassLoader().getResource("module-group.json");

        String json = reader.read(new File(url.getPath()));
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(json, JsonElement.class);
        moduleGroup = testParser.parse(element);
    }

    @Test
    public void stringifyTest() throws ADVParseException {
        // WHEN
        JsonElement actual = sut.stringify(moduleGroup);

        // THEN
        assertNotNull(actual);
    }

}