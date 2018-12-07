package ch.hsr.adv.ui.tree.logic.binarytree;

import ch.hsr.adv.commons.tree.logic.ConstantsTree;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.tree.logic.TreeParserTestBase;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(JukitoRunner.class)
public class TreeBinaryTreeParserTest {

    private TreeParserTestBase base;

    @Inject
    private TreeBinaryTreeParser sut;

    @Before
    public void setUp(FileDatastoreAccess reader) throws IOException {
        base = new TreeParserTestBase(reader, sut,
                "binary-tree-module-group.json");
    }

    @Test
    public void parseModuleNameTest() {
        base.assertModuleNameEquals("tree-binary");
    }

    @Test
    public void parseElementsTest() {
        base.assertElementSizeEquals(6);
    }

    @Test
    public void parseRelationsTest() {
        base.assertRelationSizeEquals(5);
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
        base.assertStyleOfElementEquals(3, 13888729, 7324547, "solid", 2);
    }

    @Test
    public void parseRelationStyleTest() {
        base.assertStyleOfRelationEquals(0, 14006715, 12148586, "dashed", 7);
    }

    @Test
    public void parseFlagTest() {
        base.assertFlagIsSet(ConstantsTree.SHOW_ARRAY_INDICES);
    }

    @Test
    public void parseMetaDataTest() {
        base.assertMetaDataEquals("max-right-tree-height", "2");
        base.assertMetaDataEquals("max-left-tree-height", "3");
    }
}
