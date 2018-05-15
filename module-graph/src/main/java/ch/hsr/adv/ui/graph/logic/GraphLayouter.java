package ch.hsr.adv.ui.graph.logic;


import ch.hsr.adv.ui.core.logic.Layouter;
import ch.hsr.adv.ui.core.logic.domain.ADVElement;
import ch.hsr.adv.ui.core.logic.domain.ADVRelation;
import ch.hsr.adv.ui.core.logic.domain.Module;
import ch.hsr.adv.ui.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.domain.styles.ADVStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.presets.ADVDefaultLineStyle;
import ch.hsr.adv.ui.core.logic.domain.styles.presets.ADVDefaultStyle;
import ch.hsr.adv.ui.core.presentation.widgets.*;
import ch.hsr.adv.ui.graph.logic.domain.ModuleConstants;
import com.google.inject.Singleton;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * Creates JavaFX Nodes for the graph elements and adds them to a pane
 */
@Singleton
@Module(ModuleConstants.MODULE_NAME)
public class GraphLayouter implements Layouter {

    private static final Logger logger = LoggerFactory.getLogger(
            GraphLayouter.class);

    private Map<Long, LabeledNode> vertices;
    private AutoScalePane scalePane;
    private List<ADVRelation> relations;
    private List<ADVElement> elements;

    /**
     * Layout the graph module group
     *
     * @param moduleGroup to be layouted
     * @param flags       optional flags on session level
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
            String label = e.getContent().toString();
            ADVStyle style = e.getStyle();
            if (style == null) {
                style = new ADVDefaultStyle();
            }
            LabeledNode vertex = new LabeledNode(label, style, true);

            if (e.getFixedPosX() != 0 || e.getFixedPosY() != 0) {
                vertex.setX(e.getFixedPosX());
                vertex.setY(e.getFixedPosY());
            } //TODO: else use layouting algorithm
            vertices.put(e.getElementId(), vertex);
            scalePane.addChildren(vertex);
        });
    }

    //TODO: change ConnectorType if more than one edge between two vertices
    private void createRelations() {
        Set<Integer> edgeHashes = new HashSet<>();

        relations.forEach(r -> {
            LabeledNode source = vertices.get(r.getSourceElementId());
            LabeledNode target = vertices.get(r.getTargetElementId());
            boolean combinationNotExists = edgeHashes.add(
                    source.hashCode() + target.hashCode());

            ADVStyle style = r.getStyle();
            if (style == null) {
                style = new ADVDefaultLineStyle();
            }
            LabeledEdge.DirectionType type = LabeledEdge.DirectionType.NONE;
            if (r.isDirected()) {
                type = LabeledEdge.DirectionType.UNIDIRECTIONAL;
            }

            // does an edge already exists
            LabeledEdge edge;
            if (combinationNotExists) {
                edge = new LabeledEdge(
                        r.getLabel().toString(),
                        source, ConnectorType.DIRECT,
                        target, ConnectorType.DIRECT,
                        style, type);
            } else {
                edge = new CurvedLabeledEdge(
                        r.getLabel().toString(),
                        source, ConnectorType.DIRECT,
                        target, ConnectorType.DIRECT,
                        style, type);
            }

            scalePane.addChildren(edge);
        });
    }


}
