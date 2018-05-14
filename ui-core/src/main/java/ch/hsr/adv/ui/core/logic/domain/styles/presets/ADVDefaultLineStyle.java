package ch.hsr.adv.ui.core.logic.domain.styles.presets;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStrokeStyle;

/**
 * The default implementation of the ADV Style sets standard values for all
 * style variables.
 */
public class ADVDefaultLineStyle extends ADVDefaultStyle {

    private static final int STROKE_THICKNESS = 1;
    private static final String STOKE_STYLE = ADVStrokeStyle.SOLID.getStyle();

    @Override
    public String getStrokeStyle() {
        return STOKE_STYLE;
    }

    @Override
    public int getStrokeThickness() {
        return STROKE_THICKNESS;
    }
}
