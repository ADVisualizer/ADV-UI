package ch.adv.ui.core.logic.mocks;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.logic.Parser;
import ch.adv.ui.core.logic.domain.Session;
import ch.adv.ui.core.logic.util.ADVParseException;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Singleton
public class TestParser implements Parser {
    private Session testSession;
    private String testJSON;

    @Inject
    public TestParser(FileDatastoreAccess reader, Gson gson) throws
            IOException {

        URL url1 = getClass().getClassLoader()
                .getResource("session1.json");

        testJSON = reader.read(new File(url1.getPath()));
        testSession = gson.fromJson(testJSON, Session.class);
    }

    @Override
    public Session parse(String json) throws ADVParseException {
        return testSession;
    }

    public Session getTestSession() {
        return testSession;
    }
}
