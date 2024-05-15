package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

import java.util.List;

public class TurnsMessage extends MessageToClient {
    private final List<String> playersInOrder;


    public TurnsMessage(MessageId id, List<String> playersInOrder) {
        super(id);
        this.playersInOrder = playersInOrder;
    }

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
