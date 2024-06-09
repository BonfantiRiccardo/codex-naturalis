package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.view.View;
/**
 * Message that is sent to the client to check if it is still connected
 */
public class PingToClient extends MessageToClient{

    /**
     * Constructor
     * @param id the id of the message
     */
    public PingToClient(MessageId id) {
        super(id);
    }

    /**
     * Decodes the message and executes the action that it represents
     * @param v the view that will execute the action
     */
    @Override
    public void decodeAndExecute(View v) {}     //DO NOTHING
}
