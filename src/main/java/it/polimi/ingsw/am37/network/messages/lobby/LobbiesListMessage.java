package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.List;
/**
 * This message is sent by the server to the client when the client asks for the list of lobbies.
 * The server will send the list of lobbies to the client.
 */
public class LobbiesListMessage extends MessageToClient {
    /**
     * The list of lobbies.
     */
    private final List<Integer> lobbies;

    /**
     * Constructor.
     * @param id The message id.
     * @param lobbies The list of lobbies.
     */
    public LobbiesListMessage(MessageId id, List<Integer> lobbies) {
        super(id);
        this.lobbies = lobbies;
    }

    /**
     * This method will set the list of lobbies in the view and change the view state to CHOOSE_LOBBY.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        v.getLocalGameInstance().setListOfLobbies(lobbies);

        synchronized (v) {
            v.setState(ViewState.CHOOSE_LOBBY);
            v.notify();
        }
    }

    /**
     * This method will return a string representation of the message.
     * @return The string representation of the message.
     */
    @Override
    public String toString() {
        return "Received: " + super.toString() + " | lobbies: " + lobbies;
    }
}
