package ch.hsr.adv.ui.array.presentation;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.array.logic.ArrayParser;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import org.jukito.JukitoModule;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.testfx.framework.junit.ApplicationTest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ArrayLayouterTest extends ApplicationTest {
    private static final String SHOW_OBJECT_RELATIONS = "SHOW_OBJECT_RELATIONS";
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ArrayParser testParser;
    @Inject
    private ArrayLayouter sut;
    private ModuleGroup moduleGroup;

    @Before
    public void setUp() throws IOException {
        URL url = getClass().getClassLoader().getResource("module-group.json");
        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        moduleGroup = testParser.parse(gson.fromJson(json, JsonElement.class));
    }

    @Test
    public void layoutDefaultTest(ArrayDefaultLayouter mockDefaultLayouter) {
        // WHEN
        sut.layout(moduleGroup, null);

        // THEN
        verify(mockDefaultLayouter).layout(moduleGroup);
    }

    @Test
    public void layoutObjectReferenceTest(ArrayObjectReferenceLayouter
                                                  mockObjectReferenceLayouter) {
        // WHEN
        List<String> flags = new ArrayList<>();
        flags.add(SHOW_OBJECT_RELATIONS);
        sut.layout(moduleGroup, flags);

        // THEN
        verify(mockObjectReferenceLayouter).layout(moduleGroup);
    }

    public static class Module extends JukitoModule {
        @Override
        protected void configureTest() {
            forceMock(ArrayDefaultLayouter.class);
            forceMock(ArrayObjectReferenceLayouter.class);

        }
    }
}