package ch.adv.ui.core.presentation.widgets;


import javafx.scene.layout.Region;

public abstract class ADVNode extends Region {

    abstract ConnectorPoint getConnectorPointOutgoing();

    abstract ConnectorPoint getConnectorPointIngoing();

}
