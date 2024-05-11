package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.controller.MultipleMatchesHandler;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class RMIServer implements RMIServerIStub {
    private List<RMIClientSkeleton> clients;

    private final MultipleMatchesHandler multipleMatchesHandler;

    public RMIServer(MultipleMatchesHandler multipleMatchesHandler) throws RemoteException {
        clients = new ArrayList<RMIClientSkeleton>();
        this.multipleMatchesHandler= multipleMatchesHandler;
    }

    public synchronized void join(RMIClientSkeleton client) throws RemoteException {
        clients.add(client);
    }

    public synchronized void leave(RMIClientSkeleton client) throws RemoteException {
        clients.remove(client);
    }

    @Override
    public void createGame(String name, int numOfPlayers) throws RemoteException {
        Player p = new Player(name);
        if((2 >= numOfPlayers) && (numOfPlayers<=4)) {
           // multipleMatchesHandler.addClient();
    }}

    @Override
    public void joinGame(int controllerHash, String name) throws RemoteException {

    }

    @Override
    public void placeStartCard(String rp, StartCard sc, Side s) throws RemoteException {

    }

    @Override
    public void chooseToken(Player p, Token t) throws RemoteException {

    }

    @Override
    public void chooseObjective(Player p, ObjectiveCard o) throws RemoteException {

    }

    @Override
    public void placeCard(Player p, StandardCard sc, Side s, Position pos) throws RemoteException {

    }

    @Override
    public void drawCardFromDeck(Player p, Deck d) throws RemoteException {

    }

    @Override
    public void drawCardFromAvailable(Player p, StandardCard sc) throws RemoteException {

    }
}
