package it.polimi.ingsw.am37.view.virtualserver;

import it.polimi.ingsw.am37.network.messages.*;
import it.polimi.ingsw.am37.network.messages.mixed.*;
import it.polimi.ingsw.am37.network.messages.initialization.*;
import it.polimi.ingsw.am37.network.messages.lobby.*;
import it.polimi.ingsw.am37.network.messages.turns.*;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * This class is the implementation of the VirtualServer interface for the TCP connection.
 */
public class TCPVirtualServer implements VirtualServer {
    /**
     * The ObjectOutputStream used to send messages to the server.
     */
    private ObjectOutputStream out;

    /**
     * getter for the ObjectOutputStream.
     * @return the ObjectOutputStream.
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /**
     * setter for the ObjectOutputStream.
     * @param out the ObjectOutputStream.
     */
    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    /**
     * This method sends a message to the server to create a lobby.
     * @param nick the nickname of the player.
     * @param numPlayers the number of players in the lobby.
     */
    @Override
    public void createLobby(String nick, int numPlayers) {
        try {
            out.writeObject(new CreationMessage(MessageId.CREATE, nick, numPlayers));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to ask for the available lobbies.
     * The server will respond with a LobbyListMessage.
     * The client will then be able to join one of the lobbies.
     */
    @Override
    public void askLobbies() {
        try {
            out.writeObject(new LobbyRequestMessage(MessageId.REQUEST_LOBBY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to join a lobby.
     * The server will respond with a LobbyMessage.
     * The client will then be able to start the game.
     * @param hash the hash of the lobbies.
     * @param nick the nickname of the player.
     */
    @Override
    public void joinLobby(int hash, String nick) {
        try {
            out.writeObject(new JoinMessage(MessageId.JOIN, hash, nick));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to place the start card.
     * The server will respond with a PlaceMessage.
     * The client will then be able to place the workers.
     * @param player the nickname of the player.
     * @param cardId the id of the card.
     * @param side the side of the card.
     * @param pos the position of the card.
     */
    @Override
    public void placeStartCard(String player, int cardId, String side, Position pos) {
        try {
            out.writeObject(new PlaceMessage(MessageId.PLACE_SC, player, cardId, side, pos));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to choose a token.
     * The server will respond with a TokenMessage.
     * The client will then be able to choose the token.
     * @param player the nickname of the player.
     * @param token the token chosen by the player.
     */
    @Override
    public void chooseToken(String player, Token token) {
        try {
            out.writeObject(new ChooseTokenMessage(MessageId.TOKEN, player, token));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to choose an objective.
     * The server will respond with an ObjectiveMessage.
     * The client will then be able to choose the objective.
     * @param player the nickname of the player.
     * @param cardId the id of the card.
     */
    @Override
    public void chooseObjective(String player, int cardId) {
        try {
            out.writeObject(new ChooseObjectiveMessage(MessageId.OBJECTIVE, player, cardId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to place a card.
     * The server will respond with a PlaceMessage.
     * The client will then be able to place the card.
     * @param player the nickname of the player.
     * @param cardId the id of the card.
     * @param side the side of the card.
     * @param pos the position of the card.
     */
    @Override
    public void placeCard(String player, int cardId, String side, Position pos) {
        try {
            out.writeObject(new PlaceMessage(MessageId.PLACE, player, cardId, side, pos));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to draw a card from the deck.
     * The server will respond with a DrawDeckMessage.
     * The client will then be able to draw the card.
     * @param player the nickname of the player.
     * @param deck the deck from which the player will draw the card.
     */
    @Override
    public void drawCardFromDeck(String player, String deck) {
        try {
            out.writeObject(new DrawDeckMessage(MessageId.DRAW_DECK, player, deck));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method sends a message to the server to draw a card from the available cards.
     * The server will respond with a DrawAvailableMessage.
     * The client will then be able to draw the card.
     * @param player the nickname of the player.
     * @param cardId the id of the card.
     */
    @Override
    public void drawCardFromAvailable(String player, int cardId) {
        try {
            out.writeObject(new DrawAvailableMessage(MessageId.DRAW_AVAIL, player, cardId));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
