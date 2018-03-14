package ch.adv.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listens for incoming snapshot transmissions and routes it to the correct module parser.
 *
 * @author mtrentini
 */
public class SocketServer extends Thread {
    private static ServerSocket javaSocket;

    private static final String THREAD_NAME = "SocketServer Thread";
    private static final int DEFAULT_PORT = 8765;
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public SocketServer() {
        super(THREAD_NAME);
    }

    /**
     * Accepts socket connections, acknowledges incoming snapshots and routes the data to the corresponding parser.
     * Receiving the 'END' tag results in accepting new socket connections.
     */
    @Override
    public void run() {
        try {
            javaSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            logger.error("Could not initialize java.net.ServerSocket", e);
        }
        while (true) {
            try {
                Socket socket = javaSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                String snapshot;
                while ((snapshot = reader.readLine()) != null) {
                    if (snapshot.equals("END")) {
                        logger.info("End of session transmission");
                        return;
                    }
                    logger.debug("Snapshot received: " + snapshot);
                    //TODO: route snapshot data to right module
                    writer.println("OK");
                }
            } catch (IOException e) {
                logger.error("Unable to read incoming transmissions", e);
            }
        }
    }

}
