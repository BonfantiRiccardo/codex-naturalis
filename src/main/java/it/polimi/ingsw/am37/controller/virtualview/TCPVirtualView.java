package it.polimi.ingsw.am37.controller.virtualview;

import it.polimi.ingsw.am37.network.messages.*;
import it.polimi.ingsw.am37.network.messages.lobby.*;
import it.polimi.ingsw.am37.network.messages.initialization.*;
import it.polimi.ingsw.am37.network.messages.mixed.*;
import it.polimi.ingsw.am37.network.messages.turns.*;
import it.polimi.ingsw.am37.network.messages.endgame.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.*;
import it.polimi.ingsw.am37.network.server.ClientHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * the TCPVirtualVIew implements the methods of the VirtualView Interface in case the player chooses an RMI connection.
 */
public class TCPVirtualView implements VirtualView {
    /**
     * the ch attribute is the reference to the player's ClientHandler.
     */
    private final ClientHandler ch;

    /**
     * TCPVirtualView is a setter method for the ClientHandler.
     * @param ch is the Client Handler.
     */
    public TCPVirtualView(ClientHandler ch) {
        this.ch = ch;
    }

    /**
     * getCh is a getter method for the ClientHandler.
     * @return the ClientHandler.
     */
    public ClientHandler getCh() {
        return ch;
    }

    //-------------------------------------------------------------------------------------------------


    /**
     * the acknowledgePlayer method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param p is the player notified.
     * @param s is the message notified to the player.
     */
    public void acknowledgePlayer(Player p, String s) {
        //SENDS ACKNOWLEDGMENT MESSAGE TO THE CLIENT (USE NEW THREAD)
        ch.send(new NotifyMessage(MessageId.NOTIFY, s));
    }

    /**
     * the updateLobbyView method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param receiver is the player receiving the message.
     * @param joined is the list of players in the lobby.
     * @param lobbyNum is the number of players in the lobby.
     * @param maxPlayers is the maximum number of players the lobby can contain.
     */
    public void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers) {
        //SENDS LOBBY UPDATES TO THE REMOTE LOBBY VIEW (USE NEW THREADS)
        List<String> nicknames = new ArrayList<>();
        for (Player p: joined)
            nicknames.add(p.getNickname());

        ch.send(new UpdateLobbyMessage(MessageId.UPDATE_LOBBY, receiver.getNickname(), nicknames, lobbyNum, maxPlayers));
    }

    /**
     * the playerAdded method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param p is the player added.
     */
    public void playerAdded(Player p) {
        ch.send(new PlayerJoinedMessage(MessageId.UPDATE_LOBBY, p.getNickname()));
    }

    /**
     * the sendInitial method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param cGold is the available gold cards the player can draw.
     * @param cResource is the available resource cards the player can draw.
     * @param sc is the player's starting card.
     * @param hand is the player's hand.
     * @param publicObjectives is the public objectives the player has to complete.
     * @param objToChooseFrom is the private objectives the player has to choose from.
     * @param goldDeckBack is the top of the gold deck from which the player can draw.
     * @param resourceDeckBack is the top of the gold deck from which the player can draw.
     */
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

    /**
     * the nowUnavailableToken method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param p is the player choosing the token.
     * @param t is the token chosen.
     */
    public void nowUnavailableToken(Player p, Token t) {
        //SENDS THE NOW UNAVAILABLE TOKEN AND THE PLAYER WHO CHOSE IT TO THE REMOTE VIEW (USE NEW THREAD)
        ch.send(new NowUnavailableMessage(MessageId.TOKEN, p.getNickname(), t));
    }

    /**
     * the sendPlayersInOrder method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param players is the ordered list of the players.
     */
    @Override
    public void sendPlayersInOrder(List<Player> players) {
        List<String> nicknames = new ArrayList<>();
        for (Player p: players)
            nicknames.add(p.getNickname());

        ch.send(new TurnsMessage(MessageId.NOTIFY, nicknames));
    }

    /**
     * the notifyTurn method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param p is the player notified.
     */
    public void notifyTurn(Player p) {
        //SENDS NOTIFICATION TO THE PLAYER THAT HAS ENTERED HIS TURN (USE NEW THREAD)
        //TRY RECONNECTING WITH PLAYER OR SKIP TURN
        ch.send(new NotifyMessage(MessageId.NOTIFY, "your turn"));
    }

    /**
     * the updatesDeckView method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param back is the top of the deck requested.
     */
    public void updatesDeckView(String deck, Resource back) {
        //SENDS DECKS UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
        ch.send(new UpdateDeckView(MessageId.UPDATE_DECK, deck, back));
    }

    /**
     * the updatePlayerHandAndDeckView method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the new top of the deck requested.
     * @param cardId is the card the player has drawn and that now is in his hand.
     */
    public void updatePlayerHandAndDeckView(String deck, Resource topOfDeck, int cardId) {
        //SENDS HAND UPDATES TO THE REMOTE VIEW (USE NEW THREAD)
        ch.send(new DeckCardDrawnMessage(MessageId.UPDATE_DECK, deck, topOfDeck, cardId));
    }

    /**
     * the updatesAvailableCardView method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the resource of the new top of the deck requested.
     * @param listChanged is the new available card that has changed.
     * @param cardList is the new list of the available cards on the field.
     */
    public void updatesAvailableCardView(String deck, Resource topOfDeck, String listChanged, List<StandardCard> cardList) {
        //SENDS AVAILABLE CARDS UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
        List<Integer> availableId = new ArrayList<>();
        for (StandardCard c: cardList)
            availableId.add(c.getId());

        ch.send(new UpdateAvailableView(MessageId.UPDATE_AVAIL, deck, topOfDeck, listChanged, availableId));
    }

    /**
     * the updatesPlayersKingdomView method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param p is the player who places the card.
     * @param c is the cardId of the card to be placed.
     * @param s is the side of the card to be placed.
     * @param pos is the position where the player wants to place the card.
     */
    public void updatesPlayersKingdomView(Player p, int c, String s, Position pos) {
        //SENDS KINGDOM UPDATES TO THE REMOTE VIEW (USE NEW THREADS)
        ch.send(new UpdateKingdomMessage(MessageId.UPDATE_KINGDOM, p.getNickname(), c, s, pos));
    }

    /**
     * the sendResults method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     * @param results is the points of the players.
     */
    public void sendResults(PlayerPoints[] results) {
        //SENDS THE RESULTS OF THE GAME TO THE REMOTE VIEW (USE NEW THREADS)
        Map<String, Integer> playersPoints = new HashMap<>();
        Map<String, Integer> playersCompletions = new HashMap<>();

        for (PlayerPoints p: results) {
            playersPoints.put(p.getPlayer().getNickname(), p.getPoints());
            playersCompletions.put(p.getPlayer().getNickname(), p.getNumOfCompletion());
        }

        ch.send(new ResultsMessage(MessageId.SEND_RESULTS, playersPoints, playersCompletions));

        ch.getMultipleMatchesHandler().removeClient(ch);
    }

    /**
     * the playerDisconnection method implements the usage of the method in the VirtualView Interface, for the players
     * with a TCP connection.
     */
    @Override
    public void playerDisconnection() {
        if (ch != null) {
            ch.send(new PlayerDisconnectedMessage(MessageId.TERMINATE));

            ch.getMultipleMatchesHandler().removeClient(ch);
        }
    }

}