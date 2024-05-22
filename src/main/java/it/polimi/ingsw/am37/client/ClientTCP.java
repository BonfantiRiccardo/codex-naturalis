package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.messages.*;
import it.polimi.ingsw.am37.view.TCPVirtualServer;
import it.polimi.ingsw.am37.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;

public class ClientTCP implements ClientConnectionInterface{
    private final String ip;
    private final int port;
    private final View v;

    public ClientTCP(String ip, int port, View v) {
        this.ip = ip;
        this.port = port;
        this.v = v;
    }

    public void startClient() throws IOException {
        final Socket socket = new Socket(ip, port);
        System.out.println("Connection established on port: " + port);

        final ObjectOutputStream socketOut = new ObjectOutputStream(socket.getOutputStream());
        socketOut.flush();

        TCPVirtualServer vs = new TCPVirtualServer();
        vs.setOut(socketOut);
        v.setVirtualServer(vs);

        final ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());

        try {

            new Thread(() -> handleServer(socketIn)).start();

            while (true) {
                boolean choice = v.handleGame();

                if (!choice)
                    break;
            }
        } catch (final NoSuchElementException e) {
            System.out.println("Connection closed");
        } finally {
            System.out.println("Closing connection with server");
            socketIn.close();       //CLOSING WHILE ACTIVE THREAD IS READING CAUSES AN IO EXCEPTION
            socketOut.close();
            socket.close();
        }
    }

    private void handleServer(ObjectInputStream socketIn) {
        MessageToClient message;
        try {
            while ( (message = (MessageToClient) socketIn.readObject()) != null) {
                message.decodeAndExecute(v);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nConnection with server was lost, closing application.\n");
            System.exit(0);
        }
    }

}
