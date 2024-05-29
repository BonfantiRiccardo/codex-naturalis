package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.view.View;

public class PingToClient extends MessageToClient{

    public PingToClient(MessageId id) {
        super(id);
    }

    @Override
    public void decodeAndExecute(View v) {}     //DO NOTHING
}
