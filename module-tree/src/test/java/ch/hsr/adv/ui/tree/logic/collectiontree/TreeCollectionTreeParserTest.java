package ch.hsr.adv.ui.tree.logic.collectiontree;

import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.tree.logic.TreeParserTestBase;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(JukitoRunner.class)
public class TreeCollectionTreeParserTest {

    private TreeParserTestBase base;

    @Inject
    private TreeCollectionTreeParser sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException {
        base = new TreeParserTestBase(reader, sut,
                "collection-tree-module-group.json");
    }

    @Test
    public void parseModuleNameTest() {
        base.assertModuleNameEquals("tree-collection");
    }

    @Test
    public void parseElementsTest() {
        base.assertElementSizeEquals(9);
    }

    @Test
    public void parseRelationsTest() {
        base.assertRelationSizeEquals(6);
    }

    @Test
    public void parseElementTypeTest() {
        base.assertElementsAreOfTypeTreeNodeElement();
    }

    @Test
    public void parseRelationTypeTest() {
        base.assertRelationsAreOfTypeTreeNodeRelation();
    }

    @Test
    public void parseElementStyleTest() {
        base.assertStyleOfElementEquals(6, 13888729, 7324547, "dashed", 4);
    }

    @Test
    public void parseRelationStyleTest() {
        base.assertStyleOfRelationEquals(3, 14006715, 12148586, "dotted", 3);
    }
}
