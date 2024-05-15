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

import java.rmi.RemoteException;
import java.util.ArrayList;
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
            cs.playerAdded();       //p.getNickname()
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
            cs.updateInitialPhase();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    public void nowUnavailableToken(Player p, Token t){
        try {
            cs.sendAvailableToken();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendPlayersInOrder(List<Player> players) {

    }

    public void notifyTurn(Player p){
        try {
            cs.notifyPlayer();
        } catch (RemoteException e) {
            new RuntimeException(e);
        }
    }

    @Override
    public void updatesDeckView(String deck, Resource back) {

    }

    @Override
    public void updatePlayerHandAndDeckView(String deck, Resource topOfDeck, int cardId) {

    }

    @Override
    public void updatesAvailableCardView(String deck, Resource topOfDeck, String listChanged, List<StandardCard> cardList) {

    }

    public void updatesDeckView(Deck d, Back s){

    }

    public void updatesPlayersKingdomView(Player p, int c, String s, Position pos){
        try {
            cs.updateKingdom();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendResults(PlayerPoints[] results){

    }

    public void actionNotPermittedMessaging(Player p, String errorMessage){

    }

}



