package ch.hsr.adv.ui.core.access;

import java.io.File;
import java.io.IOException;

/**
 * Persists sessions in a data store.
 */
public interface DatastoreAccess {

    /**
     * Read the file from the data store.
     *
     * @param file to be read
     * @return a string representation of the read file
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
