package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.messages.MessageToServer;
import it.polimi.ingsw.am37.server.ClientHandler;

import java.util.HashMap;
import java.util.Map;

public class MultipleMatchesHandler {
    private final Map<ClientHandler, GameController> map;
    private final Map<Integer, GameController> lobbyList;

    public MultipleMatchesHandler() {
        map = new HashMap<>();
        lobbyList = new HashMap<>();
    }

    public void handle(ClientHandler ch, MessageToServer m) {
        m.decodeAndExecute(map.get(ch), ch);

    }

    public void addClient(ClientHandler ch, GameController c) {
        map.put(ch, c);
    }

    public void removeClient(ClientHandler ch, GameController c) {
        map.remove(ch, c);
    }

    public void addLobby(int controllerHash, GameController c) {
        lobbyList.put(controllerHash, c);
    }

    public void removeLobby(int controllerHash) {
        lobbyList.remove(controllerHash);
    }

    public Map<Integer, GameController> getLobbyList() {
        return lobbyList;
    }
}