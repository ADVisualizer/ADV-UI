package ch.adv.ui.core.presentation.widgets;


import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.Region;


/**
 * Base class for all ADV widget nodes
 */
public abstract class ADVNode extends Region {

    abstract ConnectorType getConnectorTypeOutgoing();

    abstract ConnectorType getConnectorTypeIngoing();

    abstract Point2D getCenter();

    abstract ObjectProperty<Point2D> centerProperty();

}
