package it.polimi.ingsw.am37.messages;

import java.util.List;

public class LobbiesMessage extends MessageToClient {
    private final List<Integer> lobbies;

    public LobbiesMessage(MessageId id, List<Integer> lobbies) {
        super(id);
        this.lobbies = lobbies;
    }

    @Override
    public void decodeAndExecute() {

    }

    public List<Integer> getLobbies() {
        return lobbies;
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | lobbies: " + lobbies;
    }
}
