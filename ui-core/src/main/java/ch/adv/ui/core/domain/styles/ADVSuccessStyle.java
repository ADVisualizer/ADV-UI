package ch.adv.ui.core.domain.styles;

/**
 * Preconfigured success style
 */
public class ADVSuccessStyle implements ADVStyle {

    @Override
    public String getFillColor() {
        return ADVColor.STANDARD.getColor();
    }

    @Override
    public String getStrokeColor() {
        return ADVColor.GREEN.getColor();
    }

    @Override
    public String getStrokeStyle() {
        return ADVStrokeStyle.THROUGH.getStyle();
    }

    @Override
    public String getStrokeThickness() {
        return ADVStrokeThickness.STANDARD.getThickness();
    }
}
