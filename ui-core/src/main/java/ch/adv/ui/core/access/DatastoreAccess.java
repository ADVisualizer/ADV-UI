package ch.adv.ui.core.access;

import java.io.File;
import java.io.IOException;

/**
 * Datastore interface to store sessions
 */
public interface DatastoreAccess {

    /**
     * Read the file from the datastore.
     *
     * @param file to be read
     * @return a string representation of the red file
     * @throws IOException exception
     */
    String read(File file) throws IOException;

    /**
     * Write a json string to a new or existing file.
     *
     * @param file to be written
     * @param json to be saved
     * @throws IOException exception
     */
    void write(File file, String json) throws IOException;
}
