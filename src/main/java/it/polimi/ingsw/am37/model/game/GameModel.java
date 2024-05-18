package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.player.Player;

import java.util.*;

/**
 * The GameModel class contains all the information and behaviour of an instance of the game.
 */
public class GameModel {
    /**
     * The currentStatus attribute gives information about the current state of the model.
     */
    private GameStatus currentStatus;
    /**
     * The participantsInOrder attribute is a list of all the participants in the order of their turns.
     */
    private final List<Player> participantsInOrder;
    /**
     * The scoreboard attribute is a reference to the scoreboard of the game.
     */
    private final Scoreboard scoreboard;
    /**
     * The gDeck attribute contains the Deck of Gold Cards that is used during the game.
     */
    private final GoldDeck gDeck;
    /**
     * The rDeck attribute contains the Deck of Resource Cards that is used during the game.
     */
    private final ResourceDeck rDeck;
    /**
     * The sDeck attribute contains the Deck of Start Cards that is used during the game.
     */
    private final StartDeck sDeck;
    /**
     * The oDeck attribute contains the Deck of Objective Cards that is used during the game.
     */
    private final ObjectiveDeck oDeck;
    /**
     * The availableRCards attribute is a list of exactly 2 Resource Card that are visible to all players and can be
     * drawn instead of drawing from a deck. If a player draws from the list, a new Resource Card is added to it.
     */
    private List<StandardCard> availableRCards;
    /**
     * The availableGCards attribute is a list of exactly 2 Gold Card that are visible to all players and can be
     * drawn instead of drawing from a deck. If a player draws from the list, a new Gold Card is added to it.
     */
    private List<StandardCard> availableGCards;
    /**
     * The publicObjectives attribute is an array of exactly 2 Objective Card that contains the public objectives that
     * anyone can complete in order to get points.
     */
    private ObjectiveCard[] publicObjectives;
    /**
     * The currentTurn attribute indicates the player that is currently in his turn.
     */
    private Player currentTurn;
    /**
     * The turnCounter attribute is the number of turns from the beginning of the game. It is used in the endgame phase
     * to calculate and verify if we have reached the last turn.
     */
    private int turnCounter;
    /**
     * The lastTurn attribute is the number of the final turn that will be calculated at the beginning of the endgame
     * phase.
     */
    private int lastTurn;
    /**
     * The disconnectedPlayer attribute is a list of Player that are currently disconnected from the game.
     */
    private final List<Player> disconnectedPlayers;

