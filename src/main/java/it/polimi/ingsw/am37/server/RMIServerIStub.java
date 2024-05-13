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

    void placeStartCard(String player, int cardId, String side, Position pos) throws RemoteException;

    void chooseToken(String p, Token t) throws RemoteException;

    void chooseObjective(String p, int cardId) throws RemoteException;

    void placeCard(String player, int cardId, String side, Position pos) throws RemoteException;

    void drawCardFromDeck(Player p, Deck d) throws RemoteException;

    void drawCardFromAvailable(Player p, int cardId) throws RemoteException;
}
