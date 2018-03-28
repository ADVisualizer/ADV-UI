package ch.adv.ui.presentation;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.inject.Singleton;

/**
 * Controller for the replay of the snapshot navigation
 */
@Singleton
public class ReplayController {
    private static final double SLIDER_MIN = 1.0;
    private static final double SLIDER_MAX = 3.0;

    private DoubleProperty replaySpeed;

    public ReplayController() {
        replaySpeed = new SimpleDoubleProperty();
    }

    public DoubleProperty getReplaySpeedProperty() {
        return replaySpeed;
    }

    /**
     * @return the currently selected replay speed as a relative value [1..3]
     */
    public long getReplaySpeed() {
        double speed = SLIDER_MIN + SLIDER_MAX - replaySpeed.get();
        return (long) speed;
    }

}
