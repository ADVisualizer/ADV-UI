package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.logic.domain.Session;
import ch.hsr.adv.ui.core.logic.SessionStoreTest;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.net.URL;

@Singleton
public class TestStringifyer implements Stringifyer {
    private String testJSON;

    @Inject
    public TestStringifyer(FileDatastoreAccess reader) throws Exception {
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
