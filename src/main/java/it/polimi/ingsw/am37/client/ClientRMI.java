package it.polimi.ingsw.am37.client;

import it.polimi.ingsw.am37.server.RMIServerStub;
import it.polimi.ingsw.am37.view.ClientSidePlayer;
import it.polimi.ingsw.am37.view.RMIVirtualServer;
import it.polimi.ingsw.am37.view.View;
import it.polimi.ingsw.am37.view.ViewState;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ClientRMI extends UnicastRemoteObject implements RMIClientSkeleton, ClientConnectionInterface {
    private final String ip;
    private final int port;
    private final View v;

    public ClientRMI(String ip, int port, View v) throws RemoteException {
        this.ip = ip;
        this.port = port;
        this.v = v;
    }

    public void startClient() throws RemoteException {
        Registry reg = LocateRegistry.getRegistry(ip, port);

        try {
            RMIServerStub server = (RMIServerStub) reg.lookup("RMIServer");
            System.out.println("Connection established on port: " + port);

            RMIVirtualServer vs = new RMIVirtualServer(server, this);
            v.setVirtualServer(vs);

            v.handleGame();

        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public void updateLobbyView(String yourNickname, List<String> playerNickname, int lobbyNum, int totalPlayers) throws RemoteException {
        v.getLocalGameInstance().setMe(new ClientSidePlayer(yourNickname));

        for (String nick: playerNickname)
            if(!yourNickname.equals(nick))
                v.getLocalGameInstance().addPlayer(new ClientSidePlayer(nick));

        v.getLocalGameInstance().setNumOfLobby(lobbyNum);

        v.getLocalGameInstance().setNumOfPlayers(totalPlayers);

        v.setState(ViewState.WAIT_IN_LOBBY);
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
