package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.messages.*;
import it.polimi.ingsw.am37.messages.initialization.*;
import it.polimi.ingsw.am37.messages.lobby.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.*;
import it.polimi.ingsw.am37.server.ClientHandler;

import java.util.ArrayList;
import java.util.List;

public class TCPVirtualView implements VirtualView {

    private final ClientHandler ch;

    public TCPVirtualView(ClientHandler ch) {
        this.ch = ch;
    }

    public ClientHandler getCh() {
        return ch;
    }

    //-------------------------------------------------------------------------------------------------



    public void acknowledgePlayer(Player p, String s) {
        //SENDS ACKNOWLEDGMENT MESSAGE TO THE CLIENT (USE NEW THREAD)
        ch.send(new NotifyMessage(MessageId.NOTIFY, s));
    }

    public void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers) {
        //SENDS LOBBY UPDATES TO THE REMOTE LOBBY VIEW (USE NEW THREADS)
        List<String> nicknames = new ArrayList<>();
        for (Player p: joined)
            nicknames.add(p.getNickname());

        ch.send(new UpdateLobbyMessage(MessageId.UPDATE_LOBBY, receiver.getNickname(), nicknames, lobbyNum, maxPlayers));
    }

    public void playerAdded(Player p) {
        ch.send(new PlayerJoinedMessage(MessageId.UPDATE_LOBBY, p.getNickname()));
    }

    public void sendInitial(List<StandardCard> cGold, List<StandardCard> cResource, StartCard sc, List<StandardCard> hand,
                            ObjectiveCard[] publicObjectives, ObjectiveCard[] objToChooseFrom, Resource goldDeckBack, Resource resourceDeckBack) {
        //SENDS INITIALIZATION MESSAGE TO THE CLIENT
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

            ch.send(new InitMessage(MessageId.INIT, cGoldId, cResourceId, sc.getId(), handId, publicObjId, privateObjId,
                    goldDeckBack, resourceDeckBack));

    }

    public void nowUnavailableToken(Player p, Token t) {
        //SENDS THE NOW UNAVAILABLE TOKEN AND THE PLAYER WHO CHOSE IT TO THE REMOTE VIEW (USE NEW THREAD)
        ch.send(new NowUnavailableMessage(MessageId.TOKEN, p.getNickname(), t));
    }

    /**
     * the method notifyTurn notifies the players when it's their turn.
     * @param p is the player notified.
     */
    public void notifyTurn(Player p, boolean blackToken) {
        //SENDS NOTIFICATION TO THE PLAYER THAT HAS ENTERED HIS TURN (USE NEW THREAD)
        //TRY RECONNECTING WITH PLAYER OR SKIP TURN
        if (blackToken)
            ch.send(new NotifyMessage(MessageId.NOTIFY, "your turn and token"));
        else
            ch.send(new NotifyMessage(MessageId.NOTIFY, "your turn"));
    }

    public void updatesDeckView(String deck, Resource back) {
        //SENDS DECKS UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
    }

    public void updatePlayerHandView(Player p, StandardCard c) {
        //SENDS HAND UPDATES TO THE REMOTE VIEW (USE NEW THREAD)
    }

    public void updatesCardView(List<StandardCard> cList) {
        //SENDS AVAILABLE CARDS UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
    }

    public void updatesPlayersKingdomView(Player p, int c, String s, Position pos) {
        //SENDS KINGDOM UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
        ch.send(new UpdateKingdomMessage(MessageId.UPDATE_KINGDOM, p.getNickname(), c, s, pos));
    }

    public void sendResults(PlayerPoints[] results) {
        //SENDS THE RESULTS OF THE GAME TO THE REMOTE VIEW (USE NEW THREADS)
    }

    public void actionNotPermittedMessaging(Player p, String errorMessage) {
        //SENDS ERROR MESSAGE TO CLIENT'S VIEW (USE NEW THREAD)

        //STUB FOR TESTING:
        System.err.println(p.getNickname() + ": " + errorMessage);
    }
}