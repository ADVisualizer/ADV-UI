package ch.hsr.adv.ui.core.logic.stores;

import ch.hsr.adv.commons.core.logic.domain.Session;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JukitoRunner.class)
public class SessionStoreTest {

    private Session session1;
    // both session have the same session id
    private Session session2_1;
    private Session session2_2;

    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private Gson gson;

    @Inject
    private SessionStore sut;

    @Before
    public void setUp(SessionStore storeUnderTest) throws Exception {
        this.sut = storeUnderTest;

        URL url1 = SessionStoreTest.class.getClassLoader()
                .getResource("session1.json");
        URL url2_1 = SessionStoreTest.class.getClassLoader().getResource(""
                + "session2snapshot1.json");
        URL url2_2 = SessionStoreTest.class.getClassLoader().getResource(""
                + "session2snapshot2.json");

        session1 = gson
                .fromJson(reader.read(new File(url1.getPath())), Session.class);
        session2_1 = gson.fromJson(reader.read(new File(url2_1.getPath())),
                Session.class);
        session2_2 = gson.fromJson(reader.read(new File(url2_2.getPath())),
                Session.class);
    }

    @Test
    public void addSessionTest() {
        // WHEN
        sut.add(session1);

        // THEN
        assertEquals(1, sut.getAll().size());
        assertEquals(session1, sut.getCurrentSession());
        assertEquals(session1, sut.getAll().get(0));
    }

    @Test
    public void addMultipleSessionsTest() {
        // WHEN
        sut.add(session1);
        sut.add(session2_1);

        // THEN
        assertEquals(2, sut.getAll().size());
        assertEquals(session2_1, sut.getCurrentSession());
    }

    @Test
    public void mergeSessionsTest() {
        // WHEN
        sut.add(session2_1);
        sut.add(session2_2);

        // THEN
        assertEquals(1, sut.getAll().size());
        assertEquals(session2_1, sut.getCurrentSession());
        assertEquals(2, sut.getCurrentSession().getSnapshots().size());
    }

    @Test
    public void addDuplicateSessionTest() {
        // WHEN
        sut.add(session1);
        sut.add(session1);

        // THEN
        assertEquals(1, sut.getAll().size());
        assertEquals(session1, sut.getCurrentSession());
        assertEquals(session1.getSnapshots(), sut.getCurrentSession().getSnapshots());
    }

    @Test
    public void deleteSessionTest() {
        // GIVEN
        sut.add(session1);
        assertEquals(1, sut.getAll().size());
        assertEquals(session1, sut.getCurrentSession());

        // WHEN
        sut.delete(session1.getSessionId());

        // THEN
        assertEquals(0, sut.getAll().size());
        assertNull(sut.getCurrentSession());
    }

}