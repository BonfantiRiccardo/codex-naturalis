package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

/**
 * This message is sent by the server to the client when a player joins the lobby.
 * The server will add the player to the lobby and send the updated lobby view to all the players in the lobby.
 */
public class PlayerJoinedMessage extends MessageToClient {
    /**
     * The nickname of the player.
     */
    private final String nickname;

    /**
     * Constructor.
     * @param id The message id.
     * @param nickname The nickname of the player.
     */
    public PlayerJoinedMessage(MessageId id, String nickname) {
        super(id);
        this.nickname = nickname;
    }

    /**
     * This method will add the player to the lobby and send the updated lobby view to all the players in the lobby.
     * @param v The view.
     */
    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            v.getLocalGameInstance().addPlayer(new ClientSidePlayer(nickname));

            v.notify();
        }
    }
}
