package ch.hsr.adv.ui.graph.logic.domain;


import ch.adv.ui.core.logic.domain.ADVRelation;
import ch.adv.ui.core.logic.domain.styles.ADVStyle;

/**
 * Object adapter. Maps an ADVEdge to an ADVRelation.
 */
public class GraphRelation implements ADVRelation {
    private long sourceElementId;
    private long targetElementId;
    private ADVStyle style;
    private String content;
    private boolean isDirected;


    public GraphRelation() {

    }

    @Override
    public long getSourceElementId() {
        return 0;
    }

    @Override
    public void setSourceElementId(long sourceElementId) {

    }

    @Override
    public long getTargetElementId() {
        return 0;
    }

    @Override
    public void setTargetElementId(long targetElementId) {

    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void setLabel(String label) {

    }

    @Override
    public ADVStyle getStyle() {
        return null;
    }

    @Override
    public void setStyle(ADVStyle style) {

    }
}
