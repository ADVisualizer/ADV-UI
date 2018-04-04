package ch.adv.ui.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Listens for incoming snapshot transmissions and routes it to the correct
 * module parser.
 *
 * @author mtrentini
 */
@Singleton
public class SocketServer extends Thread {

    private static final int DEFAULT_PORT = 8765;
    private static final String THREAD_NAME = "SocketServer Thread";
    private static final Logger logger = LoggerFactory.getLogger(SocketServer
            .class);
    private final ADVConnectionFactory connectionFactory;
    private ServerSocket javaSocket;
    private int portNr;

    @Inject
    public SocketServer(ADVConnectionFactory connectionFactory) {
        super(THREAD_NAME);
        this.connectionFactory = connectionFactory;
        portNr = DEFAULT_PORT;
    }

    /**
     * Accepts socket connections, acknowledges incoming snapshots and routes
     * the data to the corresponding parser.
     * Receiving the 'END' tag results in accepting new socket connections.
     */
    @Override
    public void run() {

        try {
            javaSocket = new ServerSocket(portNr);
            logger.info("Socket server started on port {}", portNr);
        } catch (IOException e) {
            logger.error("Unable to start socket server on port {}", portNr, e);
        }

        while (true) {
            try {
                Socket socket = javaSocket.accept();
                ADVConnection connection = connectionFactory.create(socket);
                connection.process();
            } catch (IOException e) {
                logger.error("Unable to accept socket connection", e);
            }
        }
    }

    /**
     * Set alternative port on which SocketServer should listen.
     *
     * @param port the port number to listen on
     */
    public void setPort(int port) {
        if (portNr >= 1024 && portNr <= 65535) {
            portNr = port;
        } else {
            portNr = DEFAULT_PORT;
        }
    }
}
