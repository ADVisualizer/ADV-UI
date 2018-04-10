package ch.adv.ui.core.domain.styles.presets;

import ch.adv.ui.core.domain.styles.ADVStrokeStyle;
import ch.adv.ui.core.domain.styles.ADVStyle;

/**
 * The default implementation of the ADV Style sets standard values for all
 * style variables.
 */
public class ADVDefaultLineStyle implements ADVStyle {
    private static final int BLACK = 0;
    private static final int DEFAULT_THICKNESS = 1;

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
        return ADVStrokeStyle.SOLID.getStyle();
    }

    @Override
    public int getStrokeThickness() {
        return DEFAULT_THICKNESS;
    }
}
