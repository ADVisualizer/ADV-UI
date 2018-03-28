package ch.adv.ui.logic.model.styles;

/**
 * Represents a stroke style
 * <p>
 * Available options: DOTTED, DASHED, THROUGH
 */
public enum ADVStrokeStyle {

    DOTTED("dotted"), DASHED("dashed"), THROUGH("through");

    private String style;

    ADVStrokeStyle(String style) {
        this.style = style.toLowerCase();
    }

    public String getStyle() {
        return style;
    }

    /**
     * Cases insensitive lookup for the stroke style
     * @param styleName name
     * @return ADVStrokeStyle enum instance
     */
    public static ADVStrokeStyle byName(String styleName) {
        return valueOf(styleName.toUpperCase());
    }


}
