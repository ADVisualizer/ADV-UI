package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.Session;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(JukitoRunner.class)
public class CoreStringifyerTest {

    @Inject
    CoreStringifyer sut;

    @Test
    public void stringifySessionTest() {

        // GIVEN
        final String sessionName = "A test session";
        Session session = new Session();
        session.setSessionName(sessionName);

        // WHEN
        String json = sut.stringify(session);

        // THEN
        assertNotNull(json);
        assertTrue(json.contains(sessionName));
    }
}
