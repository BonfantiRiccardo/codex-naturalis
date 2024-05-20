package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RMIVirtualView implements VirtualView{

    private final RMIClientSkeleton cs;

    public RMIVirtualView (RMIClientSkeleton cs){
        this.cs=cs;
    }

    public RMIClientSkeleton getCs() {
        return cs;
    }

    public void acknowledgePlayer(Player p, String s){
        try {
            cs.notifyPlayer(s);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

    }

    public void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers){
        List<String> nicknames = new ArrayList<>();
        for (Player p : joined)
            nicknames.add(p.getNickname());
        try {
            cs.updateLobbyView(receiver.getNickname(), nicknames, lobbyNum, maxPlayers);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void playerAdded(Player p) {
        try {
            cs.playerAdded(p.getNickname());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendInitial(List<StandardCard> cGold, List<StandardCard> cResource, StartCard sc, List<StandardCard> hand,
                     ObjectiveCard[] publicObjectives, ObjectiveCard[] objToChooseFrom, Resource goldDeckBack, Resource resourceDeckBack){
        List<Integer> cGoldId = new ArrayList<>();
        for (StandardCard c: cGold)
            cGoldId.add(c.getId());

        List<Integer> cResourceId = new ArrayList<>();
        for (StandardCard c: cResource)
            cResourceId.add(c.getId());

        List<Integer> handId = new ArrayList<>();
        for (StandardCard c: hand)
            handId.add(c.getId());

        List<Integer> publicObjId = new ArrayList<>();
        for (ObjectiveCard c: publicObjectives)
            publicObjId.add(c.getId());

        List<Integer> privateObjId = new ArrayList<>();
        for (ObjectiveCard c: objToChooseFrom)
            privateObjId.add(c.getId());
        try {
            cs.updateInitialPhase(cGoldId,cResourceId,sc.getId(),handId,publicObjId,privateObjId,goldDeckBack,resourceDeckBack);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void nowUnavailableToken(Player p, Token t){
        try {
            cs.sendNowUnavailableToken(p.getNickname(),t);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPlayersInOrder(List<Player> players) {
        List<String> nicknames = new ArrayList<>();
        for (Player p: players)
            nicknames.add(p.getNickname());

        try {
            cs.sendPlayersInOrder(nicknames);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void notifyTurn(Player p){
        try {
            cs.notifyPlayer("your turn");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatesDeckView(String deck, Resource back) {
        try {
            cs.updateDeckView(deck,back);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatePlayerHandAndDeckView(String deck, Resource topOfDeck, int cardId) {
        try {
            cs.updateHandAndDeckView(deck,topOfDeck,cardId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updatesAvailableCardView(String deck, Resource topOfDeck, String listChanged, List<StandardCard> cardList) {
        List<Integer> availableId = new ArrayList<>();
        for (StandardCard c: cardList)
            availableId.add(c.getId());
        try {
            cs.updateAvailable(deck,topOfDeck,listChanged,availableId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatesPlayersKingdomView(Player p, int c, String s, Position pos){
        try {
            cs.updateKingdom(p.getNickname(),c,s,pos);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResults(PlayerPoints[] results){
        Map<String, Integer> playersPoints = new HashMap<>();
        Map<String, Integer> playersCompletions = new HashMap<>();

        for (PlayerPoints p: results) {
            playersPoints.put(p.getPlayer().getNickname(), p.getPoints());
            playersCompletions.put(p.getPlayer().getNickname(), p.getNumOfCompletion());
        }

        try {
            cs.showResults(playersPoints, playersCompletions);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void playerDisconnection() {
        try {
            cs.playerDisconnection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}



