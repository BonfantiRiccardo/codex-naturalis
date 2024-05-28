package it.polimi.ingsw.am37.network.client;

import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.virtualserver.TCPVirtualServer;
import it.polimi.ingsw.am37.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.NoSuchElementException;

/**
 * the ClientTCP class implements the ClientConnectionInterface and handles all the messages sent by the player.
 */
public class ClientTCP implements ClientConnectionInterface{
    /**
     * the ip attribute is the ip of the server connecting to.
     */
    private final String ip;
    /**
     * the port attribute is the port of the server connecting to.
     */
    private final int port;
    /**
     * the v attribute is the view connecting to.
     */
    private final View v;

    /**
     * ClientTCP is a setter method for the server's port and ip, and the for the view.
     * @param ip is the server's ip.
     * @param port is the server's port.
     * @param v is the view.
     */
    public ClientTCP(String ip, int port, View v) {
        this.ip = ip;
        this.port = port;
        this.v = v;
    }

    /**
     * the startClient method is called by the ClientMain to connect the Client and the view.
     * @throws IOException when the input is wrong.
     */
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

    /**
     * the handleServer method handles all the messages that the server sent.
     * @param socketIn is the object input stream of the socket.
     */
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
