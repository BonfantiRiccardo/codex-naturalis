package it.polimi.ingsw.am37.network.messages.mixed;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

/**
 * This message is sent by the server to the client when an error occurs.
 * The server will send the error message to the client.
 */
public class ErrorMessage extends MessageToClient {
    /**
     * The description of the error.
     */
    private final String description;

    /**
     * Constructor.
     * @param id The message id.
     * @param description The description of the error.
     */
    public ErrorMessage(MessageId id, String description) {
        super(id);
        this.description = description;
    }

    /**
     * This method will print the error message and change the view state to ERROR.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            v.printError(description);
            v.setState(ViewState.ERROR);
            v.notify();
        }
    }

    /**
     * This method will return a string representation of the message.
     * @return The string representation of the message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | description: " + description;
    }
}
