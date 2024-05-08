package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.util.EventListener;

public interface VirtualServer extends EventListener {
    void createLobby(String nick, int numPlayers);

    void askLobbies();

    void joinLobby(int hash, String nick);

    void placeStartCard(String player, int cardId, String side, Position pos);

    void chooseToken(String player, Token token);

    void chooseObjective(String player, int cardId);

    void placeCard();

    void drawCardFromDeck();

    void drawCardFromAvailable();
}
