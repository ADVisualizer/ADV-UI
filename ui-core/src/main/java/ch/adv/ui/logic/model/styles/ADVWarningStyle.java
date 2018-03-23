package ch.adv.ui.logic.model.styles;

public class ADVWarningStyle implements ADVStyle {
    @Override
    public String getFillColor() {
        return ADVColor.STANDARD.getColor();
    }

    @Override
    public String getStrokeColor() {
        return ADVColor.YELLOW.getColor();
    }

    @Override
    public String getStrokeStyle() {
        return ADVStrokeStyle.DOTTED.getStyle();
    }

    @Override
    public String getStrokeThickness() {
        return ADVStrokeThickness.BOLD.getThickness();
    }
}
