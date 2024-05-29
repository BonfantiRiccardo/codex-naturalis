package it.polimi.ingsw.am37.network.client;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.network.messages.PingToServer;
import it.polimi.ingsw.am37.view.ViewState;
import it.polimi.ingsw.am37.view.virtualserver.TCPVirtualServer;
import it.polimi.ingsw.am37.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Timer;
import java.util.TimerTask;

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
     * the disconnectionTimer attribute is the timer that handles the disconnection of the client.
     */
    private Timer disconnectionTimer;

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

        disconnectionTimer = new Timer();
    }

    /**
     * the startClient method implements the usage of the method from the ClientConnectionInterface.
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
            //Thread to handle pinging to server
            new Thread(() -> startPingingServer(socketOut)).start();

            //Thread to handle server messages
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
     * the handleServer method handles all the messages that the server sends.
     * @param socketIn is the object input stream of the socket.
     */
    private void handleServer(ObjectInputStream socketIn) {
        MessageToClient message;
        try {
            while ( (message = (MessageToClient) socketIn.readObject()) != null) {
                message.decodeAndExecute(v);

                handleTimers();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("\nConnection with server was lost, closing application.\n");
            System.exit(0);
        }
    }


    private void startPingingServer(final ObjectOutputStream socketOut) {
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Ping thread interrupted");
            }
            synchronized (socketOut) {
                try {
                    socketOut.writeObject(new PingToServer(MessageId.PING));
                    socketOut.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void handleTimers() {
        disconnectionTimer.cancel();
        disconnectionTimer = new Timer();
        disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                v.setState(ViewState.DISCONNECTION);
                System.out.println("\nConnection with server was lost, closing application.");
                System.exit(0);
            }
        }, 15000);
    }

}
