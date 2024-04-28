package it.polimi.ingsw.am37.common.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final MessageId id;

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
