package it.polimi.ingsw.am37.common;

import it.polimi.ingsw.am37.common.messages.*;
import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.server.ServerTCP;

import java.net.Socket;

public class MessageDecoder {
    private GameController c;

    private final ServerTCP server;

    public MessageDecoder(ServerTCP server) {
        this.server = server;
    }

    public void decodeAndExecute(Socket s, Message m) {
        switch (m.getId()) {
            case CREATE -> {
                CreationMessage create = (CreationMessage) m;
                c = new GameController(new Player(create.getCreator()), create.getNum());
                System.out.println("correctly created");
                server.send(s, new AckMessage(MessageId.ACK, "Correctly created"));
            }

            case JOIN -> {
                JoinMessage join = (JoinMessage) m;
                try {
                    c.addPlayer((new Player(join.getNick())));
                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                         AlreadyAssignedException | NullPointerException e) {
                    server.send(s, new ErrorMessage(MessageId.ERROR, "Error, not added."));
                }
                System.out.println("correctly added");
                server.send(s, new AckMessage(MessageId.ACK, "Correctly added"));
            }

        }

    }
}
