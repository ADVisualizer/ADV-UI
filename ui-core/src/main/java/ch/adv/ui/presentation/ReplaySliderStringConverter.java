package ch.adv.ui.presentation;

import javafx.util.StringConverter;

import javax.inject.Singleton;

/**
 * String converter for the tick labels of the speed slider.
 *
 * @author mwieland
 */
@Singleton
public class ReplaySliderStringConverter extends StringConverter<Double> {

    private static final double SLIDER_SLOW = 0.0;
    private static final double SLIDER_MEDIUM = 1.0;
    private static final double SLIDER_FAST = 2.0;

    private static final String SLOW = "slow";
    private static final String MEDIUM = "medium";
    private static final String FAST = "fast";

    @Override
    public String toString(Double number) {
        if (number < SLIDER_MEDIUM) {
            return SLOW;
        } else if (number < SLIDER_FAST) {
            return MEDIUM;
        } else {
            return FAST;
        }
    }

    @Override
    public Double fromString(String s) {
        switch (s) {
            case SLOW:
                return SLIDER_SLOW;
            case MEDIUM:
                return SLIDER_MEDIUM;
            case FAST:
                return SLIDER_FAST;
            default:
                return SLIDER_MEDIUM;
        }
    }
}
