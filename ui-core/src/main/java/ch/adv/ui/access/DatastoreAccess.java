package ch.adv.ui.access;

import java.io.File;

/**
 * Datastore interface to store sessions
 */
public interface DatastoreAccess {

    /**
     * Read the file from the datastore.
     *
     * @param file to be read
     * @return a string representation of the red file
     */
    String read(File file);

    /**
     * Write a json string to a new or existing file.
     *
     * @param file to be written
     * @param json to be saved
     * @return true if the method executed successfully
     */
    boolean write(File file, String json);
}
