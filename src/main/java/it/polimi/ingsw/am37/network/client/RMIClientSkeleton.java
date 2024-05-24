package it.polimi.ingsw.am37.network.client;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.server.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RMIClientSkeleton extends Remote, ClientInterface {

    void updateLobbyView(String yourNickname, List<String> playerNickname, int lobbyNum, int totalPlayers) throws RemoteException;

    void receiveLobbies(ArrayList<Integer> Lobbies) throws RemoteException;

    void playerAdded(String player) throws RemoteException;

    void updateInitialPhase(List<Integer> availableGold, List<Integer> availableResource, int startCard, List<Integer> hand,
                            List<Integer> publicObjectives, List<Integer> privateObjectives, Resource goldDeckBack, Resource resourceDeckBack) throws RemoteException;

    void sendNowUnavailableToken(String player, Token token) throws RemoteException;

    void sendPlayersInOrder(List<String> playersInOrder) throws RemoteException;

    void notifyPlayer (String message) throws RemoteException;

    void updateKingdom(String player, int cardId, String side, Position pos) throws RemoteException;

    void updateHandAndDeckView(String deck, Resource resource, int cardId) throws RemoteException;

    void updateDeckView(String deck, Resource resource) throws RemoteException;

    void updateAvailable(String deck, Resource topOfDeck, String availableChanged, List<Integer> available) throws RemoteException;

    void showResults(Map<String, Integer> PlayerPoints, Map<String, Integer> PlayerNumCompletedObjectives) throws RemoteException;

    void errorMessage(String description) throws RemoteException;

    void playerDisconnection() throws RemoteException;

}
