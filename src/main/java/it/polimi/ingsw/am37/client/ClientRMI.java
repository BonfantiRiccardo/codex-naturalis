package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.server.ClientInterface;
import it.polimi.ingsw.am37.server.RMIServer;
import it.polimi.ingsw.am37.server.RMIServerStub;
import it.polimi.ingsw.am37.view.View;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientRMI implements RMIClientSkeleton, ClientConnectionInterface {
    private final String ip;
    private final int port;
    private final View v;

    public ClientRMI(String ip, int port, View v) {
        this.ip = ip;
        this.port = port;
        this.v = v;
    }

    public void startClient() throws RemoteException {
        System.out.println("correctly started");
        Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1098);
        try {
            RMIServerStub server = (RMIServerStub) reg.lookup("RMIServer");
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        System.out.println("Correctly connected");
    }

    @Override
    public void updateLobbyView() throws RemoteException {

    }

    @Override
    public void playerAdded() throws RemoteException {

    }

    @Override
    public void updateInitialPhase() throws RemoteException {

    }

    @Override
    public void sendAvailableToken() throws RemoteException {

    }

    @Override
    public void notifyPlayer() throws RemoteException {

    }

    @Override
    public void updateKingdom() throws RemoteException {

    }

    @Override
    public void updateHandView() throws RemoteException {

    }

    @Override
    public void updateDeckView() throws RemoteException {

    }

    @Override
    public void updateAvailable() throws RemoteException {

    }

    @Override
    public void showResults() throws RemoteException {

    }
}
