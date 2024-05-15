package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.server.ClientInterface;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientSkeleton extends Remote, ClientInterface {

    void updateLobbyView() throws RemoteException;

    void playerAdded() throws RemoteException;

    void updateInitialPhase() throws RemoteException;

    void sendAvailableToken() throws RemoteException;

    void notifyPlayer () throws RemoteException;

    void updateKingdom() throws RemoteException;

    void updateHandView() throws RemoteException;

    void updateDeckView() throws RemoteException;

    void updateAvailable() throws RemoteException;

    void showResults() throws RemoteException;

}
