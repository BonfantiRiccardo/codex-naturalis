package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToClient;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.util.List;

public class LobbiesListMessage extends MessageToClient {
    private final List<Integer> lobbies;

    public LobbiesListMessage(MessageId id, List<Integer> lobbies) {
        super(id);
        this.lobbies = lobbies;
    }

    @Override
    public void decodeAndExecute(View v) {
        v.getLocalGameInstance().setListOfLobbies(lobbies);

        synchronized (v) {      //DO NOT SYNCHRONIZE ON VIEW, LOCK ON SOMETHING ELSE
            v.setState(ViewState.CHOOSE_LOBBY);
            v.notify();
        }
    }

    @Override
    public String toString() {
        return "Received: " + super.toString() + " | lobbies: " + lobbies;
    }
}
