package it.polimi.ingsw.am37.messages;

import it.polimi.ingsw.am37.view.View;

public abstract class MessageToClient extends Message {

    public MessageToClient(MessageId id) {
        super(id);
    }

    public abstract void decodeAndExecute(View v);
}
