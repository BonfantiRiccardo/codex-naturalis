package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.server.RMIServer;

import java.rmi.RemoteException;

public class RMIVirtualServer implements VirtualServer{

    private final RMIServer RMIS;

    public RMIVirtualServer (RMIServer RMIS){
        this.RMIS=RMIS;
    }

    public RMIServer getRMIS() {
        return RMIS;
    }
    @Override
    public void createLobby(String nick, int numPlayers) {
        try {
            RMIS.createGame(nick, numPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askLobbies() {
        try {
            RMIS.AvailableLobbies();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinLobby(int hash, String nick) {
        try {
            RMIS.joinGame(hash,nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(String player, int cardId, String side, Position pos) {
        try {
            RMIS.placeStartCard(player,cardId,side,pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseToken(String player, Token token) {
        try {
            RMIS.chooseToken(player,token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseObjective(String player, int cardId) {
        try {
            RMIS.chooseObjective(player,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(String player, int cardId, String side, Position pos) {
        try {
            RMIS.placeCard(player,cardId,side,pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawCardFromDeck(Player p, Deck d) {        //REPlACE WITH BELOW METHODS
        try {
            RMIS.drawCardFromDeck(p,d);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void drawCardFromAvailable(Player p, int cardId) {        //REPlACE WITH BELOW METHODS
        try {
            RMIS.drawCardFromAvailable(p,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCardFromDeck(String player, String deck) {

    }

    @Override
    public void drawCardFromAvailable(String player, int cardId) {

    }
}
