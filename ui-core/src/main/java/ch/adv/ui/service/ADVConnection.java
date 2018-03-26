package ch.adv.ui.service;

import ch.adv.ui.access.GsonProvider;
import com.google.inject.assistedinject.Assisted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ADVConnection {

    private static final Logger logger = LoggerFactory.getLogger(ADVConnection
            .class);

    private final Socket socket;
    private final GsonProvider gsonProvider;
    private final ADVFlowControl flowControl;
    private BufferedReader reader;
    private PrintWriter writer;

    @Inject
    public ADVConnection(ADVFlowControl flowControl, GsonProvider gsonProvider,
                         @Assisted Socket socket) {
        this.socket = socket;
        this.flowControl = flowControl;
        this.gsonProvider = gsonProvider;
    }

    public void process() throws IOException {
        initializeStreams();
        readData();
    }

    private void initializeStreams() throws IOException {
        reader = new BufferedReader(new
                InputStreamReader(socket.getInputStream(),
                StandardCharsets.UTF_8));
        writer = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), StandardCharsets.UTF_8),
                true);
    }

    private void readData() throws IOException {
        String sessionJSON;
        while ((sessionJSON = reader.readLine()) != null) {

            logger.debug("Parse incoming request");
            ADVRequest request = gsonProvider.getMinifier().fromJson
                    (sessionJSON, ADVRequest.class);

            if (request.getCommand().equals(ProtocolCommand.END)) {
                logger.info("End of session transmission");
                break;
            }

            logger.debug("Acknowledge received json");
            ADVResponse response = new ADVResponse(ProtocolCommand
                    .ACKNOWLEDGE);
            writer.println(response.toJson());

            try {
                logger.debug("Process json: \n {}", sessionJSON);
                flowControl.process(request.getJson());
            } catch (Exception e) {
                logger.error("Exception occurred during execution of "
                        + "ADV UI. Send exception to Lib", e);

                ADVResponse exceptionResponse = new ADVResponse
                        (ProtocolCommand.EXCEPTION, getStacktraceString(e));
                writer.println(exceptionResponse.toJson());
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
