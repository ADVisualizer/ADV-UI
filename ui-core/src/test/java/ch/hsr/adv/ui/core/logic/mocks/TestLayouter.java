package ch.hsr.adv.ui.core.logic.mocks;

import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.LayoutedSnapshot;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;

import java.util.List;

@Singleton
public class TestLayouter implements Layouter {

    private final Pane testPane = new Pane();
    private final LayoutedSnapshot testLayoutedSnapshot =
            new LayoutedSnapshot(1, testPane);

    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        return testPane;
    }

    public LayoutedSnapshot getLayoutedSnapshotTest() {
        return testLayoutedSnapshot;
    }
}
