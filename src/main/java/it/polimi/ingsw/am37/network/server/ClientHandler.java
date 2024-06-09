package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.messages.*;
import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;
import it.polimi.ingsw.am37.model.player.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * This class is used to handle the connection with the client.
 * It implements the ClientInterface and the Handler is used to send messages to the client.
 * It also handles the disconnection of the client and the ping messages.
 */
public class ClientHandler implements ClientInterface{
    /**
     * The socket used to communicate with the client.
     */
    private final Socket socket;
    /**
     * The MultipleMatchesHandler used to handle the multiple matches.
     */
    private final MultipleMatchesHandler multipleMatchesHandler;
    /**
     * The ObjectInputStream used to read messages from the client.
     */
    private ObjectInputStream in = null;
    /**
     * The ObjectOutputStream used to send messages to the client.
     */
    private ObjectOutputStream out = null;
    /**
     * The lock used to synchronize the output stream.
     */
    private final Lock lockOutput;
    /**
     * The Timer used to handle the disconnection of the client.
     */

    private Timer disconnectionTimer;
    /**
     * The Thread used to ping the client.
     */
    private Thread pingThread;
    /**
     * The boolean used to check if the client is disconnected.
     */
    private boolean disconnected = false;

    /**
     * The constructor of the class.
     * @param socket The socket used to communicate with the client.
     * @param multipleMatchesHandler The MultipleMatchesHandler used to handle the multiple matches.
     */
    public ClientHandler(Socket socket, MultipleMatchesHandler multipleMatchesHandler) {
        this.socket = socket;
        this.multipleMatchesHandler = multipleMatchesHandler;

        lockOutput = new ReentrantLock();

        disconnectionTimer = new Timer();
    }

    /**
     * This method is used to handle the connection with the client.
     * It reads the messages from the client and sends them to the MultipleMatchesHandler.
     * It also sends the messages to the client.
     * If the client is disconnected, it closes the connection with the client.
     * If the client is disconnected, it notifies all the other clients connected to the game controller that the game is over.
     */
    public void handle() {
        try {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("Waiting for client input...");

            //START PINGING CLIENT
            pingThread = new Thread(this::startPingingClient);
            pingThread.start();


            //READ MESSAGES FROM CLIENT
            MessageToServer message;
            try {
                assert in != null;
                while ( (message = (MessageToServer) in.readObject()) != null ) {
                    if (!message.getId().equals( MessageId.PING)) {
                        System.out.println(message);
                    }


                    if (message.getId().equals(MessageId.TERMINATE))
                        break;
                    else
                        multipleMatchesHandler.handle(this, message);

                    handleTimers();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }

            System.out.println("Closing connection with client");
            disconnectAndCloseGame();

            in.close();
            out.close();
            socket.close();
        } catch (final IOException e) {
            System.out.println(e.getMessage() + " warning CLOSING CLIENT HANDLE");
        }
    }

    /**
     * This method is used to send a message to the client.
     * @param m The message to send.
     */
    public void send(Message m) {
        assert out != null;

        lockOutput.lock();
        try {
            out.writeObject(m);
            out.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage() + " closing message sender");
        }
        lockOutput.unlock();
    }

    /**
     * This method is used to close the connection with the client.
     * It also notifies all the other clients connected to the game controller that the game is over.
     * It sets the boolean disconnected to true.
     * It cancels the disconnection timer.
     * It interrupts the ping thread.
     * It removes the client from the MultipleMatchesHandler.
     * It removes the game controller from the MultipleMatchesHandler.
     * It removes the lobby from the MultipleMatchesHandler.
     */
    public void disconnectAndCloseGame() {
        disconnected = true;
        disconnectionTimer.cancel();
        System.out.println("Disconnecting others from game");
        GameController c = multipleMatchesHandler.getMap().get(this);

        if (c != null) {
            if (multipleMatchesHandler.getLobbyList().containsKey(c.hashCode()))
                multipleMatchesHandler.removeLobby(c.hashCode());

            multipleMatchesHandler.removeClient(this);

            for (Player p: c.getAddedPlayers())
                c.getPlayerViews().get(p).playerDisconnection();
        }
    }

    /**
     * This method is used to close the connection with the client.
     * It sets the boolean disconnected to true.
     * It cancels the disconnection timer.
     * It interrupts the ping thread.
     * It removes the client from the MultipleMatchesHandler.
     * It removes the game controller from the MultipleMatchesHandler.
     * It removes the lobby from the MultipleMatchesHandler.
     * It closes the socket.
     */
    private void startPingingClient() {
        while (!disconnected) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Ping thread interrupted");
            }

            if (!disconnected) {
                lockOutput.lock();
                try {
                    out.writeObject(new PingToClient(MessageId.PING));
                    out.flush();
                } catch (IOException e) {
                    System.err.println("Error while pinging client");
                }
                lockOutput.unlock();
            }
        }
    }

    /**
     * This method is used to handle the disconnection timer.
     * It cancels the disconnection timer and creates a new one.
     * If the client does not send a ping to the server for 15 seconds, it is considered disconnected and
     * the method calls the disconnectAndCloseGame method.
     * It also interrupts the ping thread.
     */
    private void handleTimers() {
        disconnectionTimer.cancel();
        disconnectionTimer = new Timer();
        disconnectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("\nOne of the player did not send a ping to the server for 15 seconds, it is considered disconnected.");
                disconnectAndCloseGame();
                pingThread.interrupt();
            }
        }, 15000);
    }

    /**
     * This method is used to get the MultipleMatchesHandler.
     * @return The MultipleMatchesHandler.
     */
    public MultipleMatchesHandler getMultipleMatchesHandler() {
        return multipleMatchesHandler;
    }
}
