package ch.hsr.adv.ui.tree.logic.generaltree;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultElementStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultRelationStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.tree.domain.GeneralTreeTestNode;
import ch.hsr.adv.ui.tree.logic.generaltree.TreeGeneralTreeParser;
import ch.hsr.adv.ui.tree.logic.generaltree.TreeGeneralTreeStringifyer;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class TreeGeneralTreeStringifyerTest {

    private ModuleGroup moduleGroup;

    @Inject
    private TreeGeneralTreeStringifyer sut;

    @Inject
    private TreeGeneralTreeParser parser;

    @Before
    public void setup() {
        createTestTree();
    }


    @Test
    public void stringifyElementsTest() {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals(5, parsedModuleGroup.getElements().size());
        assertEquals("test-root", parsedModuleGroup.getElements().get(0)
                .getContent());
    }

    @Test
    public void stringifyRelationsTest() {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals(4, parsedModuleGroup.getRelations().size());
    }

    @Test
    public void stringifyModuleNameTest() {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals("test-module", parsedModuleGroup.getModuleName());
    }

    private void createTestTree() {
        GeneralTreeTestNode<String> rootNode = new GeneralTreeTestNode<>(
                "test-root", new ADVDefaultElementStyle());
        GeneralTreeTestNode<String> child1Node = new GeneralTreeTestNode<>(
                "test-child1");
        GeneralTreeTestNode<String> child2Node = new GeneralTreeTestNode<>(
                "test-child2");
        GeneralTreeTestNode<String> child3Node = new GeneralTreeTestNode<>(
                "test-child3");
        GeneralTreeTestNode<String> subChild1Node = new GeneralTreeTestNode<>(
                "test-sub-child1");

        rootNode.addChild(child1Node);
        rootNode.addChild(child2Node);
        rootNode.addChild(child3Node);
        child1Node.addChild(subChild1Node);

        TreeNodeElement root = new TreeNodeElement(rootNode, 1);
        TreeNodeElement child1 = new TreeNodeElement(rootNode, 2);
        TreeNodeElement child2 = new TreeNodeElement(rootNode, 4);
        TreeNodeElement child3 = new TreeNodeElement(rootNode, 5);
        TreeNodeElement subChild1 = new TreeNodeElement(rootNode, 3);

        TreeNodeRelation relationRC1 = new TreeNodeRelation(1, 2,
                new ADVDefaultRelationStyle());
        TreeNodeRelation relationRC2 = new TreeNodeRelation(1, 4,
                new ADVDefaultRelationStyle());
        TreeNodeRelation relationRC3 = new TreeNodeRelation(1, 5,
                new ADVDefaultRelationStyle());
        TreeNodeRelation relationC1SC1 = new TreeNodeRelation(2, 3,
                new ADVDefaultRelationStyle());

        moduleGroup = new ModuleGroup("test-module");
        moduleGroup.addElement(root);
        moduleGroup.addElement(child1);
        moduleGroup.addElement(child2);
        moduleGroup.addElement(child3);
        moduleGroup.addElement(subChild1);
        moduleGroup.addRelation(relationRC1);
        moduleGroup.addRelation(relationRC2);
        moduleGroup.addRelation(relationRC3);
        moduleGroup.addRelation(relationC1SC1);
    }
}
