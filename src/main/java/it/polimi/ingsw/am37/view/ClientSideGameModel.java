package it.polimi.ingsw.am37.view;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.am37.model.cards.Card;
import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.game.Resource;

import java.util.ArrayList;
import java.util.List;

public class ClientSideGameModel {
    private List<Integer> listOfLobbies;
    private int numOfLobby;
    private int numOfPlayers;
    private ClientSidePlayer me;
    private List<ObjectiveCard> privateObjectives;
    private StartCard myStartCard;
    private ObjectiveCard myPrivateObjective;
    private List<StandardCard> myHand;
    private final List<ClientSidePlayer> players;

    private final List<ResourceCard> resourceCards;
    private final List<GoldCard> goldCards;
    private final List<ObjectiveCard> objectiveCards;
    private final List<StartCard> startCards;

    private List<StandardCard> availableResourceCards;
    private List<StandardCard> availableGoldCards;
    private Resource topOfGoldDeck;
    private Resource topOfResourceDeck;
    private List<ObjectiveCard> publicObjectives;

    public ClientSideGameModel() {
        listOfLobbies = new ArrayList<>();
        players = new ArrayList<>();


        CardCreator cc = new CardCreator();

        resourceCards = new ArrayList<>();          //INPUT READER IS NULL WHY?
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

    //-------------------------------------------------------------------------------------------


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

    public List<StandardCard> getAvailableResourceCards() {
        return availableResourceCards;
    }

    public void setAvailableResourceCards(List<StandardCard> availableResourceCards) {
        this.availableResourceCards = availableResourceCards;
    }

    public List<StandardCard> getAvailableGoldCards() {
        return availableGoldCards;
    }

    public void setAvailableGoldCards(List<StandardCard> availableGoldCards) {
        this.availableGoldCards = availableGoldCards;
    }

    public Resource getTopOfGoldDeck() {
        return topOfGoldDeck;
    }

    public void setTopOfGoldDeck(Resource topOfGoldDeck) {
        this.topOfGoldDeck = topOfGoldDeck;
    }

    public Resource getTopOfResourceDeck() {
        return topOfResourceDeck;
    }

    public void setTopOfResourceDeck(Resource topOfResourceDeck) {
        this.topOfResourceDeck = topOfResourceDeck;
    }

    public List<ObjectiveCard> getPublicObjectives() {
        return publicObjectives;
    }

    public void setPublicObjectives(List<ObjectiveCard> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }


    public StartCard getMyStartCard() {
        return myStartCard;
    }

    public void setMyStartCard(StartCard myStartCard) {
        this.myStartCard = myStartCard;
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

    public List<StandardCard> getMyHand() {
        return myHand;
    }

    public void setMyHand(List<StandardCard> myHand) {
        this.myHand = myHand;
    }
}
