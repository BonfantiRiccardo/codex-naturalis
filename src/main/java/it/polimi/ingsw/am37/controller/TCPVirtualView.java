package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.messages.MessageId;
import it.polimi.ingsw.am37.messages.UpdateLobbyMessage;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.GameCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;
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



    public void acknowledgePlayer(Player p) {
        //SENDS ACKNOWLEDGMENT MESSAGE TO THE CLIENT (USE NEW THREAD)
    }

    public void updateLobbyView(List<Player> joined, int numPlayers, int maxPlayers) {
        //SENDS LOBBY UPDATES TO THE REMOTE LOBBY VIEW (USE NEW THREADS)
        new Thread(() -> {      //USING THREADS IS PROBABLY OVERKILL
            List<String> nicknames = new ArrayList<>();
            for (Player p: joined)
                nicknames.add(p.getNickname());

            ch.send(new UpdateLobbyMessage(MessageId.UPDATE_LOBBY, nicknames, numPlayers, maxPlayers));

        }).start();
    }

    public void sendAvailable(List<StandardCard> cGold, List<StandardCard> cResource) {
        // Sends available cards to the client (USE NEW THREADS)
    }

    public void sendStartCard(Player p, StartCard sc) {
        //SEND START CARD TO THE PLAYER  (USE NEW THREAD)
    }

    public void nowUnavailableToken(Player p, Token t) {
        //SENDS THE NOW UNAVAILABLE TOKEN AND THE PLAYER WHO CHOSE IT TO THE REMOTE VIEW (USE NEW THREAD)
    }

    public void generateHandView(Player p, List<StandardCard> hand) {
        //SENDS GENERATED HAND TO THE REMOTE VIEW (USE NEW THREAD)
    }

    public void generatePublicObjectivesView(Player p, ObjectiveCard[] publicObjectives) {
        //SENDS THE TWO PUBLIC OBJECTIVE CARDS (USE NEW THREAD)
    }

    public void updatesObjectivesView(Player p, ObjectiveCard[] objToChooseFrom) {
        //SENDS THE TWO OBJECTIVE CARDS THAT THE PLAYER CAN CHOOSE FROM (USE NEW THREAD)
    }

    public void updatesPrivateObjectivesView(Player p, ObjectiveCard privateObjective) {
        //SENDS THE CHOSEN OBJECTIVE CARD TO THE PLAYER  (USE NEW THREAD)
    }

    public void notifyTurn(Player p) {
        //SENDS NOTIFICATION TO THE PLAYER THAT HAS ENTERED HIS TURN (USE NEW THREAD)
        //TRY RECONNECTING WITH PLAYER OR SKIP TURN
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

    public void updatesPlayersKingdomView(Player p, GameCard c, Side s, Position pos) {
        //SENDS KINGDOM UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
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