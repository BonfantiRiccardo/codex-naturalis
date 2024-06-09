package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.server.ClientHandler;
/**
 * Abstract class that represents a message that is sent to the server
 */
public abstract class MessageToServer extends Message{

    /**
     * Constructor
     * @param id the id of the message
     */
    public MessageToServer(MessageId id) {
        super(id);
    }

    /**
     * Decodes the message and executes the action that it represents
     * @param c the controller that will execute the action
     * @param ch the client handler that sent the message
     */
    public abstract void decodeAndExecute(GameController c, ClientHandler ch);
}
