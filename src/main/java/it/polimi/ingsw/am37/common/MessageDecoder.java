package it.polimi.ingsw.am37.common;

import it.polimi.ingsw.am37.common.messages.*;
import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.server.ClientHandler;

import java.util.*;

public class MessageDecoder {
    private final Map<Integer, GameController> lobbyList;

    public MessageDecoder() {
        lobbyList = new HashMap<>();
    }

    public void decodeAndExecute(ClientHandler ch, Message m) {
        switch (m.getId()) {
            case CREATE -> {
                CreationMessage creation = (CreationMessage) m;
                Player p = new Player(creation.getCreator());
                if(creation.getNum() >= 2 && creation.getNum() <= 4) {
                    GameController c = new GameController(p, creation.getNum());
                    lobbyList.put(c.hashCode(), c);
                    c.getPlayerViews().get(p).setCh(ch);
                    System.out.println("correctly created: " + c.hashCode());
                } else
                    ch.send(new ErrorMessage(MessageId.ERROR, "The player number is invalid, game not created."));
            }

            case REQUEST_LOBBY -> {
                List<Integer> lobbies = new ArrayList<>(lobbyList.keySet());
                ch.send(new LobbiesMessage(MessageId.LOBBIES, lobbies));
            }

            case JOIN -> {
                JoinMessage join = (JoinMessage) m;
                try {
                    Player p = new Player(join.getNick());
                    lobbyList.get(join.getControllerHash()).addPlayer(p);
                    lobbyList.get(join.getControllerHash()).getPlayerViews().get(p).setCh(ch);
                    System.out.println("correctly added: " + p.getNickname());
                } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException |
                         AlreadyAssignedException | NullPointerException e) {
                    ch.send(new ErrorMessage(MessageId.ERROR, e.getMessage()));
                }
            }
        }
    }

}
