package ch.hsr.adv.ui.graph.logic;

import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.*;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.presets.ADVDefaultLineStyle;
import ch.hsr.adv.ui.core.presentation.widgets.*;
import ch.hsr.adv.ui.graph.logic.domain.Matrix;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
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

    private Map<Long, LabeledNode> vertices;
    private AutoScalePane scalePane;
    private List<ADVRelation> relations;
    private List<ADVElement> elements;
    private Matrix edgeMatrix;

    @Inject
    private GraphLayouterUtil util;

    @Override
    public LayoutedSnapshot layout(Snapshot snapshot, List<String> flags) {
        logger.info("Layouting graph snapshot...");
        vertices = new HashMap<>();
        scalePane = new AutoScalePane();
        elements = snapshot.getElements();
        relations = snapshot.getRelations();

        createElements();
        createRelations();

        LayoutedSnapshot layoutedSnapshot = new LayoutedSnapshot(
                snapshot.getSnapshotId(), scalePane);
        layoutedSnapshot.setSnapshotDescription(
                snapshot.getSnapshotDescription());
        return layoutedSnapshot;
    }


    private void createElements() {
        elements.forEach(e -> {
            String label = e.getContent().toString();
            LabeledNode vertex = new LabeledNode(label, true);
            if (e.getFixedPosX() != 0 || e.getFixedPosY() != 0) {
                vertex.setX(e.getFixedPosX());
                vertex.setY(e.getFixedPosY());
            } //TODO: else use layouting algorithm
            util.setStyling(vertex, e.getStyle());
            vertices.put(e.getElementId(), vertex);
            scalePane.addChildren(vertex);
        });
    }

    //TODO: change ConnectorType if more than one edge between two vertices
    private void createRelations() {
        edgeMatrix = new Matrix(new ArrayList<>(vertices.values()));
        relations.forEach(r -> {
            LabeledNode source = vertices.get(r.getSourceElementId());
            LabeledNode target = vertices.get(r.getTargetElementId());
            ADVStyle style = r.getStyle();
            if (style == null) {
                style = new ADVDefaultLineStyle();
            }
            LabeledEdge.DirectionType type = LabeledEdge.DirectionType.NONE;
            if (r.isDirected()) {
                type = LabeledEdge.DirectionType.UNIDIRECTIONAL;
            }
            LabeledEdge edge;
            if (edgeMatrix.hasEdges(source, target)) {
                edge = new CurvedLabeledEdge(
                        r.getLabel(),
                        source, ConnectorType.TOP,
                        target, ConnectorType.TOP,
                        scalePane.getContent(),
                        style, type
                );
            } else {
                edge = new LabeledEdge(
                        r.getLabel(),
                        source, ConnectorType.DIRECT,
                        target, ConnectorType.DIRECT,
                        scalePane.getContent(),
                        style, type);
            }
            edgeMatrix.addEdge(source, target);

            scalePane.addChildren(edge);
        });
    }


}
