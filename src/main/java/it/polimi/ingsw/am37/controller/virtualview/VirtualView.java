package it.polimi.ingsw.am37.controller.virtualview;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.EventListener;
import java.util.List;

/**
 * the VirtualView Interface exposes to the player the methods that the controller is able to call on the client's
 * actual View, without knowing with which connection protocol said client is connected.
 */
public interface VirtualView extends EventListener {
    /**
     * the acknowledgePlayer method is called to send to the player confirmations about the previews action of the player.
     * @param p is the player notified.
     * @param s is the message notified to the player.
     */
    void acknowledgePlayer(Player p, String s);

    /**
     * the updateLobbyView method is called when the player wants infos about the lobby.
     * @param receiver is the player receiving the message.
     * @param joined is the list of players in the lobby.
     * @param lobbyNum is the number of players in the lobby.
     * @param maxPlayers is the maximum number of players the lobby can contain.
     */
    void updateLobbyView(Player receiver, List<Player> joined, int lobbyNum, int maxPlayers);

    /**
     * the playerAdded method is called when a player is added to the lobby and notifies the other player about it.
     * @param p is the player added.
     */
    void playerAdded(Player p);

    /**
     * the sendInitial method is called to set up the start of the game.
     * @param cGold is the available gold cards the player can draw.
     * @param cResource is the available resource cards the player can draw.
     * @param sc is the player's starting card.
     * @param hand is the player's hand.
     * @param publicObjectives is the public objectives the player has to complete.
     * @param objToChooseFrom is the private objectives the player has to choose from.
     * @param goldDeckBack is the top of the gold deck from which the player can draw.
     * @param resourceDeckBack is the top of the gold deck from which the player can draw.
     */
    void sendInitial(List<StandardCard> cGold, List<StandardCard> cResource, StartCard sc, List<StandardCard> hand,
                     ObjectiveCard[] publicObjectives, ObjectiveCard[] objToChooseFrom, Resource goldDeckBack, Resource resourceDeckBack);

    /**
     * the nowUnavailableToken method is called to set up the token of the player.
     * ones to choose from.
     * @param p is the player choosing the token.
     * @param t is the token chosen.
     */
    void nowUnavailableToken(Player p, Token t);

    /**
     * the sendPlayersInOrder method is called to see the ordered list of the players according to their turn.
     * @param players is the ordered list of the players.
     */
    void sendPlayersInOrder(List<Player> players);

    /**
     * the notifyTurn method is called to notify a player his turn.
     * @param p is the player notified.
     */
    void notifyTurn(Player p);

    /**
     * the updatesDeckView is called by the player to see the top of the decks.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param back is the top of the deck requested.
     */
    void updatesDeckView(String deck, Resource back);

    /**
     * the updatePlayerHandAndDeckView method is called right after the player has drawn a card, to have an update of his hand
     * and of the new top of deck.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the new top of the deck requested.
     * @param cardId is the card the player has drawn and that now is in his hand.
     */
    void updatePlayerHandAndDeckView(String deck, Resource topOfDeck, int cardId);

    /**
     * the updatesAvailableCardView method is called by the player to see the available cards.
     * @param deck is a parameter which gives information about the nature of the deck, if it's gold or resource.
     * @param topOfDeck is the resource of the new top of the deck requested.
     * @param listChanged is the new available card that has changed.
     * @param cardList is the new list of the available cards on the field.
     */
    void updatesAvailableCardView(String deck, Resource topOfDeck, String listChanged, List<StandardCard> cardList);

    /**
     * the updatesPlayersKingdomView method is called to place a card and update the View of the player right after placing i,
     * either if it's a starting card or another card.
     * @param p is the player who places the card.
     * @param c is the cardId of the card to be placed.
     * @param s is the side of the card to be placed.
     * @param pos is the position where the player wants to place the card.
     */
    void updatesPlayersKingdomView(Player p, int c, String s, Position pos);

    /**
     * the sendResults method calculates and shows the final points of each player.
     * @param results is the points of the players.
     */
    void sendResults(PlayerPoints[] results);

    /**
     * the playerDisconnection method is called when a player disconnects.
     */
    void playerDisconnection();

}
