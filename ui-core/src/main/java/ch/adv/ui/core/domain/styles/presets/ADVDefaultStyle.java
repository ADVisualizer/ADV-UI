package ch.adv.ui.core.domain.styles.presets;

import ch.adv.ui.core.domain.styles.ADVStrokeStyle;
import ch.adv.ui.core.domain.styles.ADVStyle;
import com.google.gson.annotations.SerializedName;

/**
 * The default implementation of the ADV Style sets standard values for all
 * style variables.
 */
public class ADVDefaultStyle implements ADVStyle {
    @SerializedName("fillColor")
    private static final int FILL_COLOR = 0;
    @SerializedName("strokeColor")
    private static final int STROKE_COLOR = 0;
    @SerializedName("strokeStyle")
    private static final String STOKE_STYLE = ADVStrokeStyle.NONE.getStyle();
    @SerializedName("strokeThickness")
    private static final int STROKE_THICKNESS = 0;

    @Override
    public int getFillColor() {
        return FILL_COLOR;
    }

    @Override
    public int getStrokeColor() {
        return STROKE_COLOR;
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
