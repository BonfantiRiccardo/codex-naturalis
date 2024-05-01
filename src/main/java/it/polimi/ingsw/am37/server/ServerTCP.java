package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    private final int port;
    private final MultipleMatchesHandler multipleMatchesHandler;

    public ServerTCP(int port) {
        this.port = port;
        multipleMatchesHandler = new MultipleMatchesHandler();
    }

    public void startServer() {
        final ExecutorService executor = Executors.newFixedThreadPool(10);

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

    public int getPort() {
        return port;
    }

}
