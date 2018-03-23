package ch.adv.ui.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Stores and reads JSON Files on the filesystem
 */
public class FileDatastore implements Datastore {

    private static final Logger logger = LoggerFactory.getLogger(FileDatastore
            .class);

    @Override
    public String read(File file) {

        String jsonPayload = new String();
        Path path = Paths.get(file.getAbsolutePath());
        try (BufferedReader reader = Files.newBufferedReader(path, Charset.defaultCharset() )) {
            String jsonLine;
            while ((jsonLine = reader.readLine()) != null ) {
                jsonPayload += jsonLine;
            }
        } catch (IOException e) {
            logger.info("Unable to read file {}", file.getAbsoluteFile(), e);
        }

        return jsonPayload;
    }

    @Override
    public boolean write(File file, String jsonPayload) {
        Path path = Paths.get(file.getAbsolutePath());

        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.defaultCharset())) {
            writer.write(jsonPayload);
            return true;
        } catch (IOException e) {
            logger.info("Unable to write file {}", file.getAbsoluteFile(), e);
            return false;
        }
    }
}
