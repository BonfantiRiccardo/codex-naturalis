package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerIStub extends Remote {

    void createGame(String name, int numOfPlayers) throws RemoteException;

    void joinGame(int controllerHash, String name) throws RemoteException;

    void placeStartCard(Player p, StartCard sc, Side s) throws RemoteException;

    void chooseToken(Player p, Token t) throws RemoteException;

    void chooseObjective(Player p, ObjectiveCard o) throws RemoteException;

    void placeCard(Player p, StandardCard sc, Side s, Position pos) throws RemoteException;

    void drawCardFromDeck(Player p, Deck d) throws RemoteException;

    void drawCardFromAvailable(Player p, StandardCard sc) throws RemoteException;
}
