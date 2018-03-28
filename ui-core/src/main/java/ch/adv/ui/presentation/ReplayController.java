package ch.adv.ui.presentation;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.inject.Singleton;

/**
 * Controller for the replay of the snapshot navigation
 */
@Singleton
public class ReplayController {

    private DoubleProperty replaySpeed;

    public ReplayController() {
        replaySpeed = new SimpleDoubleProperty();
    }

    public DoubleProperty getReplaySpeed() {
        return replaySpeed;
    }
}
