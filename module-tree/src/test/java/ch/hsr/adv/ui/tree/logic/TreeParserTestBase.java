package ch.hsr.adv.ui.tree.logic;

import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ADVRelation;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeElement;
import ch.hsr.adv.commons.tree.logic.domain.TreeNodeRelation;
import ch.hsr.adv.ui.core.access.FileDatastoreAccess;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TreeParserTestBase {

    private static final double DOUBLE_ACCURACY = 0.00001;

    private JsonElement jsonElement;

    private TreeParser sut;

    public TreeParserTestBase(FileDatastoreAccess reader, TreeParser sut,
                              String recourse) throws IOException {
        this.sut = sut;
        URL url = TreeParserTestBase.class.getClassLoader()
                .getResource(recourse);
        if (url == null) {
            throw new FileNotFoundException();
        }

        String json = reader.read(new File(url.getPath()));

        Gson gson = new Gson();
        jsonElement = gson.fromJson(json, JsonElement.class);
    }

    public void assertModuleNameEquals(String expected) {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals(expected, actual.getModuleName());
    }

    public void assertElementSizeEquals(int expected) {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals(expected, actual.getElements().size());
    }

    public void assertRelationSizeEquals(int expected) {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals(expected, actual.getRelations().size());
    }

    public void assertElementsAreOfTypeTreeNodeElement() {
        ModuleGroup actual = sut.parse(jsonElement);

        for (ADVElement<?> elem : actual.getElements()) {
            assertTrue(elem instanceof TreeNodeElement);
        }
    }

    public void assertRelationsAreOfTypeTreeNodeRelation() {
        ModuleGroup actual = sut.parse(jsonElement);

        for (ADVRelation<?> rel : actual.getRelations()) {
            assertTrue(rel instanceof TreeNodeRelation);
        }
    }

    public void assertStyleOfElementEquals(int elementIndex, int fillColor,
                                           int strokeColor,
                                           String strokeStyle,
                                           int strokeThickness) {
        ModuleGroup actual = sut.parse(jsonElement);

        ADVStyle parsedStyle = actual.getElements().get(elementIndex)
                .getStyle();
        assertEquals(fillColor, parsedStyle.getFillColor());
        assertEquals(strokeColor, parsedStyle.getStrokeColor());
        assertEquals(strokeStyle, parsedStyle.getStrokeStyle());
        assertEquals(strokeThickness, parsedStyle
                .getStrokeThickness(), DOUBLE_ACCURACY);
    }

    public void assertStyleOfRelationEquals(int relationIndex, int fillColor,
                                            int strokeColor,
                                            String strokeStyle,
                                            int strokeThickness) {
        ModuleGroup actual = sut.parse(jsonElement);

        ADVStyle parsedStyle = actual.getRelations().get(relationIndex)
                .getStyle();
        assertEquals(fillColor, parsedStyle.getFillColor());
        assertEquals(strokeColor, parsedStyle.getStrokeColor());
        assertEquals(strokeStyle, parsedStyle.getStrokeStyle());
        assertEquals(strokeThickness, parsedStyle
                .getStrokeThickness(), DOUBLE_ACCURACY);
    }

    public void assertFlagIsSet(String flag) {
        ModuleGroup actual = sut.parse(jsonElement);

        assertTrue(actual.getFlags().contains(flag));
    }

    public void assertMetaDataEquals(String metaDataKey, String expectedValue) {
        ModuleGroup actual = sut.parse(jsonElement);

        assertEquals(expectedValue, actual.getMetaData().get(metaDataKey));
    }
}
