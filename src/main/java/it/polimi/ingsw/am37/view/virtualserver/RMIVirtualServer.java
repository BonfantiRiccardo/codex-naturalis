package it.polimi.ingsw.am37.view.virtualserver;

import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.server.RMIServerStub;

import java.rmi.RemoteException;

/**
 * This class is the RMI implementation of the VirtualServer interface.
 */
public class RMIVirtualServer implements VirtualServer {

    /**
     * The RMIServerStub is the remote object that the client uses to communicate with the server.
     */
    private final RMIServerStub RMIS;
    /**
     * The RMIClientSkeleton is the remote object that the server uses to communicate with the client.
     */
    private final RMIClientSkeleton clientSkeleton;
    /**
     * The clientId is the hash code of the clientSkeleton object.
     */
    private final int clientId;

    /**
     * The constructor of the class.
     * @param RMIS The RMIServerStub object.
     * @param clientSkeleton The RMIClientSkeleton object.
     */
    public RMIVirtualServer (RMIServerStub RMIS, RMIClientSkeleton clientSkeleton){
        this.RMIS = RMIS;
        this.clientSkeleton = clientSkeleton;
        this.clientId = clientSkeleton.hashCode();
    }

    /**
     * This method is used to create a new lobby.
     * The client sends a request to the server to create a new lobby.
     * The server will create a new game and add the client to the lobby.
     * The server will send a response to the client with the hash code of the lobby.
     * The client will use the hash code to join the lobby.
     * @param nick The nickname of the client.
     * @param numPlayers The number of players in the lobby.
     */
    @Override
    public void createLobby(String nick, int numPlayers) {
        try {
            RMIS.createGame(clientSkeleton, clientId, nick, numPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to ask the server for the list of available lobbies.
     * The server will send a response to the client with the list of available lobbies.
     */
    @Override
    public void askLobbies() {
        try {
            RMIS.availableLobbies(clientSkeleton);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to join a lobby.
     * The client sends a request to the server to join a lobby.
     * The server will add the client to the lobby.
     * The server will send a response to the client with the list of players in the lobby.
     * @param hash The hash code of the lobby.
     * @param nick The nickname of the client.
     */
    @Override
    public void joinLobby(int hash, String nick) {
        try {
            RMIS.joinGame(clientSkeleton,clientId, hash, nick);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to place the start card.
     * The client sends a request to the server to place the start card.
     * The server will place the start card on the board.
     * The server will send a response to the client with the updated board.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     * @param side The side of the card.
     * @param pos The position of the card.
     */
    @Override
    public void placeStartCard(String player, int cardId, String side, Position pos) {
        try {
            RMIS.placeCard(clientId, player, cardId, side, pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to make the player choose the token.
     * The client sends a request to the server to make the player choose the token.
     * The server will send a response to the client with the updated board.
     * @param player The nickname of the player.
     * @param token The token chosen by the player.
     */
    @Override
    public void chooseToken(String player, Token token) {
        try {
            RMIS.chooseToken(clientId, player,token);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to make the player choose the objective.
     * The client sends a request to the server to make the player choose the objective.
     * The server will send a response to the client with the updated board.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     */
    @Override
    public void chooseObjective(String player, int cardId) {
        try {
            RMIS.chooseObjective(clientId,player,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to make the player place a card.
     * The client sends a request to the server to make the player place a card.
     * The server will send a response to the client with the updated board.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     * @param side The side of the card.
     * @param pos The position of the card.
     */
    @Override
    public void placeCard(String player, int cardId, String side, Position pos) {
        try {
            RMIS.placeCard(clientId,player,cardId,side,pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to make the player draw a card from the deck.
     * The client sends a request to the server to make the player draw a card from the deck.
     * The server will send a response to the client with the updated board.
     * @param player The nickname of the player.
     * @param deck The deck from which the player will draw the card.
     */
    @Override
    public void drawCardFromDeck(String player, String deck) {
        try {
            RMIS.drawCardFromDeck(clientId,player,deck);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method is used to make the player draw a card from the available cards.
     * The client sends a request to the server to make the player draw a card from the available cards.
     * The server will send a response to the client with the updated board.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     */
    @Override
    public void drawCardFromAvailable(String player, int cardId) {
        try {
            RMIS.drawCardFromAvailable(clientId,player,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
