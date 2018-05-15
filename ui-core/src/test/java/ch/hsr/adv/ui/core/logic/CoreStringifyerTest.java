package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.domain.Snapshot;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JukitoRunner.class)
public class CoreStringifyerTest {

    @Inject
    private CoreStringifyer sut;

    @Test
    public void stringifySessionTest() {
        // GIVEN
        ModuleGroup moduleGroup = new ModuleGroup();
        moduleGroup.setModuleName("test");

        Snapshot snapshot = new Snapshot();
        snapshot.getModuleGroups().add(moduleGroup);

        String sessionName = "A test session";
        Session session = new Session();
        session.setSessionName(sessionName);
        session.getSnapshots().add(snapshot);

        // WHEN
        String json = sut.stringify(session);

        // THEN
        assertNotNull(json);
        assertTrue(json.contains(sessionName));
        assertTrue(json.contains("moduleGroups"));
    }
}
