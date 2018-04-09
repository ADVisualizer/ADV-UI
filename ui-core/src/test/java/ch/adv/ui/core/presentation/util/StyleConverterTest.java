package ch.adv.ui.core.presentation.util;

import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StyleConverterTest {

    @Test
    public void getStrokeStyleValidTest() {
        BorderStrokeStyle style = StyleConverter
                .getStrokeStyle("dotted");
        assertEquals(BorderStrokeStyle.DOTTED, style);
    }

    @Test
    public void getStrokeStyleInvalidTest() {
        BorderStrokeStyle style = StyleConverter
                .getStrokeStyle("stroked");
        assertEquals(null, style);
    }

    @Test
    public void getValidColorTest() {
        Paint color = StyleConverter.getColor(0);
        assertEquals(Color.BLACK, color);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInvalidColorTest() {
        StyleConverter.getColor(0xffffff1);
    }
}