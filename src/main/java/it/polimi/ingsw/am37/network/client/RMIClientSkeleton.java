package it.polimi.ingsw.am37.network.client;

import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.server.ClientInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * the RMIClientSkeleton is the remote client seen by the server, that exposes the methods called by the server.
 */
public interface RMIClientSkeleton extends Remote, ClientInterface {
    /**
     * the updateLobbyView method sends to the player all the infos about the lobby, for example the nicknames of the players
     * in the lobby, the number of players in the lobby or the maximum capacity of the lobby.
     * @param yourNickname is the player receiving the message.
     * @param playerNickname is the list of players in the lobby.
     * @param lobbyNum is the number of players in the lobby.
     * @param totalPlayers is the maximum number of players the lobby can contain.
     */
    void updateLobbyView(String yourNickname, List<String> playerNickname, int lobbyNum, int totalPlayers) throws RemoteException;

    /**
     * the receiveLobbies method sets the list of available lobbies and change the state of the view to "choose_lobby".
     * @param Lobbies is the list of Lobbies.
     * @throws RemoteException when the connection is lost.
     */
    void receiveLobbies(ArrayList<Integer> Lobbies) throws RemoteException;

    /**
     * the playerAdded method adds a player to the lobby and notifies the other player about it.
     * @param player is the player added.
     */
    void playerAdded(String player) throws RemoteException;

    /**
     * the sendInitial method sends to the player the starting setup of the game, including the available gold and resource
     * cards on the field, the top of the decks, his hand, his starting card, the public and private objectives.
     * @param availableGold is the list of available gold cards the player can draw.
     * @param availableResource is the list of available resource cards the player can draw.
     * @param startCard is the player's starting card.
     * @param hand is the player's hand.
     * @param publicObjectives is the public objectives the player has to complete.
     * @param privateObjectives is the private objectives the player has to choose from.
     * @param goldDeckBack is the top of the gold deck from which the player can draw.
     * @param resourceDeckBack is the top of the gold deck from which the player can draw.
     */
    void updateInitialPhase(List<Integer> availableGold, List<Integer> availableResource, int startCard, List<Integer> hand,
                            List<Integer> publicObjectives, List<Integer> privateObjectives, Resource goldDeckBack, Resource resourceDeckBack) throws RemoteException;

    /**
     * the nowUnavailableToken method set to the player the token he chose, removing it from the list of the available
     * ones to choose from.
     * @param player is the player choosing the token.
     * @param token is the token chosen.
     */
    void sendNowUnavailableToken(String player, Token token) throws RemoteException;

    /**
     * the sendPlayersInOrder method sends a randomly generated list of the players, ordered according to their
     * turns in the game.
     * @param playersInOrder is the ordered list of the players.
     */
    void sendPlayersInOrder(List<String> playersInOrder) throws RemoteException;

    /**
     * the notifyPlayer method sends to the player confirmations about the previews action of the player, for example
     * if the player has drawn a card, if he has placed a card, if he has chosen the objectives, if he has chosen
     * his token or if it's his turn.
     * @param message is the message notified to the player.
     */
    void notifyPlayer (String message) throws RemoteException;

    /**
     * the updatesPlayersKingdomView method places a card and update the View of the player right after placing it,
     * either if it's a starting card or another card.
     * @param player is the player who places the card.
     * @param cardId is the cardId of the card to be placed.
     * @param side is the side of the card to be placed.
     * @param pos is the position where the player wants to place the card.
     */
    void updateKingdom(String player, int cardId, String side, Position pos) throws RemoteException;

    /**
     * the updatePlayerHandAndDeckView method updates the player's hand and the new top of the deck right after the player
     * has drawn a card from the deck.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param resource is the new top of the deck requested.
     * @param cardId is the card the player has drawn and that now is in his hand.
     */
    void updateHandAndDeckView(String deck, Resource resource, int cardId) throws RemoteException;

    /**
     * the updatesDeckView sends to the player the top of the resource or gold deck, based on which one is requested.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param resource is the top of the deck requested.
     */
    void updateDeckView(String deck, Resource resource) throws RemoteException;

    /**
     * the updatesAvailable method sends to the player the new available cards on the field.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the resource of the new top of the deck requested.
     * @param availableChanged is the new available card that has changed.
     * @param available is the new list of the available cards on the field.
     */
    void updateAvailable(String deck, Resource topOfDeck, String availableChanged, List<Integer> available) throws RemoteException;

    /**
     * the sendResults method calculates and shows the final points of each player.
     * @param PlayerPoints is a map of each player's points.
     * @param PlayerNumCompletedObjectives is a map of the number of objectives completed by each player.
     */
    void showResults(Map<String, Integer> PlayerPoints, Map<String, Integer> PlayerNumCompletedObjectives) throws RemoteException;

    /**
     * the error message method prints the error message given in the description.
     * @param description is the message sent.
     * @throws RemoteException when the connection is lost.
     */
    void errorMessage(String description) throws RemoteException;

    /**
     * the playerDisconnection method sets the status of a player to "disconnected"
     * @throws RemoteException when the connection is lost.
     */
    void playerDisconnection() throws RemoteException;

}
