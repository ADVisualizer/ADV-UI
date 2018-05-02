package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.ui.array.logic.domain.ArrayElement;
import ch.hsr.adv.ui.core.logic.domain.Snapshot;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Default array layouter
 */
@Singleton
public class ArrayDefaultLayouter {

    private final ArrayLayouterUtil layouterUtil;

    private AutoScalePane scalePane;
    private HBox valueContainer;

    @Inject
    public ArrayDefaultLayouter(ArrayLayouterUtil layouterUtil) {
        this.layouterUtil = layouterUtil;
    }

    /**
     * Layouts a default array
     * @param snapshot snapshot to layout
     * @return layouted pane
     */
    public Pane layout(Snapshot snapshot) {
        initializeContainer();
        drawElements(snapshot);

        scalePane.addChildren(valueContainer);

        return scalePane;
    }

    private void initializeContainer() {
        scalePane = new AutoScalePane();
        valueContainer = new HBox();
        valueContainer.setAlignment(Pos.CENTER);
    }

    private void drawElements(Snapshot snapshot) {

        snapshot.getElements().forEach(e -> {
            ArrayElement element = (ArrayElement) e;
            ADVStyle style = element.getStyle();

            LabeledNode valueNode = new LabeledNode(element.getContent());
            layouterUtil.setStyling(valueNode, style);

            if (element.getFixedPosX() > 0 && element.getFixedPosY() > 0) {

                valueNode.setY(element.getFixedPosY());
                valueNode.setX(element.getFixedPosX());

                // add fixed positioned elements directly on the scale pane
                // because the hbox would ignore the fixed position.
                scalePane.addChildren(valueNode);
            } else {
                valueContainer.getChildren().add(valueNode);
            }
        });
    }
}
