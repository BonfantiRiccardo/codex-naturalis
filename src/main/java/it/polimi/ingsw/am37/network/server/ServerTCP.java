package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * This class is used to launch the server.
 */
public class ServerTCP {
    /**
     * The port on which the server is listening.
     */
    private final int port;
    /**
     * The handler that is used to manage multiple matches.
     */
    private final MultipleMatchesHandler multipleMatchesHandler;

    /**
     * This constructor is used to create a new server.
     * @param port The port on which the server is listening.
     * @param multipleMatchesHandler The handler that is used to manage multiple matches.
     */
    public ServerTCP(int port, MultipleMatchesHandler multipleMatchesHandler) {
        this.port = port;
        this.multipleMatchesHandler = multipleMatchesHandler;
    }

    /**
     * This method is used to start the server.
     * It listens for incoming connections and creates a new thread for each connection.
     * Each thread is used to handle the connection with the client.
     * The handler that is used to manage the connection is the ClientHandler.
     * The ClientHandler is created with the socket and the multipleMatchesHandler.
     * The ClientHandler is then used to handle the connection.
     * The server is shut down when an exception is thrown.
     */
    public void startServer() {
        final ExecutorService executor = Executors.newCachedThreadPool();

        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
        } catch (final IOException e) {
            System.err.println(e.getMessage());
            return;
        }
        System.out.println("Server ready, listening on port: " + port);

        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                System.out.println("Connection established with client: " + socket.getLocalAddress());

                executor.submit(() -> {
                    ClientHandler ch = new ClientHandler(socket, multipleMatchesHandler);

                    ch.handle();
                });
            } catch (final IOException e) {
                break;
            }
        }
        executor.shutdown();
    }
}
