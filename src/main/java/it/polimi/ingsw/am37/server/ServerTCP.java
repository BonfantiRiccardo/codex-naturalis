package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.common.MessageDecoder;
import it.polimi.ingsw.am37.common.messages.Message;
import it.polimi.ingsw.am37.common.messages.MessageId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    private final int port;
    private final MessageDecoder decoder;
    private final HashMap<Socket, ObjectOutputStream> map;

    public ServerTCP(int port) {
        this.port = port;
        decoder = new MessageDecoder(this);
        map = new HashMap<>();
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
                System.out.println("Connection established");
                executor.submit(() -> {
                    try {
                        ObjectInputStream in = null;
                        ObjectOutputStream out = null;
                        try {
                            in = new ObjectInputStream(socket.getInputStream());
                            out = new ObjectOutputStream(socket.getOutputStream());
                            map.put(socket, out);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }

                        System.out.println("Waiting...");
                        while (true) {
                            Message message = null;
                            try {
                                assert in != null;
                                message = (Message) in.readObject();
                                System.out.println(message);
                            } catch (IOException | ClassNotFoundException e) {
                                System.out.println(e.getMessage());
                            }
                            assert message != null;
                            if (message.getId().equals(MessageId.TERMINATE))
                                break;
                            else
                                decoder.decodeAndExecute(socket, message);
                        }

                        in.close();
                        assert out != null;
                        out.close();
                        socket.close();
                    } catch (final IOException e) {
                        System.err.println(e.getMessage());
                    }
                });
            } catch (final IOException e) {
                break;
            }
        }
        executor.shutdown();
    }

    public MessageDecoder getDecoder() {
        return decoder;
    }

    public int getPort() {
        return port;
    }

    public void send(Socket s, Message m) {
        assert map.get(s) != null;

        try {
            map.get(s).writeObject(m);
            map.get(s).flush();

            //map.get(s).close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
