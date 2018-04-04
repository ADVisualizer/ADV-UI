package ch.adv.ui.array;

import ch.adv.ui.core.access.FileDatastoreAccess;
import ch.adv.ui.core.domain.Session;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import com.google.inject.Inject;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import util.JavafxViewTest;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class ArrayLayouterTest extends JavafxViewTest {
    @Inject
    private FileDatastoreAccess reader;
    @Inject
    private ArrayParser testParser;
    @Inject
    private ArrayLayouter layouterUnderTest;

    private Session testSession;

    @Before
    public void setUp() {
        URL url = ArrayLayouterTest.class.getClassLoader()
                .getResource("session1.json");

        String testJson = reader.read(new File(url.getPath()));
        testSession = testParser.parse(testJson);
    }

    @Test
    public void layoutTest() {
        LayoutedSnapshot actual = layouterUnderTest
                .layout(testSession.getFirstSnapshot());
        assertEquals(1, actual.getSnapshotId());
        assertEquals("1", actual.getSnapshotDescription());
        ObservableList<Node> children = actual.getPane().getChildren();
        assertEquals(1, children.size());
        HBox hbox = (HBox) children.get(0);
        ObservableList<Node> arrayElements = hbox.getChildren();
        assertEquals(2,arrayElements.size());
    }
}