    /**
     * The GameModel(participantsInOrder) constructor sets the participantsInOrder attribute to the list of player
     * given as a parameter. Then it initializes the scoreboard and creates an empty list that will contain the
     * disconnected player. Finally, it creates the deck that will be used during the game and sets the current
     * phase to preparation.
     * @param participantsInOrder A list of 2 to 4 player that are the participants of this instance of the game.
     */
    public GameModel(List<Player> participantsInOrder) {
        this.participantsInOrder = participantsInOrder;

        disconnectedPlayers = new ArrayList<>();

        for (Player p : participantsInOrder) {
            try {
                p.setGame(this);
            } catch (AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
        }
        scoreboard = new Scoreboard(participantsInOrder);

        CardCreator cardCreator = new CardCreator();
        sDeck = new StartDeck(cardCreator);
        gDeck = new GoldDeck(cardCreator);
        rDeck = new ResourceDeck(cardCreator);
        oDeck = new ObjectiveDeck(cardCreator);
    }

    public GameStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(GameStatus currentStatus) {
        this.currentStatus = currentStatus;
    }

    /**
     * The getParticipants() method returns the list of Player that participates in the game.
     * @return The participantsInOrder attribute.
     */
    public List<Player> getParticipants() {
        return participantsInOrder;
    }

    /**
     * The getScoreboard() method returns the scoreboard of the game.
     * @return The scoreboard attribute.
     */
    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    /**
     * The getGDeck() method returns the instance of the Gold Deck that is used during the game.
     * @return The gDeck attribute.
     */
    public GoldDeck getGDeck() {
        return gDeck;
    }

    /**
     * The getRDeck() method returns the instance of the Resource Deck that is used during the game.
     * @return The rDeck attribute.
     */
    public ResourceDeck getRDeck() {
        return rDeck;
    }

    /**
     * The getSDeck() method returns the instance of the Start Deck that is used during the game.
     * @return The sDeck attribute.
     */
    public StartDeck getSDeck() {
        return sDeck;
    }

    /**
     * The getODeck() method returns the instance of the Objective Deck that is used during the game.
     * @return The oDeck attribute.
     */
    public ObjectiveDeck getODeck() {
        return oDeck;
    }

    /**
     * The getAvailableRCards() method returns the list of currently available ResourceCards.
     * @return The availableRCards attribute.
     */
    public List<StandardCard> getAvailableRCards() {
        return availableRCards;
    }

    /**
     * The getAvailableGCards() method returns the list of currently available GoldCards.
     * @return The availableGCards attribute.
     */
    public List<StandardCard> getAvailableGCards() {
        return availableGCards;
    }

    /**
     * The getPublicObjectives() method returns the array of public objectives that anyone can fulfill to get points.
     * @return The publicObjectives attribute.
     */
    public ObjectiveCard[] getPublicObjectives() {
        return publicObjectives;
    }

    /**
     * The method setAvailableCards sets up the list of available and cards that the player is able to see and can draw
     * when in his turn.
     * @throws NoCardsException if the deck ran out of cards.
     */
    public void setAvailableCards() throws NoCardsException {
        availableRCards = new ArrayList<>();
        availableGCards = new ArrayList<>();
        while(getAvailableGCards().size() < 2)
            availableGCards.add(gDeck.drawCard());

        while(getAvailableRCards().size() < 2)
            availableRCards.add(rDeck.drawCard());
    }

    /**
     * the method giveStartCard gives the player the choice of the first card he's going to place, the starting card.
     * @throws NoCardsException if the deck ran out of cards.
     * @throws AlreadyAssignedException if the player has already had his starting card.
     */
    public void giveStartCard() throws NoCardsException, AlreadyAssignedException {
        for (Player p: participantsInOrder) {
            p.setStartCard(sDeck.drawCard());
        }
    }

    /**
     * The method createHand sets the starting cards the players will have in their hand right after placing the start
     * card.
     * @throws NoCardsException if the deck ran out of cards.
     * @throws AlreadyAssignedException if the player that invoked this method already has had his hand set up.
     */
    public void createHand() throws NoCardsException, AlreadyAssignedException {
        for (Player p: participantsInOrder) {
            List<StandardCard> hand = new ArrayList<>();
            hand.add(rDeck.drawCard());
            hand.add(rDeck.drawCard());
            hand.add(gDeck.drawCard());
            p.setHand(hand);
        }
    }

    /**
     * The method setPublicObjectives sets up the objectives the players have in common. All the players can see these
     * objectives.
     * @throws NoCardsException if the objectives deck ran out of cards.
     */
    public void setPublicObjectives() throws NoCardsException {
        publicObjectives = new ObjectiveCard[2];
        publicObjectives[0] = oDeck.drawCard();
        publicObjectives[1] = oDeck.drawCard();
    }

    /**
     * the method giveObjectiveCards gives each player the choice of their personal objectives. Every player will be
     * able to see only their personal two objectives.
     * @throws NoCardsException if the deck ran out of cards.
     * @throws AlreadyAssignedException if the private objectives have already been assigned.
     */
    public void giveObjectiveCards() throws NoCardsException, AlreadyAssignedException {
        ObjectiveCard[] twoObjCards = new ObjectiveCard[2];
        for (Player p: participantsInOrder) {
            twoObjCards[0] = oDeck.drawCard();
            twoObjCards[1] = oDeck.drawCard();
            p.setObjectivesToChooseFrom(twoObjCards);
        }
    }

    public void setupPlayPhase() {
        turnCounter = 1;
        Collections.shuffle(participantsInOrder);
        currentTurn = participantsInOrder.getFirst();
    }

    /**
     * The getCurrentTurn() method returns the Player who is playing his turn right now.
     * @return The Player that is playing his turn.
     */
    public Player getCurrentTurn() {
        return currentTurn;
    }

    /**
     * The getTurnCounter() method returns the current turn number.
     * @return The turnCounter attribute.
     */
    public int getTurnCounter() {
        return turnCounter;
    }

    /**
     * The nextTurn() method updates the value of the currentTurn attribute, that contains the Player that is currently
     * playing his turn.
     */
    public void nextTurn() {
        int currIdx = participantsInOrder.indexOf(currentTurn);
        turnCounter++;
        if (currIdx + 1 < participantsInOrder.size()) {
            currentTurn = participantsInOrder.get(currIdx + 1);
        } else {
            currentTurn = participantsInOrder.getFirst();
        }
    }

    public void setupEndGame() {
        lastTurn = turnCounter + (participantsInOrder.size() - participantsInOrder.indexOf(currentTurn) - 1) + participantsInOrder.size();
    }

    public int getLastTurn() {
        return lastTurn;
    }

    /**
     * The getGameWinner() method calculates everyone's final points and then returns the final scoreboard.
     * @return The final Scoreboard of the game.
     */
    public PlayerPoints[] getGameWinner(){
        PlayerPoints[] finalPoints= new PlayerPoints[participantsInOrder.size()];
        Hashtable<Player, Integer> playerPoints = scoreboard.getParticipantsPoints();
        int i=0;
        int j;
        int points;
        int compl;
        PlayerPoints temp;


        for(Player p:participantsInOrder){
            points=playerPoints.get(p);

            int compl1 = publicObjectives[0].calculateNumOfCompletion(p.getMyKingdom());
            int compl2 = publicObjectives[1].calculateNumOfCompletion(p.getMyKingdom());
            int compl3 = p.getPrivateObjective().calculateNumOfCompletion(p.getMyKingdom());

            points = points + compl1 * publicObjectives[0].getPointsGiven() + compl2 * publicObjectives[1].getPointsGiven()
                                                                        + compl3 * p.getPrivateObjective().getPointsGiven();

            compl = compl1 + compl2 + compl3;

            finalPoints[i]=new PlayerPoints(p, points, compl);
            for(j=i-1; j>=0; j-- ){
                if(finalPoints[j].getPoints() < points){
                    temp=finalPoints[j];
                    finalPoints[j]=finalPoints[j+1];
                    finalPoints[j+1]=temp;
                }
                else if(finalPoints[j].getPoints() == points){
                    if(compl>finalPoints[j].getNumOfCompletion()){
                        temp=finalPoints[j];
                        finalPoints[j]=finalPoints[j+1];
                        finalPoints[j+1]=temp;
                    }
                }
            }
            i++;
        }
        return finalPoints;
    }

    /**
     * The getDisconnectedPlayers() method returns the list of player that are currently disconnected from the game.
     * @return The disconnectedPlayers attribute.
     */
    public List<Player> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }

    /**
     * The setDisconnected(p) method adds the Player p given as a parameter to the list of disconnectedPlayers.
     * @param p The Player that has to be added to the disconnectedPlayers.
     */
    public void setDisconnected(Player p) {
        disconnectedPlayers.add(p);
        p.setDisconnected(true);
    }

    /**
     * The reconnect(p) method removes the Player given as a parameter from the disconnectedPlayers list and sets his
     * isDisconnected attribute to false.
     * @param p The Player that has to be removed from the disconnectedPlayers.
     */
    public void reconnect(Player p) {
        disconnectedPlayers.remove(p);
        p.setDisconnected(false);
    }
}
