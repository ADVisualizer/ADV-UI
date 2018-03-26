package ch.adv.ui.logic.model.styles;

/**
 * Encapsulates the style of an element of a relation.
 */
public interface ADVStyle {
    /**
     * Returns the background color. Only relevant for actual nodes (circle,
     * square, ...) not for lines, arrows, path.
     *
     * @return the background color
     */
    String getFillColor();

    /**
     * Returns the color of the stroke or border
     *
     * @return the color of the stroke or border
     */
    String getStrokeColor();

    /**
     * Returns the style of the stroke. For example dotted or dashed.
     *
     * @return the style of the stroke
     */
    String getStrokeStyle();

    /**
     * Returns the thickness of the stroke or border.
     *
     * @return the thickness of the stroke
     */
    String getStrokeThickness();
}
