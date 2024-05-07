package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.messages.*;
import it.polimi.ingsw.am37.messages.lobby.CreationMessage;
import it.polimi.ingsw.am37.messages.lobby.JoinMessage;
import it.polimi.ingsw.am37.messages.lobby.LobbyRequestMessage;
import it.polimi.ingsw.am37.model.sides.Position;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class TCPVirtualServer implements VirtualServer {
    private ObjectOutputStream out;

    public ObjectOutputStream getOut() {
        return out;
    }

    public void setOut(ObjectOutputStream out) {
        this.out = out;
    }

    @Override
    public void createLobby(String nick, int numPlayers) {
        try {
            out.writeObject(new CreationMessage(MessageId.CREATE, nick, numPlayers));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void askLobbies() {
        try {
            out.writeObject(new LobbyRequestMessage(MessageId.REQUEST_LOBBY));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void joinLobby(int hash, String nick) {
        try {
            out.writeObject(new JoinMessage(MessageId.JOIN, hash, nick));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void placeStartCard(String player, int cardId, String side, Position pos) {
        try {
            out.writeObject(new PlaceMessage(MessageId.PLACE_SC, player, cardId, side, pos));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void chooseToken() {

    }

    @Override
    public void chooseObjective() {

    }

    @Override
    public void placeCard() {

    }

    @Override
    public void drawCardFromDeck() {

    }

    @Override
    public void drawCardFromAvailable() {

    }
}
