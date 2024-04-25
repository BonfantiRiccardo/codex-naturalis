package it.polimi.ingsw.am37.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    private final int port;
    private final MessageDecoder decoder;

    public ServerTCP(int port) {
        this.port = port;
        decoder = new MessageDecoder();
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
        System.out.println("Server ready");
        while (true) {
            try {
                final Socket socket = serverSocket.accept();
                executor.submit(() -> {
                    try {
                        ObjectInputStream in = null;
                        ObjectOutputStream out = null;
                        try {
                            out = new ObjectOutputStream(socket.getOutputStream());
                            in = new ObjectInputStream(socket.getInputStream());
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }

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
                            if (message.getCreator().equals("quit")) {
                                break;
                            } else {
                                try {
                                    decoder.decodeAndExecute(message.getCreator(), message.getNum());
                                    out.writeObject(message);
                                    out.flush();
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                }
                            }

                            try {
                                message = (Message) in.readObject();
                            } catch (IOException | ClassNotFoundException e) {
                                System.out.println(e.getMessage());
                            }

                            assert message != null;
                            if (message.getCreator().equals("quit")) {
                                break;
                            } else {
                                try {
                                    decoder.add(message.getCreator());
                                    out.writeObject(message);
                                    out.flush();
                                } catch (IOException e) {
                                    System.out.println(e.getMessage());
                                }
                            }
                        }
                        in.close();
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
}
