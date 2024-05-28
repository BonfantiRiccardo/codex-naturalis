package it.polimi.ingsw.am37.controller.virtualview;

import it.polimi.ingsw.am37.network.client.RMIClientSkeleton;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.network.server.RMIServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the RMIVirtualVIew implements the methods of the VirtualView Interface in case the player chooses an RMI connection.
 */
public class RMIVirtualView implements VirtualView {
    /**
     * the cs attribute is the reference to the player's ClientSkeleton.
     */
    private final RMIClientSkeleton cs;
    /**
     * the rmiServer attribute is the reference to the server the player is connected to.
     */
    private final RMIServer rmiServer;

    /**
     * RMIVirtualView is a setter method for the ClientSkeleton and of the Server.
     * @param cs is the ClientSkeleton
     * @param rmiServer is the Server.
     */
    public RMIVirtualView (RMIClientSkeleton cs, RMIServer rmiServer){
        this.cs=cs;
        this.rmiServer=rmiServer;
    }

    /**
     * the acknowledgePlayer method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param p is the player notified.
     * @param s is the message notified to the player.
     */
    public void acknowledgePlayer(Player p, String s){
        try {
            cs.notifyPlayer(s);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }

    }

    /**
     * the updateLobbyView method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param receiver is the player receiving the message.
     * @param joined is the list of players in the lobby.
     * @param lobbyNum is the number of players in the lobby.
     * @param maxPlayers is the maximum number of players the lobby can contain.
     */
    public void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers){
        List<String> nicknames = new ArrayList<>();
        for (Player p : joined)
            nicknames.add(p.getNickname());
        try {
            cs.updateLobbyView(receiver.getNickname(), nicknames, lobbyNum, maxPlayers);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the playerAdded method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param p is the player added.
     */
    public void playerAdded(Player p) {
        try {
            cs.playerAdded(p.getNickname());
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the sendInitial method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
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
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the nowUnavailableToken method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param p is the player choosing the token.
     * @param t is the token chosen.
     */
    public void nowUnavailableToken(Player p, Token t){
        try {
            cs.sendNowUnavailableToken(p.getNickname(),t);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the sendPLayersInOrder method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param players is the ordered list of the players.
     */
    @Override
    public void sendPlayersInOrder(List<Player> players) {
        List<String> nicknames = new ArrayList<>();
        for (Player p: players)
            nicknames.add(p.getNickname());

        try {
            cs.sendPlayersInOrder(nicknames);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the notifyTurn method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param p is the player notified.
     */
    public void notifyTurn(Player p){
        try {
            cs.notifyPlayer("your turn");
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the updatesDeckView method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param back is the top of the deck requested.
     */
    @Override
    public void updatesDeckView(String deck, Resource back) {
        try {
            cs.updateDeckView(deck,back);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the updatePlayerHandAndDeckView method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the new top of the deck requested.
     * @param cardId is the card the player has drawn and that now is in his hand.
     */
    @Override
    public void updatePlayerHandAndDeckView(String deck, Resource topOfDeck, int cardId) {
        try {
            cs.updateHandAndDeckView(deck,topOfDeck,cardId);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the updatesAvailableCardView method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the resource of the new top of the deck requested.
     * @param listChanged is the new available card that has changed.
     * @param cardList is the new list of the available cards on the field.
     */
    @Override
    public void updatesAvailableCardView(String deck, Resource topOfDeck, String listChanged, List<StandardCard> cardList) {
        List<Integer> availableId = new ArrayList<>();
        for (StandardCard c: cardList)
            availableId.add(c.getId());
        try {
            cs.updateAvailable(deck,topOfDeck,listChanged,availableId);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the updatesPlayersKingdomView method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param p is the player who places the card.
     * @param c is the cardId of the card to be placed.
     * @param s is the side of the card to be placed.
     * @param pos is the position where the player wants to place the card.
     */
    public void updatesPlayersKingdomView(Player p, int c, String s, Position pos){
        try {
            cs.updateKingdom(p.getNickname(),c,s,pos);
        } catch (RemoteException e) {
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the sendResults method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     * @param results is the points of the players.
     */
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
            System.out.println("Client disconnected");
            rmiServer.playerDisconnected(cs);
        }
    }

    /**
     * the playerDisconnection method implements the usage of the method in the VirtualView Interface, for the players
     * with an RMI connection.
     */
    @Override
    public void playerDisconnection() {
        try {
            rmiServer.getMultipleMatchesHandler().removeClient(cs);
            cs.playerDisconnection();
        } catch (RemoteException e) {
            System.out.println("Handling client disconnection");
        }
    }
}



