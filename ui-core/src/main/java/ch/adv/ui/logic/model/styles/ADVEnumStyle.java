package ch.adv.ui.logic.model.styles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ADVEnumStyle implements ADVStyle {

    private ADVColor strokeColor;
    private ADVStrokeStyle strokeStyle;
    private ADVStrokeThickness
            strokeThickness;
    private ADVColor fillColor;

    private static final Logger logger = LoggerFactory.getLogger(ADVStyle
            .class);

    public ADVEnumStyle() {
        this(null, null, null, null);
    }

    public ADVEnumStyle(ADVColor strokeColor,
                        ADVStrokeStyle strokeStyle,
                        ADVStrokeThickness
                                strokeThickness) {
        this(strokeColor, strokeStyle,
                strokeThickness, null);
    }

    public ADVEnumStyle(ADVColor strokeThicknessColor,
                        ADVStrokeStyle strokeStyle,
                        ADVStrokeThickness
                                strokeThickness, ADVColor
                                fillColor) {

        if (strokeThicknessColor != null) {
            this.strokeColor = strokeThicknessColor;
        } else {
            this.strokeColor = ADVColor.STANDARD;
        }

        if (strokeThickness != null) {
            this.strokeThickness = strokeThickness;
        } else {
            this.strokeThickness = ADVStrokeThickness.STANDARD;
        }

        if (strokeStyle != null) {
            this.strokeStyle = strokeStyle;
        } else {
            this.strokeStyle = ADVStrokeStyle
                    .THROUGH;
        }

        if (fillColor != null) {
            this.fillColor = fillColor;
        } else {
            this.fillColor = ADVColor.STANDARD;
        }
    }


    public ADVColor getFillColorEnum() {
        return fillColor;
    }

    public void setFillColor(ADVColor fillColor) {
        this.fillColor = fillColor;
    }

    public ADVColor getStrokeColorEnum() {
        return strokeColor;
    }

    public void setStrokeColorEnum(ADVColor strokeColor) {
        this.strokeColor = strokeColor;
    }

    public ADVStrokeStyle getStrokeStyleEnum() {
        return strokeStyle;
    }

    public void setStrokeStyleEnum(ADVStrokeStyle
                                           strokeStyle) {
        this.strokeStyle = strokeStyle;
    }

    public ADVStrokeThickness getStrokeThicknessEnum() {
        return strokeThickness;
    }

    public void setStrokeThicknessEnum(ADVStrokeThickness
                                               strokeThickness) {
        this.strokeThickness = strokeThickness;
    }

    @Override
    public String getFillColor() {
        return fillColor.getColor();
    }

    @Override
    public String getStrokeColor() {
        return strokeColor.getColor();
    }

    @Override
    public String getStrokeStyle() {
        return strokeStyle.getStyle();
    }

    @Override
    public String getStrokeThickness() {
        return strokeThickness.getThickness();
    }
}
