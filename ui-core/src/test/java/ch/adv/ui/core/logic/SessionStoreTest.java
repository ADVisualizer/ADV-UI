package ch.adv.ui.core.logic;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.logic.domain.Session;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;

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
    private PropertyChangeListener listener;

    private SessionStore storeUnderTest;

    @Before
    public void setUp(SessionStore storeUnderTest) throws Exception {
        this.storeUnderTest = storeUnderTest;

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
        storeUnderTest.add(session1);
        assertEquals(1, storeUnderTest.getAll().size());
        assertEquals(session1, storeUnderTest.getCurrent());
        assertEquals(session1, storeUnderTest.getAll().get(0));
    }

    @Test
    public void addMultipleSessionsTest() {
        storeUnderTest.add(session1);
        storeUnderTest.add(session2_1);
        assertEquals(2, storeUnderTest.getAll().size());
        assertEquals(session2_1, storeUnderTest.getCurrent());
    }

    @Test
    public void mergeSessionsTest() {
        storeUnderTest.add(session2_1);
        storeUnderTest.add(session2_2);
        assertEquals(1, storeUnderTest.getAll().size());
        assertEquals(session2_1, storeUnderTest.getCurrent());
        assertEquals(2, storeUnderTest.getCurrent().getSnapshots()
                .size());
    }

    @Test
    public void addDuplicateSessionTest() {
        storeUnderTest.add(session1);
        storeUnderTest.add(session1);
        assertEquals(1, storeUnderTest.getAll().size());
        assertEquals(session1, storeUnderTest.getCurrent());
        assertEquals(session1.getSnapshots(), storeUnderTest.getCurrent()
                .getSnapshots());
    }

    @Test
    public void deleteSessionTest() {
        storeUnderTest.add(session1);
        assertEquals(1, storeUnderTest.getAll().size());
        assertEquals(session1, storeUnderTest.getCurrent());
        storeUnderTest.delete(session1.getSessionId());
        assertEquals(0, storeUnderTest.getAll().size());
        assertEquals(null, storeUnderTest.getCurrent());
    }

}