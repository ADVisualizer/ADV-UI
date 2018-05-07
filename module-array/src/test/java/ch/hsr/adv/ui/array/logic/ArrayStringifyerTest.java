package ch.hsr.adv.ui.array.logic;

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
public class ArrayStringifyerTest {

    private ModuleGroup moduleGroup;

    @Inject
    private ArrayStringifyer sut;

    @Before
    public void setup(FileDatastoreAccess reader, ArrayParser testParser)
            throws ADVParseException, IOException {

        URL url = getClass().getClassLoader().getResource("session.json");

        String json = reader.read(new File(url.getPath()));
        Gson gson = new Gson();
        JsonElement element = gson.fromJson (json, JsonElement.class);
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