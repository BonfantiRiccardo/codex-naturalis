package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.network.messages.MessageToServer;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * the MultipleMatchesHandler class handles all the games at the same time.
 */
public class MultipleMatchesHandler {
    /**
     * the map attribute is a map that associate each ClientHandler with the controller of his game.
     */
    private final Map<ClientHandler, GameController> map;
    /**
     * the mapRMI is a map that associate each ClientSkeleton with the controller of his game.
     */
    private final Map<RMIClientSkeleton, GameController> mapRMI;
    /**
     * the lobbyList attribute is a map that associate each lobby with the controller of his game.
     */
    private final Map<Integer, GameController> lobbyList;

    /**
     * MultipleMatchesHandler is a setter method that sets all the maps of the class.
     */
    public MultipleMatchesHandler() {
        map = new HashMap<>();
        lobbyList = new HashMap<>();
        mapRMI = new HashMap<>();
    }

    /**
     * the handle method calls the decodeAndExecute method giving him the ClientHandler who sent the message and the controller
     * associated, in case of TCP connection.
     * @param ch is the ClientHandler.
     * @param m is the message sent.
     */
    public void handle(ClientHandler ch, MessageToServer m) {
        m.decodeAndExecute(map.get(ch), ch);

    }

    /**
     * getMapRMI is a getter method that returns the map of the ClientSkeletons associated with their controller.
     * @return the mapRMI attribute.
     */
    public synchronized Map<RMIClientSkeleton, GameController> getMapRMI(){
        return mapRMI;
    }

    /**
     * the addClient method puts a new a ClientHandler and the Controller associated in the map.
     * @param ch is the ClientHandler.
     * @param c is the controller associated.
     */
    public synchronized void addClient(ClientHandler ch, GameController c) {
        map.put(ch, c);
    }

    /**
     * the removeClient method removes a ClientHandler from the map.
     * @param ch is the ClientHandler removed.
     */
    public synchronized void removeClient(ClientHandler ch) {
        map.remove(ch);
    }

    /**
     * getMap is a getter method that returns the map of ClientHandlers and their associated Controller.
     * @return the map attribute.
     */
    public Map<ClientHandler, GameController> getMap() {
        return map;
    }

    /**
     * the addLobby method puts in the lobbyList map a new lobby identified by an Integer and the controller associated.
     * @param controllerHash is the number of the lobby.
     * @param c is the controller associated.
     */
    public synchronized void addLobby(int controllerHash, GameController c) {
        lobbyList.put(controllerHash, c);
    }

    /**
     * the removeLobby method removes a lobby from the lobbyList map.
     * @param controllerHash is the number of the lobby removed.
     */
    public synchronized void removeLobby(int controllerHash) {
        lobbyList.remove(controllerHash);
    }

    /**
     * getLobbyList is a getter method that returns the map of the lobbies and their associated controller.
     * @return the map of lobbies and their associated controller.
     */
    public synchronized Map<Integer, GameController> getLobbyList() {
        return lobbyList;
    }

    /**
     * the addClient method puts in the mapRMI a new ClientSkeleton and the controller associated.
     * @param clientSkeleton is the ClientSkeleton.
     * @param c is the controller.
     */
    public synchronized void addClient(RMIClientSkeleton clientSkeleton, GameController c) {
        mapRMI.put(clientSkeleton, c);
    }

    /**
     * the removeClient method removes a ClientSkeleton from the mapRMI.
     * @param clientSkeleton is the ClientSkeleton removed.
     */
    public synchronized void removeClient(RMIClientSkeleton clientSkeleton) {
        mapRMI.remove(clientSkeleton);
    }
}