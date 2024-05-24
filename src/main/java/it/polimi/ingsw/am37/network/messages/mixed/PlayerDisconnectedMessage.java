package it.polimi.ingsw.am37.network.messages.mixed;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

public class PlayerDisconnectedMessage extends MessageToClient {

    public PlayerDisconnectedMessage(MessageId id) {
        super(id);
    }

    @Override
    public void decodeAndExecute(View v) {
        v.setState(ViewState.DISCONNECTION);
    }
}
