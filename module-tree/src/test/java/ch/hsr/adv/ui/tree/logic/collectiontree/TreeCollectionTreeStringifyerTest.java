package ch.hsr.adv.ui.tree.logic.collectiontree;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.presets.ADVDefaultRelationStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.tree.domain.GeneralTreeTestNode;
import com.google.gson.JsonElement;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(JukitoRunner.class)
public class TreeCollectionTreeStringifyerTest {

    private ModuleGroup moduleGroup;

    @Inject
    private TreeCollectionTreeStringifyer sut;

    @Inject
    private TreeCollectionTreeParser parser;

    @Before
    public void setup() {

        GeneralTreeTestNode<String> root1Node = new GeneralTreeTestNode<>(
                "test-root1");
        GeneralTreeTestNode<String> root2Node = new GeneralTreeTestNode<>(
                "test-root2");
        GeneralTreeTestNode<String> child1Node = new GeneralTreeTestNode<>(
                "test-child1");


        TreeNodeElement root1 = new TreeNodeElement(root1Node, 1);
        TreeNodeElement root2 = new TreeNodeElement(root2Node, 3);
        TreeNodeElement child1 = new TreeNodeElement(child1Node, 2);

        TreeNodeRelation relation12 = new TreeNodeRelation(1, 2,
                new ADVDefaultRelationStyle());

        moduleGroup = new ModuleGroup("test-collection-module");
        moduleGroup.addElement(root1);
        moduleGroup.addElement(root2);
        moduleGroup.addElement(child1);
        moduleGroup.addRelation(relation12);
    }

    @Test
    public void stringifyElementsTest() {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals(3, parsedModuleGroup.getElements().size());
        assertEquals("test-child1",
                parsedModuleGroup.getElements().get(2).getContent());
    }

    @Test
    public void stringifyRelationsTest() {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals(1, parsedModuleGroup.getRelations().size());
    }

    @Test
    public void stringifyModuleNameTest() {
        JsonElement actual = sut.stringify(moduleGroup);

        ModuleGroup parsedModuleGroup = parser.parse(actual);
        assertEquals("test-collection-module",
                parsedModuleGroup.getModuleName());
    }
}
