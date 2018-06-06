package ch.hsr.adv.ui.core.presentation;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.inject.Singleton;

/**
 * Controller for the replay of the snapshot navigation
 *
 * @author mtrentini
 */
@Singleton
class ReplayController {
    private static final double SLIDER_MIN = 1.0;
    private static final double SLIDER_MAX = 3.0;

    private DoubleProperty replaySpeed;

    ReplayController() {
        replaySpeed = new SimpleDoubleProperty();
    }

    DoubleProperty getReplaySpeedProperty() {
        return replaySpeed;
    }

    /**
     * @return the currently selected replay speed as a relative value [1..3]
     */
    long getReplaySpeed() {
        double speed = SLIDER_MIN + SLIDER_MAX - replaySpeed.get();
        return (long) speed;
    }

}
