package it.polimi.ingsw.am37.messages;

public abstract class MessageToClient extends Message {

    public MessageToClient(MessageId id) {
        super(id);
    }

    public abstract void decodeAndExecute();
}
