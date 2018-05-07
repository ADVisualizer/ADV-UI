package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.mocks.GuiceTestModule;
import ch.hsr.adv.ui.core.logic.util.ADVParseException;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.jukito.UseModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JukitoRunner.class)
@UseModules(GuiceTestModule.class)
public class CoreParserTest {

    @Inject
    CoreParser sut;

    @Test
    public void parseSessionTest(FileDatastoreAccess reader)
            throws IOException, ADVParseException {

        // GIVEN
        URL url = getClass().getClassLoader().getResource("session1.json");
        String json = reader.read(new File(url.getPath()));

        // WHEN
        Session session = sut.parse(json);

        // THEN
        assertNotNull(session);
        assertEquals(2, session.getSnapshots().size());
    }
}
