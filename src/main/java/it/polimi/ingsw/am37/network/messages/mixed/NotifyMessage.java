package it.polimi.ingsw.am37.network.messages.mixed;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.beans.PropertyChangeEvent;

/**
 * This message is sent by the server to the client when a player joins the lobby.
 * The server will add the player to the lobby and send the updated lobby view to all the players in the lobby.
 */
public class NotifyMessage extends MessageToClient {
    /**
     * The message.
     */
    private final String message;

    /**
     * Constructor.
     * @param id The message id.
     * @param message The message.
     */
    public NotifyMessage(MessageId id, String message) {
        super(id);
        this.message = message;
    }

    /**
     * This method will receive the message and change the view state accordingly.
     * It will also notify the view.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            switch (message) {
                case "start card ok" -> {
                    v.setState(ViewState.CHOOSE_TOKEN);
                    v.notify();
                }
                case "token ok" -> {
                    v.setState(ViewState.CHOOSE_OBJECTIVE);
                    v.notify();
                }
                case "objective ok" -> {
                    if(v.getState().equals(ViewState.CHOOSE_OBJECTIVE)) {
                        v.setState(ViewState.NOT_TURN);
                        v.notify();
                    }
                }
                case "your turn" -> {
                    v.setState(ViewState.PLACE);
                    v.notify();
                }
                case "place ok" -> {
                    v.setState(ViewState.DRAW);
                    v.notify();
                }
                case  "draw ok" -> {                    //TO DELETE SINCE I CAN'T JUST NOTIFY THE PLAYER, I HAVE TO SEND THE NEW CARD
                    v.setState(ViewState.NOT_TURN);     //MAYBE USEFUL IN DRAW FROM AVAILABLE
                    v.notify();
                }
                case  "endgame" -> {
                    //add to updates
                    PropertyChangeEvent evt = new PropertyChangeEvent(
                            this,
                            "ENDGAME",
                            null,
                            null);
                    v.propertyChange(evt);
                }
                case  "last turn" -> {
                    //add to updates
                    PropertyChangeEvent evt = new PropertyChangeEvent(
                            this,
                            "LAST_TURN",
                            null,
                            null);
                    v.propertyChange(evt);
                }
            }
        }
    }
}
