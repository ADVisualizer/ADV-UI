package ch.adv.ui.core.logic.mocks;

import ch.adv.ui.core.logic.Layouter;
import ch.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.adv.ui.core.logic.domain.Snapshot;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;

import java.util.List;

@Singleton
public class TestLayouter implements Layouter {

    private final Pane testPane = new Pane();
    private final LayoutedSnapshot testLayoutedSnapshot =
            new LayoutedSnapshot(1, testPane);

    @Override
    public LayoutedSnapshot layout(Snapshot snapshot, List<String> flags) {
        return testLayoutedSnapshot;
    }

    public Pane getTestPane() {
        return testPane;
    }

    public LayoutedSnapshot getTestLayoutedSnapshot() {
        return testLayoutedSnapshot;
    }
}
