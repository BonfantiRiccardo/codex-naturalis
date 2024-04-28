package it.polimi.ingsw.am37.common.messages;

import java.io.Serializable;

public class JoinMessage extends Message implements Serializable {
    private final String nick;

    public JoinMessage(MessageId id, String nick) {
        super(id);
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    @Override
    public String toString() {
        return "Received: " + nick;
    }
}
