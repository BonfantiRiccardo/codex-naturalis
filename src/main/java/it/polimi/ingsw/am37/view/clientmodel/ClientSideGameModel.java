package it.polimi.ingsw.am37.view.clientmodel;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

public class ClientSideGameModel {
    PropertyChangeListener listener;
    private final List<ResourceCard> resourceCards;
    private final List<GoldCard> goldCards;
    private final List<ObjectiveCard> objectiveCards;
    private final List<StartCard> startCards;
    private final List<Token> tokens;

    private List<Integer> listOfLobbies;
    private int numOfLobby;
    private int numOfPlayers;

    private ClientSidePlayer me;
    private List<ObjectiveCard> privateObjectives;
    private StartCard myStartCard;
    private ObjectiveCard myPrivateObjective;
    private List<StandardCard> myHand;
    private final List<ClientSidePlayer> players;

    private List<StandardCard> availableResourceCards;
    private List<StandardCard> availableGoldCards;
    private Resource topOfGoldDeck;
    private Resource topOfResourceDeck;
    private List<ObjectiveCard> publicObjectives;

    private List<String> playersInOrder;
    private String currentPlayer;

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

    public void setListener(PropertyChangeListener listener) {
        this.listener = listener;
    }

    public List<ResourceCard> getResourceCards() {
        return resourceCards;
    }

    public List<GoldCard> getGoldCards() {
        return goldCards;
    }

    public List<ObjectiveCard> getObjectiveCards() {
        return objectiveCards;
    }

    public List<StartCard> getStartCards() {
        return startCards;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public void removeToken(Token t) {
        tokens.remove(t);
    }

    //-------------------------------------------------------------------------------------------

    //HANDLE LOBBIES
    public List<Integer> getListOfLobbies() {
        return listOfLobbies;
    }

    public void setListOfLobbies(List<Integer> listOfLobbies) {
        this.listOfLobbies = listOfLobbies;
    }

    public int getNumOfLobby() {
        return numOfLobby;
    }

    public void setNumOfLobby(int numOfLobby) {
        this.numOfLobby = numOfLobby;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }



    //HANDLE PLAYERS
    public List<ClientSidePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(ClientSidePlayer newPlayer) {
        players.add(newPlayer);
    }

    public ClientSidePlayer getMe() {
        return me;
    }

    public void setMe(ClientSidePlayer me) {
        this.me = me;
    }

    //HANDLE AVAILABLE CARDS
    public List<StandardCard> getAvailableResourceCards() {
        return availableResourceCards;
    }

    public void setAvailableResourceCards(List<StandardCard> availableResourceCards) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_AVAILABLE",
                this.availableResourceCards,
                availableResourceCards);

        this.availableResourceCards = availableResourceCards;

        this.listener.propertyChange(evt);
    }

    public List<StandardCard> getAvailableGoldCards() {
        return availableGoldCards;
    }

    public void setAvailableGoldCards(List<StandardCard> availableGoldCards) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_AVAILABLE",
                this.availableGoldCards,
                availableGoldCards);

        this.availableGoldCards = availableGoldCards;

        this.listener.propertyChange(evt);
    }

    //HANDLE DECKS
    public Resource getTopOfGoldDeck() {
        return topOfGoldDeck;
    }

    public void setTopOfGoldDeck(Resource topOfGoldDeck) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_DECK",
                this.topOfGoldDeck,
                topOfGoldDeck);

        this.topOfGoldDeck = topOfGoldDeck;

        this.listener.propertyChange(evt);
    }

    public Resource getTopOfResourceDeck() {
        return topOfResourceDeck;
    }

    public void setTopOfResourceDeck(Resource topOfResourceDeck) {
        PropertyChangeEvent evt = new PropertyChangeEvent(
                this,
                "CHANGED_DECK",
                this.topOfResourceDeck,
                topOfResourceDeck);


        this.topOfResourceDeck = topOfResourceDeck;

        this.listener.propertyChange(evt);
    }

    //HANDLE OBJECTIVES
    public List<ObjectiveCard> getPublicObjectives() {
        return publicObjectives;
    }

    public void setPublicObjectives(List<ObjectiveCard> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }

    public List<ObjectiveCard> getPrivateObjectives() {
        return privateObjectives;
    }

    public void setPrivateObjectives(List<ObjectiveCard> privateObjectives) {
        this.privateObjectives = privateObjectives;
    }

    public ObjectiveCard getMyPrivateObjective() {
        return myPrivateObjective;
    }

    public void setMyPrivateObjective(ObjectiveCard myPrivateObjective) {
        this.myPrivateObjective = myPrivateObjective;
    }

    //HANDLE START CARD
    public StartCard getMyStartCard() {
        return myStartCard;
    }

    public void setMyStartCard(StartCard myStartCard) {
        this.myStartCard = myStartCard;
    }

    //HANDLE HAND
    public List<StandardCard> getMyHand() {
        return myHand;
    }

    public void setMyHand(List<StandardCard> myHand) {
        this.myHand = myHand;
    }

    //HANDLE PLACING
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
                                p.getKingdom(),
                                p.getKingdom());


                        this.listener.propertyChange(evt);

                        break;
                    }
                }
            }
        }
    }


    //HANDLE TURNS
    public List<String> getPlayersInOrder() {
        return playersInOrder;
    }

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

    public String getCurrentPlayer() {
        return currentPlayer;
    }

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
