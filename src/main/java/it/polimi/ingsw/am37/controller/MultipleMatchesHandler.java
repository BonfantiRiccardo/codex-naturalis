package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.HashMap;
import java.util.Map;

public class MultipleMatchesHandler {
    private final Map<ClientHandler, GameController> map;
    private final Map<RMIClientSkeleton, GameController> mapRMI;
    private final Map<Integer, GameController> lobbyList;

    public MultipleMatchesHandler() {
        map = new HashMap<>();
        lobbyList = new HashMap<>();
        mapRMI = new HashMap<>();
    }

    public void handle(ClientHandler ch, MessageToServer m) {
        m.decodeAndExecute(map.get(ch), ch);

    }

    public synchronized Map<RMIClientSkeleton, GameController> getMapRMI(){
        return mapRMI;
    }

    public synchronized void addClient(ClientHandler ch, GameController c) {
        map.put(ch, c);
    }

    public synchronized void removeClient(ClientHandler ch) {
        map.remove(ch);
    }

    public Map<ClientHandler, GameController> getMap() {
        return map;
    }

    public synchronized void addLobby(int controllerHash, GameController c) {
        lobbyList.put(controllerHash, c);
    }

    public synchronized void removeLobby(int controllerHash) {
        lobbyList.remove(controllerHash);
    }

    public synchronized Map<Integer, GameController> getLobbyList() {
        return lobbyList;
    }

    public synchronized void addClient(RMIClientSkeleton clientSkeleton, GameController c) {
        mapRMI.put(clientSkeleton, c);
    }

    public synchronized void removeClient(RMIClientSkeleton clientSkeleton) {
        mapRMI.remove(clientSkeleton);
    }
}