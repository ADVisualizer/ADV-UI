package ch.hsr.adv.ui.core.logic.domain;

import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;

/**
 * Represents a generic elements which is displayed on the ADV UI.
 *
 * @param <T> content type
 */
public interface ADVElement<T> {

    /**
     * Returns the identifier
     * @return element id
     */
    long getElementId();

    /**
     * Returns the style for to the element
     * @return style
     */
    ADVStyle getStyle();

    /**
     * Returns X position of the element
     * @return posX
     */
    int getFixedPosX();

    /**
     * Returns Y position of the element
     * @return posY
     */
    int getFixedPosY();

    /**
     * Returns the content of the element
     *
     * @return content object
     */
    T getContent();
}
