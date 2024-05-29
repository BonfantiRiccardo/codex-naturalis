package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerStub extends Remote {

    void createGame(RMIClientSkeleton client, int clientId, String name, int numOfPlayers) throws RemoteException;

    void availableLobbies(RMIClientSkeleton client) throws RemoteException;

    void joinGame(RMIClientSkeleton client, int clientId, int controllerHash, String name) throws RemoteException;

    void chooseToken(int clientId, String p, Token t) throws RemoteException;

    void chooseObjective(int clientId, String p, int cardId) throws RemoteException;

    void placeCard(int clientId, String player, int cardId, String side, Position pos) throws RemoteException;

    void drawCardFromDeck(int clientId, String p, String d) throws RemoteException;

    void drawCardFromAvailable(int clientId, String p, int cardId) throws RemoteException;

    void ping(int clientId) throws RemoteException;
}
