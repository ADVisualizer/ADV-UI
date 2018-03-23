package ch.adv.ui.logic.model;

import ch.adv.ui.logic.model.styles.ADVStyle;

public interface ADVRelation {

    long getSourceElementId();

    /**
     * Sets the source of the relation. For example the starting node of an edge.
     *
     * @param sourceElementId id of the source
     */
    void setSourceElementId(long sourceElementId);

    long getTargetElementId();

    /**
     * Sets the target of the relation. For example the ending node of an edge.
     *
     * @param targetElementId id of the target
     */
    void setTargetElementId(long targetElementId);

    String getLabel();

    /**
     * Sets the label for the relation. For example the weight of an edge.
     *
     * @param label descriptive label for the relation
     */
    void setLabel(String label);

    ADVStyle getStyle();

    /**
     * Sets the style of the relation to visually differentiate it from other relations. For example to show a change to the previous snapshot.
     *
     * @param style for the relation
     */
    void setStyle(ADVStyle style);
}
