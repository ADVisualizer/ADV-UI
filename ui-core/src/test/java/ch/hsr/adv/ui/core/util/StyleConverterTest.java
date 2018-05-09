package ch.hsr.adv.ui.core.util;

import ch.hsr.adv.ui.core.presentation.util.StyleConverter;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(null, style);
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