package ch.hsr.adv.ui.core.logic.domain.styles.presets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStrokeStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;

/**
 * The default implementation of the ADV Style sets standard values for all
 * style variables.
 */
public class ADVDefaultStyle implements ADVStyle {

    private static final int BLACK = 0;
    private static final int STROKE_THICKNESS = 2;
    private static final String STOKE_STYLE = ADVStrokeStyle.SOLID.getStyle();

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
        return STOKE_STYLE;
    }

    @Override
    public int getStrokeThickness() {
        return STROKE_THICKNESS;
    }
}
