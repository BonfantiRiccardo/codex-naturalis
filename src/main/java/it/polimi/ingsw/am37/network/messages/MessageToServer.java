package it.polimi.ingsw.am37.network.messages;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.network.server.ClientHandler;

public abstract class MessageToServer extends Message{

    public MessageToServer(MessageId id) {
        super(id);
    }

    public abstract void decodeAndExecute(GameController c, ClientHandler ch);
}
