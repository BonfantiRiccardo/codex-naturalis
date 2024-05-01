package it.polimi.ingsw.am37.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.server.ClientHandler;

import java.util.ArrayList;

public class LobbyRequest extends MessageToServer {

    public LobbyRequest(MessageId id) {
        super(id);
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {
        if(ch.getMultipleMatchesHandler().getLobbyList().isEmpty()) {
            ch.send(new ErrorMessage(MessageId.ERROR, "There are no active games."));
        } else {
            ch.send(new LobbiesMessage(MessageId.LOBBIES, new ArrayList<>(ch.getMultipleMatchesHandler().getLobbyList().keySet())));
        }
    }
}
