package it.polimi.ingsw.am37.messages.lobby;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.MessageToClient;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.View;

public class PlayerJoinedMessage extends MessageToClient {
    private final String nickname;

    public PlayerJoinedMessage(MessageId id, String nickname) {
        super(id);
        this.nickname = nickname;
    }

    @Override
    public void decodeAndExecute(View v) {
        synchronized (v) {
            v.getLocalGameInstance().addPlayer(new ClientSidePlayer(nickname));

            v.notify();
        }
    }
}
