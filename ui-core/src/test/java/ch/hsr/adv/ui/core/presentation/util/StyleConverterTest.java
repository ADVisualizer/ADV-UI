package ch.hsr.adv.ui.core.presentation.util;

import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StyleConverterTest {

    @Test
    public void getStrokeStyleValidTest() {
        // WHEN
        BorderStrokeStyle style = StyleConverter.getStrokeStyle("dotted");

        // THEN
        assertEquals(BorderStrokeStyle.DOTTED, style);
    }

    @Test
    public void getStrokeStyleInvalidTest() {
        // WHEN
        BorderStrokeStyle style = StyleConverter.getStrokeStyle("stroked");

        // THEN
        assertNull(style);
    }

    @Test
    public void getValidColorTest() {
        // WHEN
        Paint color = StyleConverter.getColorFromHexValue(0);

        // THEN
        assertEquals(Color.BLACK, color);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInvalidColorTest() {
        StyleConverter.getColorFromHexValue(0xffffff1);
    }
}