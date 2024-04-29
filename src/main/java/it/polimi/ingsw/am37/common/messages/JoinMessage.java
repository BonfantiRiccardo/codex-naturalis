package it.polimi.ingsw.am37.common.messages;

import java.io.Serializable;

public class JoinMessage extends Message implements Serializable {
    private final int controllerHash;
    private final String nick;

    public JoinMessage(MessageId id, int controllerHash, String nick) {
        super(id);
        this.controllerHash = controllerHash;
        this.nick = nick;
    }

    public String getNick() {
        return nick;
    }

    public int getControllerHash() {
        return controllerHash;
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | hash: " + controllerHash + " | nick: " + nick;
    }
}
