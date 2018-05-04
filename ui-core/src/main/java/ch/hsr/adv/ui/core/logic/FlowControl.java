package ch.hsr.adv.ui.core.logic;

/**
 * This class is responsible for processing and dispatching an incomming
 * request to the correct modules.
 *
 * @author mwieland
 */
public interface FlowControl {

    /**
     * Processes the incoming json
     *
     * @param sessionJSON json
     */
    void process(String sessionJSON);
}
