package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.commons.core.logic.domain.ModuleGroup;
import javafx.scene.layout.Pane;

import java.util.List;

/**
 * Builds JavaFX Elements from business objects contained in a module group.
 * Places and orients these elements on a JavaFX Pane.
 * <p>
 * Abstraction Interface of the strategy pattern. Every Module supplies a
 * concrete strategy to be used.
 *
 * @author mtrentini
 */
public interface Layouter {

    /**
     * Layouts the module group. Builds necessary JavaFX Elements and places
     * them on a pane, which will be displayed in the session view.
     *
     * @param moduleGroup to be layouted
     * @param flags       optional flags to influence the layout
     * @return a pane containing the module group's javafx representation
     */
    Pane layout(ModuleGroup moduleGroup, List<String> flags);
}
