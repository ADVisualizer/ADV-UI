package ch.adv.ui.core.domain.styles.presets;

import ch.adv.ui.core.domain.styles.ADVStrokeStyle;
import ch.adv.ui.core.domain.styles.ADVStyle;

/**
 * The default implementation of the ADV Style sets standard values for all
 * style variables.
 */
public class ADVDefaultStyle implements ADVStyle {
    private static final int BLACK = 0;
    private static final int NO_BORDER = 0;

    @Override
    public int getFillColor() {
        return BLACK;
    }

    @Override
    public int getStrokeColor() {
        return BLACK;
    }

    @Override
    public String getStrokeStyle() {
        return ADVStrokeStyle.NONE.getStyle();
    }

    @Override
    public int getStrokeThickness() {
        return NO_BORDER;
    }
}
