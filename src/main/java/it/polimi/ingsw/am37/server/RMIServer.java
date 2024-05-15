package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;
import it.polimi.ingsw.am37.controller.RMIVirtualView;
import it.polimi.ingsw.am37.messages.ErrorMessage;
import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.lobby.LobbiesListMessage;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIServerStub {
    private List<RMIClientSkeleton> clients;

    private final MultipleMatchesHandler multipleMatchesHandler;

    public RMIServer(MultipleMatchesHandler multipleMatchesHandler) throws RemoteException {
        clients = new ArrayList<RMIClientSkeleton>();
        this.multipleMatchesHandler = multipleMatchesHandler;
    }

    public synchronized void join(RMIClientSkeleton client) throws RemoteException {
        clients.add(client);
    }

    public synchronized void leave(RMIClientSkeleton client) throws RemoteException {
        clients.remove(client);
    }

    @Override
    public void createGame(RMIClientSkeleton client, String name, int numOfPlayers) throws RemoteException {
        Player p = new Player(name);
        if ((2 <= numOfPlayers) && (numOfPlayers <= 4)) {
            GameController controller = new GameController(p,numOfPlayers);

            multipleMatchesHandler.addClient(client, controller );

            multipleMatchesHandler.addLobby(controller.hashCode(), controller);

            controller.setVirtualView(p, new RMIVirtualView(client));

            controller.getPlayerViews().get(p).updateLobbyView(p, controller.getAddedPlayers(), controller.hashCode(), controller.getNumOfPlayers());

        }
        else errorMessage(p.getNickname(), "The player number is invalid, game not created." );
    }

    public void availableLobbies(/*RMIClientSkeleton client*/) throws RemoteException {
        if(multipleMatchesHandler.getLobbyList().isEmpty()) {
            //client.errorMessage("There are no active games.");
        } else {
            //client.receiveLobbies(new ArrayList<>(ch.getMultipleMatchesHandler().getLobbyList().keySet())));
        }
    }

    @Override
    public void joinGame(RMIClientSkeleton client, int controllerHash, String name) throws RemoteException {

    }

    @Override
    public void placeStartCard(String player, int cardId, String side, Position pos) throws RemoteException {

    }

    @Override
    public void chooseToken(String p, Token t) throws RemoteException {

    }

    @Override
    public void chooseObjective(String p, int cardId) throws RemoteException {

    }

    @Override
    public void placeCard(String player, int cardId, String side, Position pos) throws RemoteException {

    }

    @Override
    public void drawCardFromDeck(String p, String  d) throws RemoteException {

    }

    @Override
    public void drawCardFromAvailable(String p, int cardId) throws RemoteException {

    }

    @Override
    public void errorMessage(String p, String description) throws RemoteException {

    }
}

