package it.polimi.ingsw.am37.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIClientSkeleton extends Remote {

    void updateLobbyView() throws RemoteException;

    void updateInitialPhase() throws RemoteException;

    void updateKingdom() throws RemoteException;

    void updateHandView() throws RemoteException;

    void updateDeckView() throws RemoteException;

    void updateAvailable() throws RemoteException;

    void showResults() throws RemoteException;

}
