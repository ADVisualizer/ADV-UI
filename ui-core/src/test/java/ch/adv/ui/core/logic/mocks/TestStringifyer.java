package ch.adv.ui.core.logic.mocks;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.logic.SessionStoreTest;
import ch.adv.ui.core.logic.Stringifyer;
import ch.adv.ui.core.logic.domain.Session;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.junit.Before;

import java.io.File;
import java.net.URL;

@Singleton
public class TestStringifyer implements Stringifyer {
    private String testJSON;
    @Inject
    private FileDatastoreAccess reader;

    @Before
    public void setUp() throws Exception {
        URL url1 = SessionStoreTest.class.getClassLoader()
                .getResource("session1.json");

        testJSON = reader.read(new File(url1.getPath()));
    }

    @Override
    public String stringify(Session session) {
        return testJSON;
    }

    public String getTestJSON() {
        return testJSON;
    }
}
