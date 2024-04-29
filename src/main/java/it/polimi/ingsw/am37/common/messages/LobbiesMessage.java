package it.polimi.ingsw.am37.common.messages;

import java.util.List;

public class LobbiesMessage extends Message {
    private final List<Integer> lobbies;

    public LobbiesMessage(MessageId id, List<Integer> lobbies) {
        super(id);
        this.lobbies = lobbies;
    }

    public List<Integer> getLobbies() {
        return lobbies;
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | lobbies: " + lobbies;
    }
}
