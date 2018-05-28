package ch.hsr.adv.ui.core.presentation.util;

import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static org.junit.Assert.assertTrue;

@RunWith(JukitoRunner.class)
public class ReplaySliderStringConverterTest {

    private static final String SLOW = "slow";
    private static final String MEDIUM = "medium";
    private static final String FAST = "fast";

    @Inject
    ReplaySliderStringConverter sut;

    @Before
    public void setUp() throws Exception {
        I18n.setLocale(Locale.UK);
    }

    @Test
    public void slowTest() {
        // WHEN
        String slow = sut.toString(0d);
        String slow2 = sut.toString(0.5d);
        String slow3 = sut.toString(1d);
        String slow4 = sut.toString(1.5d);
        String slow5 = sut.toString(1.9d);
        String slow6 = sut.toString(-1d);

        // THEN
        assertTrue(slow.contains(SLOW));
        assertTrue(slow2.contains(SLOW));
        assertTrue(slow3.contains(SLOW));
        assertTrue(slow4.contains(SLOW));
        assertTrue(slow5.contains(SLOW));
        assertTrue(slow6.contains(SLOW));
    }

    @Test
    public void mediumTest() {
        // WHEN
        String medium = sut.toString(2d);
        String medium2 = sut.toString(2.5d);
        String medium3 = sut.toString(2.9d);

        // THEN
        assertTrue(medium.contains(MEDIUM));
        assertTrue(medium2.contains(MEDIUM));
        assertTrue(medium3.contains(MEDIUM));
    }

    @Test
    public void fastTest() {
        // WHEN
        String medium = sut.toString(3.0);
        String medium2 = sut.toString(100d);
        String medium3 = sut.toString(3.5d);

        // THEN
        assertTrue(medium.contains(FAST));
        assertTrue(medium2.contains(FAST));
        assertTrue(medium3.contains(FAST));
    }
}