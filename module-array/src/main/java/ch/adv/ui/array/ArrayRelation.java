package ch.adv.ui.array;

import ch.adv.ui.core.domain.ADVRelation;
import ch.adv.ui.core.domain.styles.ADVStyle;

/**
 * Relation between two {@link ArrayElement}s
 */
public class ArrayRelation implements ADVRelation {

    private long sourceElementId;
    private long targetElementId;
    private String label;
    private ADVStyle style;

    @Override
    public long getSourceElementId() {
        return sourceElementId;
    }

    @Override
    public void setSourceElementId(long sourceElementId) {
        this.sourceElementId = sourceElementId;
    }

    @Override
    public long getTargetElementId() {
        return targetElementId;
    }

    @Override
    public void setTargetElementId(long targetElementId) {
        this.targetElementId = targetElementId;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public ADVStyle getStyle() {
        return style;
    }

    @Override
    public void setStyle(ADVStyle style) {
        this.style = style;
    }
}
