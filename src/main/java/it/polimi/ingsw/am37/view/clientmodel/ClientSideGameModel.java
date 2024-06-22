package it.polimi.ingsw.am37.view.clientmodel;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the client-side representation of the game model.
 * It contains all the information that the client needs to know about the game.
 * It is updated by the server and the client-side controller.
 */
public class ClientSideGameModel {
    /**
     * The listener that will be notified when the model changes.
     */
    private PropertyChangeListener listener;
    /**
     * The list of all the resource cards in the game.
     */
    private final List<ResourceCard> resourceCards;
    /**
     * The list of all the gold cards in the game.
     */
    private final List<GoldCard> goldCards;
    /**
     * The list of all the objective cards in the game.
     */
    private final List<ObjectiveCard> objectiveCards;
    /**
     * The list of all the start cards in the game.
     */
    private final List<StartCard> startCards;
    /**
     * The list of all the tokens in the game.
     */
    private final List<Token> tokens;
    /**
     * The list of all the players in the game.
     */

    private List<Integer> listOfLobbies;
    /**
     * The number of the lobby the player is in.
     */
    private int numOfLobby;
    /**
     * The number of players in the lobby.
     */
    private int numOfPlayers;

    /**
     * The player that is currently playing.
     */
    private ClientSidePlayer me;
    /**
     * The list of tokens that are available to be chosen by the players.
     */
    private List<ObjectiveCard> privateObjectives;
    /**
     * The start card that the player has chosen.
     */
    private StartCard myStartCard;
    /**
     * The private objective card that the player has chosen.
     */
    private ObjectiveCard myPrivateObjective;
    /**
     * The hand of the player.
     */
    private List<StandardCard> myHand;
    /**
     * The list of all the players in the game.
     */
    private final List<ClientSidePlayer> players;

    /**
     * The list of all the available resource cards.
     */
    private List<StandardCard> availableResourceCards;
    /**
     * The list of all the available gold cards.
     */
    private List<StandardCard> availableGoldCards;
    /**
     * The resource card that is on top of the gold deck.
     */
    private Resource topOfGoldDeck;
    /**
     * The resource card that is on top of the resource deck.
     */

    private Resource topOfResourceDeck;
    /**
     * The list of all the public objectives in the game.
     */
    private List<ObjectiveCard> publicObjectives;
    /**
     * The list of players in order of turn.
     */
    private List<String> playersInOrder;
    /**
     * The nickname of the player that is currently playing.
     */
    private String currentPlayer;

    /**
     * Creates a new client-side game model.
     * It initializes all the lists of cards and players.
     * It creates a new card creator to create all the cards.
     * It initializes the list of lobbies.
     * It initializes the list of players.
     * It initializes the list of tokens.
     * It initializes the list of resource cards.
     * It initializes the list of gold cards.
     * It initializes the list of objective cards.
     * It initializes the list of start cards.
     * It initializes the list of available resource cards.
     * It initializes the list of available gold cards.
     * It initializes the list of public objectives.
     */
    public ClientSideGameModel() {
        listOfLobbies = new ArrayList<>();
        players = new ArrayList<>();
        tokens = new ArrayList<>();
        tokens.add(Token.BLUE);     tokens.add(Token.YELLOW);       tokens.add(Token.RED);      tokens.add(Token.GREEN);


        CardCreator cc = new CardCreator();

        resourceCards = new ArrayList<>();
        String filename = "/it/polimi/ingsw/am37/cards/ResourceCards.json";
        for (Card c: cc.createCards(filename, new TypeToken<ArrayList<ResourceCard>>() {}.getType()))
            resourceCards.add((ResourceCard) c);

        goldCards = new ArrayList<>();
         for (Card c: cc.createCards("/it/polimi/ingsw/am37/cards/GoldCards.json", new TypeToken<ArrayList<GoldCard>>() {}.getType()))
            goldCards.add((GoldCard) c);

        objectiveCards = new ArrayList<>();
         for (Card c: cc.createCards("/it/polimi/ingsw/am37/cards/ResourcesBoundObjectives.json", new TypeToken<ArrayList<ResourcesBoundObjective>>() {}.getType()))
            objectiveCards.add((ObjectiveCard) c);
         for (Card c: cc.createCards("/it/polimi/ingsw/am37/cards/LShapePlacement.json", new TypeToken<ArrayList<LShape>>() {}.getType()))
            objectiveCards.add((ObjectiveCard) c);
        for (Card c: cc.createCards("/it/polimi/ingsw/am37/cards/DiagUpPlacement.json", new TypeToken<ArrayList<DiagonalUp>>() {}.getType()))
            objectiveCards.add((ObjectiveCard) c);
        for (Card c: cc.createCards("/it/polimi/ingsw/am37/cards/DiagDownPlacement.json", new TypeToken<ArrayList<DiagonalDown>>() {}.getType()))
            objectiveCards.add((ObjectiveCard) c);

        startCards = new ArrayList<>();
         for (Card c: cc.createCards("/it/polimi/ingsw/am37/cards/StartCards.json", new TypeToken<ArrayList<StartCard>>() {}.getType()))
            startCards.add((StartCard) c);

        availableResourceCards = new ArrayList<>();
        availableGoldCards = new ArrayList<>();
        publicObjectives = new ArrayList<>();
    }

