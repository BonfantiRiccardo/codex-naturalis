package it.polimi.ingsw.am37.network.server;

import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used by the server to communicate with the client.
 * It contains all the methods that the server can call on the client.
 */
public interface RMIServerStub extends Remote {

    /**
     * This method is used to create a new game.
     * @param client The client that is creating the game.
     * @param clientId The id of the client that is creating the game.
     * @param name The name of the game.
     * @param numOfPlayers The number of players that the game will have.
     * @throws RemoteException If there is an error in the communication.
     */
    void createGame(RMIClientSkeleton client, int clientId, String name, int numOfPlayers) throws RemoteException;

    /**
     * This method is used to get the available lobbies.
     * @param client The client that is asking for the available lobbies.
     * @throws RemoteException If there is an error in the communication.
     */
    void availableLobbies(RMIClientSkeleton client) throws RemoteException;

    /**
     * This method is used to join a game.
     * @param client The client that is joining the game.
     * @param clientId The id of the client that is joining the game.
     * @param controllerHash The hash of the controller that the client is using.
     * @param name The name of the player that is joining the game.
     * @throws RemoteException If there is an error in the communication.
     */
    void joinGame(RMIClientSkeleton client, int clientId, int controllerHash, String name) throws RemoteException;

    /**
     * This method is used to make a player chose a token.
     * @param clientId  The id of the client that is making the choice.
     * @param p The name of the player that is making the choice.
     * @param t The token that the player is choosing.
     * @throws RemoteException If there is an error in the communication.
     */
    void chooseToken(int clientId, String p, Token t) throws RemoteException;

    /**
     * This method is used to make a player chose an objective.
     * @param clientId The id of the client that is making the choice.
     * @param p The name of the player that is making the choice.
     * @param cardId The id of the card that the player is choosing.
     * @throws RemoteException If there is an error in the communication.
     */
    void chooseObjective(int clientId, String p, int cardId) throws RemoteException;

    /**
     * This method is used to make a player place a card on the field
     * @param clientId The id of the client that is making the choice.
     * @param player The name of the player that is making the choice.
     * @param cardId The id of the card that the player is placing.
     * @param side The side of the card that the player is placing.
     * @param pos The position on the board where the player is placing the card.
     * @throws RemoteException If there is an error in the communication.
     */
    void placeCard(int clientId, String player, int cardId, String side, Position pos) throws RemoteException;

    /**
     * This method is used to make a player draw a card from the deck.
     * @param clientId The id of the client that is making the choice.
     * @param p The name of the player that is making the choice.
     * @param d The side of the card that the player is drawing.
     * @throws RemoteException If there is an error in the communication.
     */
    void drawCardFromDeck(int clientId, String p, String d) throws RemoteException;

    /**
     * This method is used to make a player draw a card from the available cards.
     * @param clientId The id of the client that is making the choice.
     * @param p The name of the player that is making the choice.
     * @param cardId The id of the card that the player is drawing.
     * @throws RemoteException If there is an error in the communication.
     */
    void drawCardFromAvailable(int clientId, String p, int cardId) throws RemoteException;

    /**
     * This method is used to check if the player is still connected.
     * @param clientId The id of the client that is being checked.
     * @throws RemoteException If there is an error in the communication.
     */
    void ping(int clientId) throws RemoteException;
}
