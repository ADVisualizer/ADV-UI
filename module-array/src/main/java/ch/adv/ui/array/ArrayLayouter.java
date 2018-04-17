package ch.adv.ui.array;

import ch.adv.ui.core.domain.Snapshot;
import ch.adv.ui.core.presentation.Layouter;
import ch.adv.ui.core.presentation.domain.LayoutedSnapshot;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Positions the ArrayElements on the Pane
 */
@Singleton
public class ArrayLayouter implements Layouter {

    public static final String SHOW_OBJECT_RELATIONS = "SHOW_OBJECT_RELATIONS";
    private static final Logger logger = LoggerFactory.getLogger(
            ArrayLayouter.class);

    private final ArrayObjectReferenceLayouter arrayObjectReferenceLayouter;
    private final ArrayDefaultLayouter arrayDefaultLayouter;

    @Inject
    public ArrayLayouter(ArrayObjectReferenceLayouter objectLayouter,
                         ArrayDefaultLayouter arrayDefaultLayouter) {
        this.arrayObjectReferenceLayouter = objectLayouter;
        this.arrayDefaultLayouter = arrayDefaultLayouter;
    }

    /**
     * Layouts an Array snapshot if it is not already layouted
     *
     * @param snapshot to be layouted
     * @return layouted snapshot
     */
    @Override
    public LayoutedSnapshot layout(Snapshot snapshot, List<String> flags) {

        boolean showObjectRelations = flags.stream().anyMatch(
                f -> f.equals(SHOW_OBJECT_RELATIONS));

        Pane pane;
        if (showObjectRelations) {
            logger.info("Use Object Reference Array Layouter");
            pane = arrayObjectReferenceLayouter.layout(snapshot);
        } else {
            logger.info("Use Default Array Layouter");
            pane = arrayDefaultLayouter.layout(snapshot);
        }

        return createLayoutedSnapshot(snapshot, pane);
    }

    private LayoutedSnapshot createLayoutedSnapshot(Snapshot snapshot,
                                                    Pane scalePane) {
        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot();
        layoutedSnapshot.setSnapshotDescription(
                snapshot.getSnapshotDescription());
        layoutedSnapshot.setSnapshotId(snapshot.getSnapshotId());
        layoutedSnapshot.setPane(scalePane);
        return layoutedSnapshot;
    }
}
