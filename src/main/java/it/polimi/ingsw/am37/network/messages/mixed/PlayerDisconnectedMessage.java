package it.polimi.ingsw.am37.network.messages.mixed;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

/**
 * This message is sent by the server to the client when a player disconnects.
 * The server will change the view state to DISCONNECTION.
 */
public class PlayerDisconnectedMessage extends MessageToClient {

    /**
     * Constructor.
     * @param id The message id.
     */
    public PlayerDisconnectedMessage(MessageId id) {
        super(id);
    }

    /**
     * This method will change the view state to DISCONNECTION.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        v.setState(ViewState.DISCONNECTION);
    }
}
