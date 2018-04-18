package ch.adv.ui.core.access;

import com.google.inject.Singleton;
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
@Singleton
public class FileDatastoreAccess implements DatastoreAccess {

    private static final Logger logger = LoggerFactory.getLogger(
            FileDatastoreAccess.class);

    /**
     * Reads the given file if it exists, using the jvm default charset.
     *
     * @param file file to read
     * @return null or json payload
     * @throws IOException exception
     */
    @Override
    public String read(final File file) throws IOException {
        if (file.exists()) {
            Path path = Paths.get(file.getAbsolutePath());
            try (BufferedReader reader = Files.newBufferedReader(path,
                    Charset.defaultCharset())) {
                String jsonPayload = "";
                String jsonLine;
                while ((jsonLine = reader.readLine()) != null) {
                    jsonPayload += jsonLine;
                }
                return jsonPayload;
            } catch (IOException e) {
                logger.info("Unable to read file {}",
                        file.getAbsoluteFile(), e);
                throw e;
            }
        } else {
            logger.info("Unable to read file: File {} not found.",
                    file.getAbsoluteFile());
            return null;
        }
    }

    /**
     * Writes the given json payload to the given file, using
     * the jvm default charset.
     *
     * @param file        new or existing file
     * @param jsonPayload data
     * @throws IOException exception
     */
    @Override
    public void write(final File file, String jsonPayload) throws IOException {
        Path path = Paths.get(file.getAbsolutePath());

        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset
                .defaultCharset())) {
            writer.write(jsonPayload);
        } catch (IOException e) {
            logger.info("Unable to write file {}", file.getAbsoluteFile(), e);
            throw e;
        }
    }
}
