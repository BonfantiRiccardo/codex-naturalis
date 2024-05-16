package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerStub extends Remote {

    void createGame(RMIClientSkeleton client, String name, int numOfPlayers) throws RemoteException;

    void availableLobbies(RMIClientSkeleton client) throws RemoteException;

    void joinGame(RMIClientSkeleton client, int controllerHash, String name) throws RemoteException;

    void chooseToken(RMIClientSkeleton client, String p, Token t) throws RemoteException;

    void chooseObjective(RMIClientSkeleton client, String p, int cardId) throws RemoteException;

    void placeCard(RMIClientSkeleton client, String player, int cardId, String side, Position pos) throws RemoteException;

    void drawCardFromDeck(RMIClientSkeleton client,String p, String d) throws RemoteException;

    void drawCardFromAvailable(RMIClientSkeleton client,String p, int cardId) throws RemoteException;

}
