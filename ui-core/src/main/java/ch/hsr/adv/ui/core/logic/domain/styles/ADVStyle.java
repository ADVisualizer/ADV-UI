package ch.hsr.adv.ui.core.logic.domain.styles;

/**
 * Encapsulates the style of an element or a relation.
 */
public interface ADVStyle {
    /**
     * Returns the background color. Only relevant for actual nodes (circle,
     * square, ...) not for lines, arrows, path.
     * <p>
     * Use hex value of a color.
     *
     * @return the background color
     */
    int getFillColor();

    /**
     * Returns the color of the stroke or border
     * <p>
     * Use hex value of a color.
     *
     * @return the color of the stroke or border
     */
    int getStrokeColor();

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
    int getStrokeThickness();
}
