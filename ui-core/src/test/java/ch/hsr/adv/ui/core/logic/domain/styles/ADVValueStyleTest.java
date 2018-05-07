package ch.hsr.adv.ui.core.logic.domain.styles;

import com.google.inject.Inject;
import org.jukito.JukitoRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JukitoRunner.class)
public class ADVValueStyleTest {

    @Inject
    private ADVValueStyle sut;

    @Test
    public void defaultStrokeStyleTest() {
        Assert.assertEquals(ADVStrokeStyle.NONE.getStyle(),
                sut.getStrokeStyle());
    }
}