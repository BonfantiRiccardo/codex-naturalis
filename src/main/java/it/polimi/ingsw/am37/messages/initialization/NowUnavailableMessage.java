package it.polimi.ingsw.am37.messages.initialization;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

public class NowUnavailableMessage extends MessageToClient {
    private final String player;
    private final Token token;

    public NowUnavailableMessage(MessageId id, String player, Token token) {
        super(id);
        this.player = player;
        this.token = token;
    }

    @Override
    public void decodeAndExecute(View v) {
        for (ClientSidePlayer p: v.getLocalGameInstance().getPlayers())
            if (p.getNickname().equals(player))
                p.setToken(token);

        v.getLocalGameInstance().removeToken(token);
    }
}
