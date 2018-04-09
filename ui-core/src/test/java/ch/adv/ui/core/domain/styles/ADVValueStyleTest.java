package ch.adv.ui.core.domain.styles;

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