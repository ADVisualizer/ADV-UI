package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.*;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.presentation.widgets.AutoScalePane;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledEdge;
import ch.hsr.adv.ui.core.presentation.widgets.LabeledNode;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates JavaFX Nodes for the graph elements and adds them to a pane
 */
@Singleton
@Module("graph")
public class GraphLayouter implements Layouter {
    private static final Logger logger = LoggerFactory.getLogger(
            GraphLayouter.class);
    private final Map<Long, LabeledNode> vertices = new HashMap<>();
    List<ADVRelation> relations;
    private AutoScalePane scalePane = new AutoScalePane();
    private List<ADVElement> elements;

    @Override
    public LayoutedSnapshot layout(Snapshot snapshot, List<String> flags) {
        elements = snapshot.getElements();
        relations = snapshot.getRelations();

        createElements();
        createRelations();

        return new LayoutedSnapshot(snapshot.getSnapshotId(), scalePane);
    }

    //TODO: change ConnectorType if more than one edge between two vertices
    private void createElements() {
        elements.forEach(e -> {
            String label = e.getContent().toString();
            LabeledNode vertex = new LabeledNode( label, true);
            vertices.put(e.getElementId(), vertex);
            scalePane.addChildren(vertex);
        });
    }

    private void createRelations() {
        relations.forEach(r -> {
            LabeledNode source = vertices.get(r.getSourceElementId());
            LabeledNode target = vertices.get(r.getTargetElementId());
            scalePane.addChildren(new LabeledEdge(
                    r.getLabel(), source, target, r.getStyle()));
        });
    }


}
