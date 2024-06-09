package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.server.ClientHandler;
/**
 * Message that is sent to the server to check if it is still connected
 */
public class PingToServer extends MessageToServer{

    /**
     * Constructor
     * @param id the id of the message
     */
    public PingToServer(MessageId id) {
        super(id);
    }

    /**
     * Decodes the message and executes the action that it represents
     * @param c the controller that will execute the action
     * @param ch the client handler that sent the message
     */
    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {} //DO NOTHING

}
