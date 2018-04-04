package ch.adv.ui.logic;

import ch.adv.ui.access.FileDatastoreAccess;
import ch.adv.ui.domain.Session;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

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
        storeUnderTest.addSession(session1);
        assertEquals(1, storeUnderTest.getSessions().size());
        assertEquals(session1, storeUnderTest.getCurrentSession());
        assertEquals(session1, storeUnderTest.getSessions().get(0));
    }

    @Test
    public void addMultipleSessionsTest() {
        storeUnderTest.addSession(session1);
        storeUnderTest.addSession(session2_1);
        assertEquals(2, storeUnderTest.getSessions().size());
        assertEquals(session2_1, storeUnderTest.getCurrentSession());
    }

    @Test
    public void mergeSessionsTest() {
        storeUnderTest.addSession(session2_1);
        storeUnderTest.addSession(session2_2);
        assertEquals(1, storeUnderTest.getSessions().size());
        assertEquals(session2_1, storeUnderTest.getCurrentSession());
        assertEquals(2, storeUnderTest.getCurrentSession().getSnapshots()
                .size());
    }

    @Test
    public void addDuplicateSessionTest() {
        storeUnderTest.addSession(session1);
        storeUnderTest.addSession(session1);
        assertEquals(1, storeUnderTest.getSessions().size());
        assertEquals(session1, storeUnderTest.getCurrentSession());
        assertEquals(session1.getSnapshots(), storeUnderTest.getCurrentSession()
                .getSnapshots());
    }

    @Test
    public void deleteSessionTest() {
        storeUnderTest.addSession(session1);
        assertEquals(1, storeUnderTest.getSessions().size());
        assertEquals(session1, storeUnderTest.getCurrentSession());
        storeUnderTest.deleteSession(session1);
        assertEquals(0, storeUnderTest.getSessions().size());
        assertEquals(null, storeUnderTest.getCurrentSession());
    }

    @Test
    public void receiveChangeEventOnMySessionTest() {
        storeUnderTest.addPropertyChangeListener(listener);
        storeUnderTest.addSession(session1);

        Mockito.verify(listener).propertyChange(any());
    }
}