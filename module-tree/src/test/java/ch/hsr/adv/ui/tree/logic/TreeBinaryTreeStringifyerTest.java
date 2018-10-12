package ch.hsr.adv.ui.tree.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultRelationStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import ch.hsr.adv.ui.core.logic.exceptions.ADVParseException;
import ch.hsr.adv.ui.tree.domain.BinaryTreeTestNode;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class TreeBinaryTreeStringifyerTest {

    private ModuleGroup moduleGroup;

    @Inject
    private TreeBinaryTreeStringifyer sut;

    @Inject
    private TreeBinaryTreeParser parser;

    @Before
    public void setup(FileDatastoreAccess reader,
                      TreeBinaryTreeParser testParser)
            throws ADVParseException, IOException {

        BinaryTreeTestNode<String> childNode = new BinaryTreeTestNode<>("test"
                + "-child");
        BinaryTreeTestNode<String> rootNode = new BinaryTreeTestNode<>("test"
                + "-root", new ADVDefaultElementStyle(), childNode, null);

        TreeNodeElement root = new TreeNodeElement(rootNode, 1);
        TreeNodeElement child = new TreeNodeElement(rootNode, 2);

        TreeNodeRelation relation = new TreeNodeRelation(1, 2,
                new ADVDefaultRelationStyle());

        moduleGroup = new ModuleGroup("test-module");
        moduleGroup.addElement(root);
        moduleGroup.addElement(child);
        moduleGroup.addRelation(relation);
    }

    @Test
    public void stringifyElementsTest() throws ADVParseException {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals(2, parsedModuleGroup.getElements().size());
        assertEquals("test-root", parsedModuleGroup.getElements().get(0)
                .getContent());
    }

    @Test
    public void stringifyRelationsTest() throws ADVParseException {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals(1, parsedModuleGroup.getRelations().size());
    }

    @Test
    public void stringifyModuleNameTest() throws ADVParseException {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals("test-module", parsedModuleGroup.getModuleName());
    }
}