package ch.hsr.adv.ui.core.logic.domain;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;

/**
 * Represents a relation between to {@link ADVElement}
 *
 * @param <T> content type
 */
public interface ADVRelation<T> {

    /**
     * Returns the source {@link ADVElement} id
     *
     * @return id
     */
    long getSourceElementId();

    /**
     * Sets the source of the relation. For example the starting node of an
     * edge.
     *
     * @param sourceElementId id of the source
     */
    void setSourceElementId(long sourceElementId);

    /**
     * Returns the target {@link ADVElement} id
     *
     * @return id
     */
    long getTargetElementId();

    /**
     * Sets the target of the relation. For example the ending node of an edge.
     *
     * @param targetElementId id of the target
     */
    void setTargetElementId(long targetElementId);

    /**
     * Returns relation label
     *
     * @return label
     */
    T getLabel();

    /**
     * Sets the label for the relation. For example the weight of an edge.
     *
     * @param label descriptive label for the relation
     */
    void setLabel(String label);

    /**
     * Returns the relation style
     *
     * @return style
     */
    ADVStyle getStyle();

    /**
     * Sets the style of the relation to visually differentiate it from other
     * relations. For example to show a change to the previous snapshot.
     *
     * @param style for the relation
     */
    void setStyle(ADVStyle style);

    /**
     * @return whether the relation is directed. If {@code false},
     * sourceElement and targetElement can be switched without changing the
     * information of the relation.
     */
    boolean isDirected();
}
