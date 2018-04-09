package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
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
            LabeledNode n = new LabeledNode(arrElement.getContent());
            //TODO: use arrElement.getStyle()
            n.setBackgroundColor(Color.BLACK);
            n.setFontColor(Color.WHITE);
            container.getChildren().add(n);
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
