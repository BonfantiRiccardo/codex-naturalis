package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.EventListener;
import java.util.List;

public interface VirtualView extends EventListener {

    void acknowledgePlayer(Player p, String s);

    void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers);

    void playerAdded(Player p);

    void sendInitial(List<StandardCard> cGold, List<StandardCard> cResource, StartCard sc, List<StandardCard> hand,
                     ObjectiveCard[] publicObjectives, ObjectiveCard[] objToChooseFrom, Resource goldDeckBack, Resource resourceDeckBack);


    void nowUnavailableToken(Player p, Token t);

    void sendPlayersInOrder(List<Player> players);

    void notifyTurn(Player p);

    void updatesDeckView(String deck, Resource back);

    void updatePlayerHandAndDeckView(String deck, Resource topOfDeck, int cardId);

    void updatesAvailableCardView(String deck, Resource topOfDeck, String listChanged, List<StandardCard> cardList);

    void updatesPlayersKingdomView(Player p, int c, String s, Position pos);

    void sendResults(PlayerPoints[] results);

    void playerDisconnection();

}
