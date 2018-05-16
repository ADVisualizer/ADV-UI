package ch.hsr.adv.ui.core.logic.domain.styles;


/**
 * ADVStyle implementation that uses 'low level' values.
 */
public class ADVValueStyle implements ADVStyle {
    private int fillColor;
    private int strokeColor;
    private String strokeStyle;
    private int strokeThickness;

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


    @Override
    public int getStrokeColor() {
        return strokeColor;
    }

    @Override
    public String getStrokeStyle() {
        if (strokeStyle == null) {
            return ADVStrokeStyle.NONE.getStyle();
        } else {
            return strokeStyle;
        }
    }

    @Override
    public int getStrokeThickness() {
        return strokeThickness;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
    }

    public void setStrokeStyle(ADVStrokeStyle style) {
        this.strokeStyle = style.getStyle();
    }

    public void setStrokeThickness(int strokeThickness) {
        this.strokeThickness = strokeThickness;
    }
}
