package ch.hsr.adv.ui.core.logic.domain.styles;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStrokeStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVValueStyle;
import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class ADVValueStyleTest {

    @Inject
    private ADVValueStyle valueStyleUnderTest;

    @Test
    public void defaultStrokeStyleTest() {
        Assert.assertEquals(ADVStrokeStyle.NONE.getStyle(),
                valueStyleUnderTest.getStrokeStyle());
    }
}