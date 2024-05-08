package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.messages.*;
import it.polimi.ingsw.am37.messages.initialization.InitMessage;
import it.polimi.ingsw.am37.messages.initialization.NowUnavailableMessage;
import it.polimi.ingsw.am37.messages.initialization.UpdateKingdomMessage;
import it.polimi.ingsw.am37.messages.lobby.PlayerJoinedMessage;
import it.polimi.ingsw.am37.messages.lobby.UpdateLobbyMessage;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.Deck;
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
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL

            ch.send(new NotifyMessage(MessageId.NOTIFY, s));

        }).start();
    }

    public void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers) {
        //SENDS LOBBY UPDATES TO THE REMOTE LOBBY VIEW (USE NEW THREADS)
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL
            List<String> nicknames = new ArrayList<>();
            for (Player p: joined)
                nicknames.add(p.getNickname());

            ch.send(new UpdateLobbyMessage(MessageId.UPDATE_LOBBY, receiver.getNickname(), nicknames, lobbyNum, maxPlayers));

        }).start();
    }

    public void playerAdded(Player p) {
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL

            ch.send(new PlayerJoinedMessage(MessageId.UPDATE_LOBBY, p.getNickname()));

        }).start();
    }

    public void sendInitial(List<StandardCard> cGold, List<StandardCard> cResource, StartCard sc, List<StandardCard> hand,
                            ObjectiveCard[] publicObjectives, ObjectiveCard[] objToChooseFrom, Resource goldDeckBack, Resource resourceDeckBack) {
        //SENDS INITIALIZATION MESSAGE TO THE CLIENT
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL
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

        }).start();
    }

    public void nowUnavailableToken(Player p, Token t) {
        //SENDS THE NOW UNAVAILABLE TOKEN AND THE PLAYER WHO CHOSE IT TO THE REMOTE VIEW (USE NEW THREAD)
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL

            ch.send(new NowUnavailableMessage(MessageId.TOKEN, p.getNickname(), t));

        }).start();
    }

    /**
     * the method notifyTurn notifies the players when it's their turn.
     * @param p is the player notified.
     */
    public void notifyTurn(Player p) {
        //SENDS NOTIFICATION TO THE PLAYER THAT HAS ENTERED HIS TURN (USE NEW THREAD)
        //TRY RECONNECTING WITH PLAYER OR SKIP TURN
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL

            ch.send(new NotifyMessage(MessageId.NOTIFY, "your turn"));

        }).start();
    }

    public void updatesDeckView(Deck d, Back s) {
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
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL
            ch.send(new UpdateKingdomMessage(MessageId.UPDATE_KINGDOM, p.getNickname(), c, s, pos));

        }).start();
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