package ch.adv.ui.array.logic.domain;

import ch.adv.ui.core.logic.domain.ADVRelation;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.adv.ui.core.logic.domain.styles.presets.ADVDefaultLineStyle;

/**
 * Relation between two {@link ArrayElement}s
 */
public class ArrayRelation implements ADVRelation {

    private long sourceElementId;
    private long targetElementId;
    private String label;
    private ADVStyle style;

    public ArrayRelation() {
        this.style = new ADVDefaultLineStyle();
    }

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
