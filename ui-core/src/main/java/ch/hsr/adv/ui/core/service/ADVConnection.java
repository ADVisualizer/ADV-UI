package ch.hsr.adv.ui.core.service;

import ch.hsr.adv.commons.core.access.ADVRequest;
import ch.hsr.adv.commons.core.access.ADVResponse;
import ch.hsr.adv.commons.core.access.ProtocolCommand;
import ch.hsr.adv.ui.core.logic.FlowControl;
import ch.hsr.adv.ui.core.logic.GsonProvider;
import com.google.gson.Gson;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * An incoming connection from the ADV Lib
 *
 * @author mwieland
 */
public class ADVConnection {

    private static final Logger logger = LoggerFactory
            .getLogger(ADVConnection.class);

    private final Socket socket;
    private final Gson gson;
    private final FlowControl flowControl;
    private BufferedReader reader;
    private PrintWriter writer;

    @Inject
    public ADVConnection(FlowControl flowControl, GsonProvider gsonProvider,
                         @Assisted Socket socket) {
        this.socket = socket;
        this.flowControl = flowControl;
        this.gson = gsonProvider.getMinifier().create();
    }

    /**
     * Processes the incoming connection
     *
     * @throws IOException r/w exception
     */
    void process() throws IOException {
        initializeStreams();
        readData();
    }

    private void initializeStreams() throws IOException {
        reader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(),
                        StandardCharsets.UTF_8));
        writer = new PrintWriter(
                new OutputStreamWriter(socket.getOutputStream(),
                        StandardCharsets.UTF_8),
                true);
    }

    private void readData() throws IOException {
        String sessionJSON;
        while ((sessionJSON = reader.readLine()) != null) {

            try {
                logger.debug("Parse incoming request");
                ADVRequest request = gson
                        .fromJson(sessionJSON, ADVRequest.class);

                if (request.getCommand().equals(ProtocolCommand.END)) {
                    logger.info("End of session transmission");
                    break;
                }


                logger.debug("Acknowledge received json");
                ADVResponse response = new ADVResponse(ProtocolCommand
                        .ACKNOWLEDGE);
                String json = gson.toJson(response);
                writer.println(json);

                logger.debug("Process json: \n {}", request.getJson());
                flowControl.load(request.getJson());
            } catch (Exception e) {
                logger.error("Exception occurred during execution of "
                        + "ADV UI. Send exception to Lib", e);

                ADVResponse exceptionResponse = new ADVResponse(
                        ProtocolCommand.EXCEPTION, getStacktraceString(e));
                String json = gson.toJson(exceptionResponse);

                writer.println(json);
            }
        }
    }

    private String getStacktraceString(Exception exception) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
