package it.polimi.ingsw.am37.network.messages.initialization;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.clientmodel.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

import java.util.List;
/**
 * This message is used to set the order of the players in the game.
 * The first player in the list will have the black token.
 */
public class TurnsMessage extends MessageToClient {
    /**
     * List of the players in the game in the order they will play.
     */
    private final List<String> playersInOrder;


    /**
     * Constructor of the message.
     * @param id id of the message.
     * @param playersInOrder list of the players in the game in the order they will play.
     */
    public TurnsMessage(MessageId id, List<String> playersInOrder) {
        super(id);
        this.playersInOrder = playersInOrder;
    }

    /**
     * This method is called by the client to execute the action that the message
     * carries.
     * It sets the order of the players in the game.
     * The first player in the list will have the black token.
     * @param v is the view of the player.
     */
    @Override
    public void decodeAndExecute(View v) {
        v.getLocalGameInstance().setPlayersInOrder(playersInOrder);

        if (v.getLocalGameInstance().getMe().getNickname().equals(playersInOrder.getFirst()))
            v.getLocalGameInstance().getMe().setHasBlackToken(true);

        for (ClientSidePlayer p: v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(playersInOrder.getFirst()))
                p.setHasBlackToken(true);

    }
}
