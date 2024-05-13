package it.polimi.ingsw.am37.view;

import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.EventListener;

public interface VirtualServer extends EventListener {
    void createLobby(String nick, int numPlayers);

    void askLobbies();

    void joinLobby(int hash, String nick);

    void placeStartCard(String player, int cardId, String side, Position pos);

    void chooseToken(String player, Token token);

    void chooseObjective(String player, int cardId);

    void placeCard(String player, int cardId, String side, Position pos);

    void drawCardFromDeck(Player p, Deck d);

    void drawCardFromAvailable(Player p, int cardId);
}
