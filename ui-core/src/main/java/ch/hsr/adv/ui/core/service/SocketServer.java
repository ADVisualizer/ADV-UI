package ch.hsr.adv.ui.core.service;

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

    private static final Logger logger = LoggerFactory.getLogger(SocketServer
            .class);

    private static final int REGISTERED_PORT_MIN = 1024;
    private static final int REGISTERED_PORT_MAX = 65535;


    private static final String THREAD_NAME = "SocketServer Thread";
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 8765;

    private final ADVConnectionFactory connectionFactory;

    private ServerSocket javaSocket;
    private int portNr;
    private String host;

    @Inject
    public SocketServer(ADVConnectionFactory connectionFactory) {
        super(THREAD_NAME);
        this.connectionFactory = connectionFactory;
        portNr = DEFAULT_PORT;
        host = DEFAULT_HOST;
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
            logger.info("Socket server started on {}:{}", host, portNr);
        } catch (IOException e) {
            logger.error("Unable to start socket server on {}:{}", host,
                    portNr, e);
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
        if (portNr >= REGISTERED_PORT_MIN && portNr <= REGISTERED_PORT_MAX) {
            this.portNr = port;
            logger.info("Socket port updated to {}", portNr);
        }
    }

    /**
     * Set alternative host on which SocketServer should listen.
     *
     * @param host the host
     */
    public void setHost(String host) {
        if (host != null) {
            this.host = host;
            logger.info("Socket host updated to {}", host);
        }
    }
}
