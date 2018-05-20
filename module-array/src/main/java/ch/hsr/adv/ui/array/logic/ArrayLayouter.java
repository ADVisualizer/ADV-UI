package ch.hsr.adv.ui.array.logic;

import ch.hsr.adv.commons.array.logic.ConstantsArray;
import ch.hsr.adv.commons.core.logic.domain.Module;
import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import ch.hsr.adv.ui.core.logic.Layouter;
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
@Module(ConstantsArray.MODULE_NAME)
public class ArrayLayouter implements Layouter {

    private static final String SHOW_OBJECT_RELATIONS = "SHOW_OBJECT_RELATIONS";
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
     * Layouts an Array module group if it is not already layouted
     *
     * @param moduleGroup to be layouted
     * @return layouted snapshot
     */
    @Override
    public Pane layout(ModuleGroup moduleGroup, List<String> flags) {

        boolean showObjectRelations = false;
        if (flags != null) {
            showObjectRelations = flags.stream()
                    .anyMatch(f -> f.equals(SHOW_OBJECT_RELATIONS));
        }

        if (showObjectRelations) {
            logger.info("Use Object Reference Array Layouter");
            return arrayObjectReferenceLayouter.layout(moduleGroup);
        } else {
            logger.info("Use Default Array Layouter");
            return arrayDefaultLayouter.layout(moduleGroup);
        }
    }
}
