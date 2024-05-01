package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.EventListener;
import java.util.List;

public interface VirtualView extends EventListener {

    void acknowledgePlayer(Player p);

    void updateLobbyView(List<Player> joined, int numPlayers, int maxPlayers);

    void sendAvailable(List<StandardCard> cGold, List<StandardCard> cResource);

    void sendStartCard(Player p, StartCard sc);

    void nowUnavailableToken(Player p, Token t);

    void generateHandView(Player p, List<StandardCard> hand);

    void generatePublicObjectivesView(Player p, ObjectiveCard[] publicObjectives);

    void updatesObjectivesView(Player p, ObjectiveCard[] objToChooseFrom);

    void updatesPrivateObjectivesView(Player p, ObjectiveCard privateObjective);

    void notifyTurn(Player p);

    void updatesDeckView(Deck d, Back s);

    void updatePlayerHandView(Player p, StandardCard c);

    void updatesCardView(List<StandardCard> cList);

    void updatesPlayersKingdomView(Player p, GameCard c, Side s, Position pos);

    void sendResults(PlayerPoints[] results);

    void actionNotPermittedMessaging(Player p, String errorMessage);

}
