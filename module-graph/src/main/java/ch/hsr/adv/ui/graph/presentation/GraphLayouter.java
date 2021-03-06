package ch.hsr.adv.ui.graph.presentation;


import ch.hsr.adv.commons.core.logic.domain.ADVElement;
import ch.hsr.adv.commons.core.logic.domain.ADVRelation;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.commons.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets
        .ADVDefaultElementStyle;
import ch.hsr.adv.commons.core.logic.domain.styles.presets
        .ADVDefaultRelationStyle;
import ch.hsr.adv.commons.graph.logic.ConstantsGraph;
import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.presentation.widgets.*;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Creates JavaFX Nodes for the graph elements and adds them to a pane
 * <p>
 * This class suppresses rawtype warnings, because Gson does not support
 * generic wildcards. See
 * <a href="https://github.com/ADVisualizer/ADV-Lib/issues/31">Issue 31</a>
 * for more details.
 */
@Singleton
@Module(ConstantsGraph.MODULE_NAME)
public class GraphLayouter implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            GraphLayouter.class);

    private Map<Long, LabeledNode> vertices;
    private AutoScalePane scalePane;
    @SuppressWarnings("rawtypes")
    private List<ADVRelation> relations;
    @SuppressWarnings("rawtypes")
    private List<ADVElement> elements;

    /**
     * Layouts the graph module group
     *
     * @param moduleGroup to be layouted
     * @return layouted pane
     */
    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {
        logger.info("Layouting graph snapshot...");
        vertices = new HashMap<>();
        scalePane = new AutoScalePane();
        elements = moduleGroup.getElements();
        relations = moduleGroup.getRelations();

        createElements();
        createRelations();

        return scalePane;
    }


    private void createElements() {
        elements.forEach(e -> {
            String label = "";
            if (e.getContent() != null) {
                label = e.getContent().toString();
            }
            ADVStyle style = e.getStyle();
            if (style == null) {
                style = new ADVDefaultElementStyle();
            }
            LabeledNode vertex = new LabeledNode(label, style, true);

            if (e.getFixedPosX() != 0 || e.getFixedPosY() != 0) {
                vertex.setX(e.getFixedPosX());
                vertex.setY(e.getFixedPosY());
            }
            vertices.put(e.getId(), vertex);
            scalePane.addChildren(vertex);
        });
    }

    private void createRelations() {
        Set<Integer> edgeHashes = new HashSet<>();

        relations.forEach(r -> {
            LabeledNode source = vertices.get(r.getSourceElementId());
            LabeledNode target = vertices.get(r.getTargetElementId());


            ADVStyle style = r.getStyle();
            if (style == null) {
                style = new ADVDefaultRelationStyle();
            }
            LabeledEdge.DirectionType type = LabeledEdge.DirectionType.NONE;
            if (r.isDirected()) {
                type = LabeledEdge.DirectionType.UNIDIRECTIONAL;
            }

            // does an edge already exist
            boolean combinationNotExists = edgeHashes.add(
                    source.hashCode() + target.hashCode());
            LabeledEdge edge;
            String label = "";
            if (r.getLabel() != null) {
                label = r.getLabel().toString();
            }
            if (combinationNotExists) {
                if (source.equals(target)) {
                    edge = new SelfReferenceEdge(label, source, ConnectorType
                            .RIGHT, ConnectorType.BOTTOM, style, type);
                } else {
                    edge = new LabeledEdge(
                            label,
                            source, ConnectorType.DIRECT,
                            target, ConnectorType.DIRECT,
                            style, type);
                }
            } else {
                if (source.equals(target)) {
                    edge = new SelfReferenceEdge(label, source, ConnectorType
                            .LEFT, ConnectorType.TOP, style, type);
                } else {
                    edge = new CurvedLabeledEdge(
                            label,
                            source, ConnectorType.DIRECT,
                            target, ConnectorType.DIRECT,
                            style, type);
                }
            }

            scalePane.addChildren(edge);
        });
    }


}
