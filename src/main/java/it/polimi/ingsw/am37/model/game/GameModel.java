package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.player.Player;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The GameModel class contains all the information and behaviour of an instance of the game.
 */
public class GameModel {
    private GamePhase currentPhase;
    private final List<Player> participantsInOrder;
    private final Scoreboard scoreboard;
    private CardCreator cardCreator;
    private GoldDeck gDeck;
    private ResourceDeck rDeck;
    private StartDeck sDeck;
    private ObjectiveDeck oDeck;
    private List<Card> availableRCards;
    private List<Card> availableGCards;
    private ObjectiveCard[] publicObjectives;
    private Player currentTurn;
    private int turnCounter;
    private int lastTurn;
    private final List<Player> disconnectedPlayers;

    /**
     * The GameModel(participantsInOrder) constructor sets the participantsInOrder attribute to the list of player
     * given as a parameter. Then it initializes the scoreboard and creates an empty list that will contain the
     * disconnected player.
     * @param participantsInOrder A list of 2 to 4 player that are the participants of this instance of the game.
     */
    public GameModel(List<Player> participantsInOrder) {
        this.participantsInOrder = participantsInOrder;

        scoreboard = new Scoreboard(participantsInOrder);
        disconnectedPlayers = new ArrayList<>();
    }

    /**
     * The getCurrentPhase() method returns the current phase of the game.
     * @return The value of the currentPhase attribute.
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(GamePhase currentPhase) {       //private?
        this.currentPhase = currentPhase;
    }

    public List<Player> getParticipants() {
        return participantsInOrder;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public GoldDeck getGDeck() {
        return gDeck;
    }

    public ResourceDeck getRDeck() {
        return rDeck;
    }

    public StartDeck getSDeck() {
        return sDeck;
    }

    public ObjectiveDeck getODeck() {
        return oDeck;
    }

    public List<Card> getAvailableRCards() {
        return availableRCards;
    }

    public List<Card> getAvailableGCards() {
        return availableGCards;
    }

    public ObjectiveCard[] getPublicObjectives() {
        return publicObjectives;
    }

    public void preparationPhase() {
        //TODO
    }

    private void setAvailableCards() {
        //TODO (sia gold che resource nello stesso)
    }

    private void setPublicObjectives() {
        //TODO
    }

    private void createHand(Player p) {
        //TODO
    }

    private void giveStartCard(Player p) {
        //TODO
    }

    private void giveObjectiveCards(Player p) {
        //TODO
    }

    public void playingPhase() {
        //TODO
    }

    public Player getCurrentTurn() {
        return currentTurn;
    }

    public int getTurnCounter() {
        return turnCounter;
    }

    public void nextTurn() {
        int currIdx = participantsInOrder.indexOf(currentTurn);
        turnCounter++;
        if (currIdx + 1 < participantsInOrder.size()) {
            currentTurn = participantsInOrder.get(currIdx + 1);
        } else {
            currentTurn = participantsInOrder.get(0);
        }
    }

    public void endGamePhase() {
        //TODO
    }

    public Hashtable<Player, Integer> getGameWinner() {
        //TODO
        return null;
    }

    public List<Player> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }
}
