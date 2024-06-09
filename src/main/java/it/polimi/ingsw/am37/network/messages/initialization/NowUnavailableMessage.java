package it.polimi.ingsw.am37.network.messages.initialization;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;
/**
 * This message is sent to the client when a token is no longer available.
 */
public class NowUnavailableMessage extends MessageToClient {
    /**
     * The player that has the token.
     */
    private final String player;
    /**
     * The token that is no longer available.
     */
    private final Token token;
    /**
     * Constructor of the class.
     * @param id the message id.
     * @param player the player that has the token.
     * @param token the token that is no longer available.
     */
    public NowUnavailableMessage(MessageId id, String player, Token token) {
        super(id);
        this.player = player;
        this.token = token;
    }

    /**
     * This method sets the token to the player that has it and removes the token from the game.
     * @param v the view.
     */
    @Override
    public void decodeAndExecute(View v) {
        for (ClientSidePlayer p: v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(player))
                p.setToken(token);

        v.getLocalGameInstance().removeToken(token);
    }
}
