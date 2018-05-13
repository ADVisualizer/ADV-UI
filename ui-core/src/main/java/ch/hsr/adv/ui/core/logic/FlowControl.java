package ch.hsr.adv.ui.core.logic;

import ch.hsr.adv.ui.core.logic.domain.Session;

import java.io.File;

/**
 * FlowControl manages the process flow, of each incoming request. It is
 * the primary access point for all requests and responsible for dispatching a
 * request to the correct modules.
 *
 * @author mwieland
 */
public interface FlowControl {

    /**
     * Loads the incoming json
     *
     * @param sessionJSON json
     */
    void load(String sessionJSON);

    /**
     * Saves the loaded json
     *
     * @param session the loaded session
     * @param file    the file to save the session
     */
    void save(Session session, File file);
}
