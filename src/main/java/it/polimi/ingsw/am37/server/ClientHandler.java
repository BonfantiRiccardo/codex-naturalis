package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.messages.*;
import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;
import it.polimi.ingsw.am37.model.player.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements ClientInterface{
    private final Socket socket;
    private final MultipleMatchesHandler multipleMatchesHandler;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;

    public ClientHandler(Socket socket, MultipleMatchesHandler multipleMatchesHandler) {
        this.socket = socket;
        this.multipleMatchesHandler = multipleMatchesHandler;
    }

    public void handle() {
        try {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

            System.out.println("Waiting for client input...");
            MessageToServer message;

            try {
                assert in != null;
                while ( (message = (MessageToServer) in.readObject()) != null ) {
                    System.out.println(message);

                    if (message.getId().equals(MessageId.TERMINATE))
                        break;
                    else
                        multipleMatchesHandler.handle(this, message);
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
            System.err.println(e.getMessage());
        }
    }

    public void send(Message m) {
        assert out != null;

        try {
            out.writeObject(m);
            out.flush();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void disconnectAndCloseGame() {
        //CONNECTION CLOSED WITH THE CLIENT, NOTIFY ALL THE OTHER CLIENTS CONNECTED TO THIS GAME CONTROLLER
        //THAT THE GAME IS OVER
        GameController c = multipleMatchesHandler.getMap().get(this);

        if (c != null) {
            multipleMatchesHandler.removeClient(this);

            for (Player p: c.getAddedPlayers())
                c.getPlayerViews().get(p).playerDisconnection();
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public MultipleMatchesHandler getMultipleMatchesHandler() {
        return multipleMatchesHandler;
    }
}
