package it.polimi.ingsw.am37.view.virtualserver;

import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.util.EventListener;

/**
 * This interface is used to define the methods that the VirtualServer must implement.
 * The VirtualServer is the class that sends the messages to the server.
 */
public interface VirtualServer extends EventListener {
    /**
     * This method is used to create a new lobby.
     * @param nick The nickname of the player that creates the lobby.
     * @param numPlayers The number of players that the lobby must have.
     */
    void createLobby(String nick, int numPlayers);

    /**
     * This method is used to ask the server to send the list of the available lobbies.
     */
    void askLobbies();

    /**
     * This method is used to join a lobby.
     * @param hash The hash code of the lobby.
     * @param nick The nickname of the player that wants to join the lobby.
     */

    void joinLobby(int hash, String nick);

    /**
     * This method is used to place the start card.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     * @param side The side of the card.
     * @param pos The position of the card.
     */
    void placeStartCard(String player, int cardId, String side, Position pos);

    /**
     * This method is used to make the player choose the token.
     * @param player The nickname of the player.
     * @param token The token chosen by the player.
     */
    void chooseToken(String player, Token token);

    /**
     * This method is used to make the player choose the objective.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     */
    void chooseObjective(String player, int cardId);

    /**
     * This method is used to make the player place a card.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     * @param side The side of the card.
     * @param pos The position of the card.
     */
    void placeCard(String player, int cardId, String side, Position pos);

    /**
     * This method is used to make the player draw a card from the deck.
     * @param player The nickname of the player.
     * @param deck The deck from which the player will draw the card.
     */
    void drawCardFromDeck(String player, String deck);

    /**
     * This method is used to make the player draw a card from the available cards.
     * @param player The nickname of the player.
     * @param cardId The hash code of the card.
     */
    void drawCardFromAvailable(String player, int cardId);
}
