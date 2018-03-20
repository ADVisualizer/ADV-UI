package ch.adv.ui;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import javax.inject.Singleton;

@Singleton
public class ReplayController {

    private DoubleProperty replaySpeed;

    public  ReplayController() {
        replaySpeed = new SimpleDoubleProperty();
    }

    public DoubleProperty getReplaySpeed() {
        return replaySpeed;
    }
}
