package ch.hsr.adv.ui.core.presentation.util;

import com.google.inject.Inject;
import javafx.beans.binding.StringBinding;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.junit.Assert.*;

@RunWith(JukitoRunner.class)
public class I18nTest {

    @Before
    public void setUp() {
        I18n.setLocale(Locale.UK);
    }

    @Test
    public void createStringBindingTest() {
        StringBinding actual = I18n.createStringBinding("scale.slow");
        assertEquals("slow", actual.get());

        I18n.setLocale(new Locale("de", "CH"));
        actual = I18n.createStringBinding("scale.slow");
        assertEquals("langsam", actual.get());
    }

    @Test
    public void buttonForKeyTest() {
        Button actual = I18n.buttonForKey("scale.slow");
        assertEquals("slow", actual.getText());

        I18n.setLocale(new Locale("de", "CH"));
        actual = I18n.buttonForKey("scale.slow");
        assertEquals("langsam", actual.getText());
    }

    @Test
    public void tooltipForKeyTest() {
        Tooltip actual = I18n.tooltipForKey("scale.slow");
        assertEquals("slow", actual.getText());

        I18n.setLocale(new Locale("de", "CH"));
        actual = I18n.tooltipForKey("scale.slow");
        assertEquals("langsam", actual.getText());
    }
}