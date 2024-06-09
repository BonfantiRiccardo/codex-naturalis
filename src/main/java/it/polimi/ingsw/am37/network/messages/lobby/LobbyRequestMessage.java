package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.ArrayList;

/**
 * This message is sent by the client to the server when the player wants to see the list of lobbies.
 * The server will send the list of lobbies to the client.
 */
public class LobbyRequestMessage extends MessageToServer {

    /**
     * Constructor.
     * @param id The message id.
     */
    public LobbyRequestMessage(MessageId id) {
        super(id);
    }

    /**
     * This method will send the list of lobbies to the client.
     * If there are no active games, the server will send an error message to the client.
     * If there are active games, the server will send the list of lobbies to the client.
     * @param c The game controller.
     * @param ch The client handler.
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        synchronized (ch.getMultipleMatchesHandler()) {
            if(ch.getMultipleMatchesHandler().getLobbyList().isEmpty()) {
                ch.send(new ErrorMessage(MessageId.ERROR, "There are no active games."));
            } else {
                ch.send(new LobbiesListMessage(MessageId.LOBBIES, new ArrayList<>(ch.getMultipleMatchesHandler().getLobbyList().keySet())));
            }
        }
    }

    /**
     * This method will return a string representation of the message.
     * @return The string representation of the message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString();
    }
}
