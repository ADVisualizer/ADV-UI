package ch.adv.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {

    private static Socket socket;
    private static BufferedReader reader;
    private static PrintWriter writer;
    private static ServerSocket javaSocket;

    private static final String THREAD_NAME = "SocketServer Thread";
    private static final int DEFAULT_PORT = 8765;
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    public SocketServer() {
        super(THREAD_NAME);
        try {
            javaSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = javaSocket.accept();
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
                String data;
                while ((data = reader.readLine()) != null) {
                    if (data.equals("END")) {
                        logger.info("End of transmission");
                        return;
                    }
                    logger.info("Data received: " + data);
                    writer.println("OK");
                }
            } catch (IOException e) {
                logger.error("Unable to read incomming transmissions");
            }
        }
    }

}
