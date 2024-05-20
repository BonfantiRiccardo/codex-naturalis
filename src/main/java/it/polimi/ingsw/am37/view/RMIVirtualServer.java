package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.server.RMIServerStub;

import java.rmi.RemoteException;

public class RMIVirtualServer implements VirtualServer{

    private final RMIServerStub RMIS;
    private final RMIClientSkeleton clientSkeleton;
    private final int clientId;

    public RMIVirtualServer (RMIServerStub RMIS, RMIClientSkeleton clientSkeleton){
        this.RMIS = RMIS;
        this.clientSkeleton = clientSkeleton;
        this.clientId = clientSkeleton.hashCode();
    }

    @Override
    public void createLobby(String nick, int numPlayers) {
        try {
            RMIS.createGame(clientSkeleton, clientId, nick, numPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askLobbies() {
        try {
            RMIS.availableLobbies(clientSkeleton);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinLobby(int hash, String nick) {
        try {
            RMIS.joinGame(clientSkeleton,clientId, hash, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(String player, int cardId, String side, Position pos) {
        try {
            RMIS.placeCard(clientId, player, cardId, side, pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseToken(String player, Token token) {
        try {
            RMIS.chooseToken(clientId, player,token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseObjective(String player, int cardId) {
        try {
            RMIS.chooseObjective(clientId,player,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeCard(String player, int cardId, String side, Position pos) {
        try {
            RMIS.placeCard(clientId,player,cardId,side,pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCardFromDeck(String player, String deck) {
        try {
            RMIS.drawCardFromDeck(clientId,player,deck);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void drawCardFromAvailable(String player, int cardId) {
        try {
            RMIS.drawCardFromAvailable(clientId,player,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
