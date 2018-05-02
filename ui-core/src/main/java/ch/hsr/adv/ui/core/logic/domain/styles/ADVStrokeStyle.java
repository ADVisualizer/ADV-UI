package ch.hsr.adv.ui.core.logic.domain.styles;

import com.google.gson.annotations.SerializedName;

/**
 * Defines the style of the stroke or border of an element.
 */
public enum ADVStrokeStyle {
    @SerializedName("dotted")
    DOTTED("dotted"),
    @SerializedName("dashed")
    DASHED("dashed"),
    @SerializedName("solid")
    SOLID("solid"),
    @SerializedName("none")
    NONE("none");

    private String style;

    ADVStrokeStyle(String style) {
        this.style = style.toLowerCase();
    }

    /**
     * Returns the enum of the specified string.
     *
     * @param styleName string linked to the enum value
     * @return the enum
     */
    public static ADVStrokeStyle byName(String styleName) {
        return valueOf(styleName.toUpperCase());
    }

    public String getStyle() {
        return style;
    }
}
