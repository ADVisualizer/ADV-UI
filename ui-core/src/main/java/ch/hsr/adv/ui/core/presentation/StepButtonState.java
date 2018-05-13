package ch.hsr.adv.ui.core.presentation;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Helper class to hold button state for {@link StateViewModel}
 */
class StepButtonState {

    private final BooleanProperty stepFirstBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty stepBackwardBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty stepForwardBtnDisableProperty = new
            SimpleBooleanProperty();
    private final BooleanProperty stepLastBtnDisableProperty = new
            SimpleBooleanProperty();

    /**
     * updates step button properties. Disables buttons if argument is true.
     *
     * @param first    first button property
     * @param backward backward button property
     * @param forward  forward button property
     * @param last     last button property
     */
    void disableStepButtons(boolean first, boolean backward, boolean
            forward, boolean last) {
        stepFirstBtnDisableProperty.set(first);
        stepBackwardBtnDisableProperty.set(backward);
        stepForwardBtnDisableProperty.set(forward);
        stepLastBtnDisableProperty.set(last);
    }

    BooleanProperty getStepFirstBtnDisableProperty() {
        return stepFirstBtnDisableProperty;
    }

    BooleanProperty getStepBackwardBtnDisableProperty() {
        return stepBackwardBtnDisableProperty;
    }

    BooleanProperty getStepForwardBtnDisableProperty() {
        return stepForwardBtnDisableProperty;
    }

    BooleanProperty getStepLastBtnDisableProperty() {
        return stepLastBtnDisableProperty;
    }
}