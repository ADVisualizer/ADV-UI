package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.SessionStoreTest;
import ch.hsr.adv.ui.core.logic.Stringifyer;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.gson.JsonElement;
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

    public String getTestJSON() {
        return testJSON;
    }

    @Override
    public JsonElement stringify(ModuleGroup moduleGroup) {
        return null;
    }
}
