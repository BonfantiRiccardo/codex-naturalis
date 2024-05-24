package it.polimi.ingsw.am37.network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected final MessageId id;

    public Message(MessageId id) {
        this.id = id;
    }

    public MessageId getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id: " + id;
    }
}
