package it.polimi.ingsw.am37.network.messages.lobby;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.messages.mixed.ErrorMessage;
import it.polimi.ingsw.am37.network.messages.MessageId;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.ArrayList;

public class LobbyRequestMessage extends MessageToServer {

    public LobbyRequestMessage(MessageId id) {
        super(id);
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        synchronized (ch.getMultipleMatchesHandler()) {
            if(ch.getMultipleMatchesHandler().getLobbyList().isEmpty()) {
                ch.send(new ErrorMessage(MessageId.ERROR, "There are no active games."));
            } else {
                ch.send(new LobbiesListMessage(MessageId.LOBBIES, new ArrayList<>(ch.getMultipleMatchesHandler().getLobbyList().keySet())));
            }
        }
    }

    @Override
    public String toString() {
        return "Received: " + super.toString();
    }
}
