package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.view.View;

/**
 * Abstract class that represents a message that is sent to the client
 */
public abstract class MessageToClient extends Message {

    /**
     * Constructor
     * @param id the id of the message
     */
    public MessageToClient(MessageId id) {
        super(id);
    }

    /**
     * Decodes the message and executes the action that it represents
     * @param v the view that will execute the action
     */
    public abstract void decodeAndExecute(View v);
}
