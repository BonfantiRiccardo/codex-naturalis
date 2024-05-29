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

public class ClientHandler implements ClientInterface{
    private final Socket socket;
    private final MultipleMatchesHandler multipleMatchesHandler;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private final Lock lockOutput;

    private Timer disconnectionTimer;
    private Thread pingThread;
    private boolean disconnected = false;

    public ClientHandler(Socket socket, MultipleMatchesHandler multipleMatchesHandler) {
        this.socket = socket;
        this.multipleMatchesHandler = multipleMatchesHandler;

        lockOutput = new ReentrantLock();

        disconnectionTimer = new Timer();
    }

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

    public void disconnectAndCloseGame() {
        //CONNECTION CLOSED WITH THE CLIENT, NOTIFY ALL THE OTHER CLIENTS CONNECTED TO THIS GAME CONTROLLER
        //THAT THE GAME IS OVER
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

    public MultipleMatchesHandler getMultipleMatchesHandler() {
        return multipleMatchesHandler;
    }
}
