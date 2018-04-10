package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.domain.styles.ADVStyle;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import ch.adv.ui.core.presentation.util.StyleConverter;
import ch.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Positions the ArrayElements on the Pane
 */
@Singleton
public class ArrayLayouter implements Layouter {

    /**
     * Layouts an Array snapshot if it is not already layouted
     *
     * @param snapshot to be layouted
     * @return layouted snapshot
     */
    @Override
    public LayoutedSnapshot layout(Snapshot snapshot) {
        AutoScalePane scalePane = new AutoScalePane();

        HBox container = new HBox();
        snapshot.getElements().forEach(e -> {
            ArrayElement arrElement = (ArrayElement) e;

            LabeledNode node = new LabeledNode(arrElement
                    .getContent());
            ADVStyle style = arrElement.getStyle();

            node.setBackgroundColor(StyleConverter
                    .getColor(style.getFillColor()));
            node.setFontColor(Color.WHITE);

            node.setBorder(style.getStrokeThickness(),
                    StyleConverter.getColor(style.getStrokeColor()),
                    StyleConverter.getStrokeStyle(style.getStrokeStyle()));

            if (arrElement.getFixedPosX() > 0
                    && arrElement.getFixedPosY() > 0) {

                node.setY(arrElement.getFixedPosY());
                node.setX(arrElement.getFixedPosX());

                // add fixed positioned elements directly on the scale pane
                // because the hbox would ignore the fixed position.
                scalePane.addChildren(node);
            } else {
                container.getChildren().add(node);
            }
        });

        scalePane.addChildren(container);

        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot();
        layoutedSnapshot
                .setSnapshotDescription(snapshot.getSnapshotDescription());
        layoutedSnapshot.setSnapshotId(snapshot.getSnapshotId());
        layoutedSnapshot.setPane(scalePane);

        return layoutedSnapshot;
    }
}
