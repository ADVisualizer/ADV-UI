package ch.hsr.adv.ui.core.logic.domain.styles;


/**
 * ADVStyle implementation that uses 'low level' values.
 */
public class ADVValueStyle implements ADVStyle {
    private int fillColor;
    private int strokeColor;
    private String strokeStyle;
    private int strokeThickness;

    public ADVValueStyle() {
        this(0, 0, ADVStrokeStyle.NONE.getStyle(), 0);
    }

    public ADVValueStyle(int fillColor, int strokeColor, String strokeStyle,
                         int strokeThickness) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeStyle = strokeStyle;
        this.strokeThickness = strokeThickness;
    }

    @Override
    public int getFillColor() {
        return fillColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    @Override
    public String getStrokeStyle() {

        return strokeStyle;
    }

    public void setStrokeStyle(ADVStrokeStyle style) {
        this.strokeStyle = style.getStyle();
    }

    @Override
    public int getStrokeThickness() {
        return strokeThickness;
    }

    public void setStrokeThickness(int strokeThickness) {
        this.strokeThickness = strokeThickness;
    }
}
