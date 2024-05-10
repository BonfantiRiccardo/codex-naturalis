package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.server.ClientHandler;

import java.util.List;

public class RMIVirtualView implements VirtualView{

    private final RMIClientSkeleton cs;

    public RMIVirtualView (RMIClientSkeleton cs){
        this.cs=cs;
    }

    public RMIClientSkeleton getCs() {
        return cs;
    }

    public void acknowledgePlayer(Player p, String s){

    }

    public void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers){

    }

    public void playerAdded(Player p) {

    }

    public void sendInitial(List<StandardCard> cGold, List<StandardCard> cResource, StartCard sc, List<StandardCard> hand,
                     ObjectiveCard[] publicObjectives, ObjectiveCard[] objToChooseFrom, Resource goldDeckBack, Resource resourceDeckBack){

    }


    public void nowUnavailableToken(Player p, Token t){

    }

    public void notifyTurn(Player p){

    }

    public void updatesDeckView(Deck d, Back s){

    }

    public void updatePlayerHandView(Player p, StandardCard c){

    }

    public void updatesCardView(List<StandardCard> cList){

    }

    public void updatesPlayersKingdomView(Player p, int c, String s, Position pos){

    }

    public void sendResults(PlayerPoints[] results){

    }

    public void actionNotPermittedMessaging(Player p, String errorMessage){

    }

}



