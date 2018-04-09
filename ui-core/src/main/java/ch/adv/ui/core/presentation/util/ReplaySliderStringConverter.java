package ch.adv.ui.core.presentation.util;

import ch.adv.ui.core.presentation.I18n;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.StringConverter;

import javax.inject.Singleton;

/**
 * String converter for the tick labels of the speed slider.
 *
 * @author mwieland
 */
@Singleton
public class ReplaySliderStringConverter extends StringConverter<Double> {

    private static final double SLIDER_SLOW = 1.0;
    private static final double SLIDER_MEDIUM = 2.0;
    private static final double SLIDER_FAST = 3.0;

    private final StringProperty slow = new SimpleStringProperty();
    private final StringProperty medium = new SimpleStringProperty();
    private final StringProperty fast = new SimpleStringProperty();

    public ReplaySliderStringConverter() {
        slow.bind(I18n.createStringBinding("scale.slow"));
        medium.bind(I18n.createStringBinding("scale.medium"));
        fast.bind(I18n.createStringBinding("scale.fast"));
    }

    @Override
    public String toString(Double number) {
        if (number < SLIDER_MEDIUM) {
            return slow.get();
        } else if (number < SLIDER_FAST) {
            return medium.get();
        } else {
            return fast.get();
        }
    }

    @Override
    public Double fromString(String s) {
        if (s.equals(I18n.get("scale.slow"))) {
            return SLIDER_SLOW;
        } else if (s.equals(I18n.get("scale.fast"))) {
            return SLIDER_FAST;
        } else {
            return SLIDER_MEDIUM;
        }
    }
}
