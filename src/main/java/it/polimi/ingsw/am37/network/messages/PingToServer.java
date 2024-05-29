package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.server.ClientHandler;

public class PingToServer extends MessageToServer{

    public PingToServer(MessageId id) {
        super(id);
    }

    @Override
    public void decodeAndExecute(GameController c, ClientHandler ch) {} //DO NOTHING

}
