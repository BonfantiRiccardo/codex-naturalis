package it.polimi.ingsw.am37.common.messages;

public class AckMessage extends Message{
    private final String ack;
    public AckMessage(MessageId id, String ack) {
        super(id);
        this.ack = ack;
    }

    public String getAck() {
        return ack;
    }
}
