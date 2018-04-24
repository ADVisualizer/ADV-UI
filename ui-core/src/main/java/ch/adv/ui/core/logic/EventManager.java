package ch.adv.ui.core.logic;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface EventManager {

    void subscribe(PropertyChangeListener listener,
                   ADVEvent event,
                   String... filterArgs);

    void subscribe(PropertyChangeListener listener,
                   List<ADVEvent> events,
                   String... filterArgs);

    void unsubscribe(PropertyChangeListener listener,
                     ADVEvent event,
                     String... filterArgs);

    void unsubscribe(PropertyChangeListener listener,
                     List<ADVEvent> events,
                     String... filterArgs);

    void fire(ADVEvent event, Object oldVal, Object newVal,
         String... filterArgs);

}
