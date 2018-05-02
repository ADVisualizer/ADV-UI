package ch.hsr.adv.ui.graph.logic.domain;


import ch.hsr.adv.ui.core.logic.domain.ADVRelation;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;

/**
 * Object adapter. Maps an ADVEdge to an ADVRelation.
 */
public class GraphRelation implements ADVRelation {
    private long sourceElementId;
    private long targetElementId;
    private ADVStyle style;
    private String label;
    private boolean isDirected;


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
