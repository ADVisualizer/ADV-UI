package ch.hsr.adv.ui.array.logic.domain;

import ch.hsr.adv.commons.core.logic.domain.ADVRelation;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets
        .ADVDefaultRelationStyle;

/**
 * Relation between two
 * {@link ch.hsr.adv.commons.array.logic.domain.ArrayElement}s
 */
public class ArrayRelation implements ADVRelation<String> {

    private long sourceElementId;
    private long targetElementId;
    private String label;
    private ADVStyle style;

    public ArrayRelation() {
        this.style = new ADVDefaultRelationStyle();
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

    @Override
    public boolean isDirected() {
        return true;
    }

    @Override
    public void setDirected(boolean b) {
        throw new UnsupportedOperationException();
    }
}