    /**
     * Sets the listener of the model.
     * @param listener the listener to set.
     */
    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    /**
     * Gets the listener of the model.
     * @return the listener of the model.
     */
    public PropertyChangeListener getListener() {
        return listener;
    }

    /**
     * Gets the list of all the resource cards in the game.
     * @return the list of all the resource cards in the game.
     */
    public List<ResourceCard> getResourceCards() {
        return resourceCards;
    }

    /**
     * Gets the list of all the gold cards in the game.
     * @return the list of all the gold cards in the game.
     */
    public List<GoldCard> getGoldCards() {
        return goldCards;
    }

    /**
     * Gets the list of all the objective cards in the game.
     * @return the list of all the objective cards in the game.
     */
    public List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    /**
     * Gets the list of all the start cards in the game.
     * @return the list of all the start cards in the game.
     */
    public List<StartCard> getStartCards() {
        return startCards;
    }

    /**
     * Gets the list of all the tokens in the game.
     * @return the list of all the tokens in the game.
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * removes a token from the list of tokens.
     * @param t the token to remove.
     */
    public void removeToken(Token t) {
        tokens.remove(t);
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "TOKEN_REMOVED",
                t,
                null);
        listener.propertyChange(evt);
    }

    //-------------------------------------------------------------------------------------------

    //HANDLE LOBBIES
    /**
     * Gets the list of all the lobbies.
     * @return the list of all the lobbies.
     */
    public List<Integer> getListOfLobbies() {
        return listOfLobbies;
    }

    /**
     * Sets the list of all the lobbies.
     * It notifies the listener that the list of lobbies has changed.
     * @param listOfLobbies the list of all the lobbies to set.
     */
    public void setListOfLobbies(List<Integer> listOfLobbies) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "LOBBIES",
                this.listOfLobbies,
                listOfLobbies);
        this.listener.propertyChange(evt);

        this.listOfLobbies = listOfLobbies;
    }

    /**
     * Gets the number of the lobby the player is in.
     * @return the number of the lobby the player is in.
     */
    public int getNumOfLobby() {
        return numOfLobby;
    }

    /**
     * Sets the number of the lobby the player is in.
     * @param numOfLobby the number of the lobby to set.
     */
    public void setNumOfLobby(int numOfLobby) {
        this.numOfLobby = numOfLobby;
    }

    /**
     * Gets the number of players in the lobby.
     * @return the number of players in the lobby.
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Sets the number of players in the lobby.
     * @param numOfPlayers the number of players to set.
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }



    //HANDLE PLAYERS
    /**
     * Gets the list of all the players in the game.
     * @return the list of all the players in the game.
     */
    public List<ClientSidePlayer> getPlayers() {
        return players;
    }

    /**
     * Adds a player to the list of players.
     * It notifies the listener that a new player has been added.
     * @param newPlayer the player to add.
     */
    public void addPlayer(ClientSidePlayer newPlayer) {
        players.add(newPlayer);

        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "NEW_PLAYER",
                null,
                newPlayer);
        this.listener.propertyChange(evt);
    }

    /**
     * Gets the player that is currently playing.
     * @return the player that is currently playing.
     */
    public ClientSidePlayer getMe() {
        return me;
    }

    /**
     * Sets the player that is currently playing.
     * @param me the player that is currently playing.
     */
    public void setMe(ClientSidePlayer me) {
        this.me = me;
    }

    //HANDLE AVAILABLE CARDS
    /**
     * Gets the list of all the available resource cards.
     * @return the list of all the available resource cards.
     */
    public List<StandardCard> getAvailableResourceCards() {
        return availableResourceCards;
    }

    /**
     * Sets the list of all the available resource cards.
     * It notifies the listener that the list of available resource cards has changed.
     * @param availableResourceCards the list of all the available resource cards to set.
     */
    public void setAvailableResourceCards(List<StandardCard> availableResourceCards) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_AVAILABLE",
                "r",
                availableResourceCards);

        this.availableResourceCards = availableResourceCards;

        this.listener.propertyChange(evt);
    }

    /**
     * Gets the list of all the available gold cards.
     * @return the list of all the available gold cards.
     */
    public List<StandardCard> getAvailableGoldCards() {
        return availableGoldCards;
    }

    /**
     * Sets the list of all the available gold cards.
     * It notifies the listener that the list of available gold cards has changed.
     * @param availableGoldCards the list of all the available gold cards to set.
     */
    public void setAvailableGoldCards(List<StandardCard> availableGoldCards) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_AVAILABLE",
                "g",
                availableGoldCards);

        this.availableGoldCards = availableGoldCards;

        this.listener.propertyChange(evt);
    }

    //HANDLE DECKS
    /**
     * Gets the resource card that is on top of the gold deck.
     * @return the resource card that is on top of the gold deck.
     */
    public Resource getTopOfGoldDeck() {
        return topOfGoldDeck;
    }

    /**
     * Sets the resource card that is on top of the gold deck.
     * It notifies the listener that the top of the gold deck has changed.
     * @param topOfGoldDeck the resource card that is on top of the gold deck.
     */
    public void setTopOfGoldDeck(Resource topOfGoldDeck) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_DECK",
                "g",
                topOfGoldDeck);

        this.topOfGoldDeck = topOfGoldDeck;

        this.listener.propertyChange(evt);
    }

    /**
     * Gets the resource card that is on top of the resource deck.
     * @return the resource card that is on top of the resource deck.
     */
    public Resource getTopOfResourceDeck() {
        return topOfResourceDeck;
    }

    /**
     * Sets the resource card that is on top of the resource deck.
     * It notifies the listener that the top of the resource deck has changed.
     * @param topOfResourceDeck the resource card that is on top of the resource deck.
     */
    public void setTopOfResourceDeck(Resource topOfResourceDeck) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_DECK",
                "r",
                topOfResourceDeck);


        this.topOfResourceDeck = topOfResourceDeck;

        this.listener.propertyChange(evt);
    }

    //HANDLE OBJECTIVES
    /**
     * Gets the list of all the public objectives in the game.
     * @return the list of all the public objectives in the game.
     */
    public List<ObjectiveCard> getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * Sets the list of all the public objectives in the game.
     * @param publicObjectives the list of all the public objectives to set.
     */
    public void setPublicObjectives(List<ObjectiveCard> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }

    /**
     * Gets the list of all the private objectives in the game.
     * @return the list of all the private objectives in the game.
     */
    public List<ObjectiveCard> getPrivateObjectives() {
        return privateObjectives;
    }

    /**
     * Sets the list of all the private objectives in the game.
     * @param privateObjectives the list of all the private objectives to set.
     */
    public void setPrivateObjectives(List<ObjectiveCard> privateObjectives) {
        this.privateObjectives = privateObjectives;
    }

    /**
     * Gets the private objective card that the player has chosen.
     * @return the private objective card that the player has chosen.
     */
    public ObjectiveCard getMyPrivateObjective() {
        return myPrivateObjective;
    }

    /**
     * Sets the private objective card that the player has chosen.
     * @param myPrivateObjective the private objective card that the player has chosen.
     */
    public void setMyPrivateObjective(ObjectiveCard myPrivateObjective) {
        this.myPrivateObjective = myPrivateObjective;
    }

    /**
     * Gets the start card that the player has chosen.
     * @return the start card that the player has chosen.
     */
    public StartCard getMyStartCard() {
        return myStartCard;
    }

    /**
     * Sets the start card that the player has chosen.
     * @param myStartCard the start card that the player has chosen.
     */
    public void setMyStartCard(StartCard myStartCard) {
        this.myStartCard = myStartCard;
    }

    /**
     * Gets the hand of the player.
     * @return the hand of the player.
     */
    public List<StandardCard> getMyHand() {
        return myHand;
    }

    /**
     * Sets the hand of the player.
     * @param myHand the hand of the player.
     */
    public void setMyHand(List<StandardCard> myHand) {
        this.myHand = myHand;
    }

    /**
     * Initializes the kingdom of the player.
     * It notifies the listener that the kingdom of the player has changed.
     * @param player the player that is initializing the kingdom.
     * @param placed the start card that the player has placed.
     * @param placedSide the side of the start card that the player has placed.
     */
    public void initializeKingdom(ClientSidePlayer player, StartCard placed, Side placedSide) {
        player.setKingdom(new Kingdom(placed, placedSide));
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "START_CARD_PLACED",
                null,
                placedSide);
        this.listener.propertyChange(evt);
    }

    /**
     * Places a card on the player's kingdom.
     * It notifies the listener that the kingdom has changed.
     * It removes the card from the player's hand.
     * It adds the points given by the card to the player.
     * It updates the player's kingdom.
     * It updates the linked sides of the corners of the placed side.
     * It updates the player's points.
     * It updates the player's kingdom.
     * @param player the player that is placing the card.
     * @param cardId the id of the card to place.
     * @param side the side of the card to place.
     * @param pos the position where to place the card.
     */
    public void placeCard(String player, int cardId, String side, Position pos) {
        StandardCard place = null;
        if(cardId >= 1 && cardId <= 40)
            for (StandardCard c: resourceCards) {
                if (c.getId() == cardId) {
                    place = c;
                    break;
                }
            }

        else if (cardId >= 41 && cardId <= 80)
            for (StandardCard c: goldCards)
                if (c.getId() == cardId) {
                    place = c;
                    break;
                }


        if (place!=null) {

            Side toPlace = null;
            if(side.equalsIgnoreCase("f"))
                toPlace = place.getFront();
            else if(side.equalsIgnoreCase("b"))
                toPlace = place.getBack();

            if(me.getNickname().equals(player) && toPlace != null) {

                int cornersLinked = 0;

                toPlace.placeInPosition(pos.getX(), pos.getY());
                for (Side s : me.getKingdom().getPlacedSides()) {
                    for (Direction d : Direction.values()) {
                        if (d.createPosition(s.getPositionInKingdom()).equals(pos)) {
                            s.getCorners().get(d).setLinkedSide(toPlace);
                            cornersLinked++;
                        }
                    }
                }

                if (side.equalsIgnoreCase("f"))
                    me.addPoints(place.getFront(), cornersLinked);

                me.getKingdom().updateKingdom(place, toPlace, pos);

                myHand.remove(place);


            } else if (toPlace != null) {
                for (ClientSidePlayer p: players) {
                    if (p.getNickname().equals(player)) {
                        int cornersLinked = 0;
                        toPlace.placeInPosition(pos.getX(), pos.getY());

                        for (Side s : p.getKingdom().getPlacedSides()) {
                            for (Direction d : Direction.values()) {
                                if (d.createPosition(s.getPositionInKingdom()).equals(pos)) {
                                    s.getCorners().get(d).setLinkedSide(toPlace);
                                    cornersLinked++;
                                }
                            }
                        }

                        if (side.equalsIgnoreCase("f"))
                            p.addPoints(place.getFront(), cornersLinked);

                        p.getKingdom().updateKingdom(place, toPlace, pos);

                        PropertyChangeEvent evt = new PropertyChangeEvent(
                                this,
                                "CHANGED_KINGDOM",
                                p,
                                place.getId());


                        this.listener.propertyChange(evt);

                        break;
                    }
                }
            }
        }
    }


    //HANDLE TURNS
    /**
     * Gets the list of players in order of turn.
     * @return the list of players in order of turn.
     */
    public List<String> getPlayersInOrder() {
        return playersInOrder;
    }

    /**
     * Sets the list of players in order of turn.
     * It sets the current player to the first player in the list.
     * It notifies the listener that a new turn has started.
     * @param playersInOrder the list of players in order of turn.
     */
    public void setPlayersInOrder(List<String> playersInOrder) {
        this.playersInOrder = playersInOrder;
        currentPlayer = playersInOrder.getFirst();
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "NEW_TURN",
                null,
                currentPlayer);

        listener.propertyChange(evt);
    }

    /**
     * Gets the nickname of the player that is currently playing.
     * @return the nickname of the player that is currently playing.
     */
    public String getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Changes the turn to the next player.
     * It notifies the listener that a new turn has started.
     * It sets the current player to the next player in the list.
     * If the current player is the last player in the list, it sets the current player to the first player in the list.
     */
    public void nextTurn() {
        int currIdx = playersInOrder.indexOf(currentPlayer);
        if (currIdx + 1 < playersInOrder.size()) {
            currentPlayer = playersInOrder.get(currIdx + 1);
        } else {
            currentPlayer = playersInOrder.getFirst();
        }

        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "NEW_TURN",
                currentPlayer,
                currentPlayer);

        listener.propertyChange(evt);
    }
}
