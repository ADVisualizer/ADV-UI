package ch.adv.ui.core.presentation.widgets;


import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;


/**
 * Base class for all ADV widget nodes
 */
public abstract class ADVNode extends Region {

    /**
     * Connector position for all outgoing connectors
     *
     * @return outgoing connector type
     */
    abstract ConnectorType getConnectorTypeOutgoing();

    /**
     * Connector position for all incoming connectors
     *
     * @return incoming connector type
     */
    abstract ConnectorType getConnectorTypeIncoming();

    /**
     * X/Y coordinate of the node center
     *
     * @return node center
     */
    abstract Point2D getCenter();

    /**
     * Object property for the center point
     *
     * @return center property
     */
    abstract ObjectProperty<Point2D> centerProperty();

}
