package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.player.Player;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * The GameModel class contains all the information and behaviour of an instance of the game.
 */
public class GameModel {
    /**
     * The currentPhase attribute gives information about the current phase of the game.
     */
    private GamePhase currentPhase;
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

        scoreboard = new Scoreboard(participantsInOrder);
        disconnectedPlayers = new ArrayList<>();

        for (Player p : participantsInOrder) {
            try {
                p.setGame(this);
            } catch (AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
        }

        CardCreator cardCreator = new CardCreator();
        sDeck = new StartDeck(cardCreator);
        gDeck = new GoldDeck(cardCreator);
        rDeck = new ResourceDeck(cardCreator);
        oDeck = new ObjectiveDeck(cardCreator);

        setCurrentPhase(GamePhase.PREPARATION);
    }

    /**
     * The getCurrentPhase() method returns the current phase of the game.
     * @return The value of the currentPhase attribute.
     */
    public GamePhase getCurrentPhase() {
        return currentPhase;
    }

    /**
     * The setCurrentPhase(currentPhase) method updtates the value of the currentPhase attribute.
     * @param currentPhase A value of the GamePhase enumeration.
     */
    public void setCurrentPhase(GamePhase currentPhase) {       //private?
        this.currentPhase = currentPhase;
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
     * The preparationPhase() method sets up the game as described in the rulebook. First it sets the resource and
     * gold cards available to everyone to draw. Then it gives all player a start card and lets them choose
     * (concurrently with threads) which Side they want to place down. Once everyone responds it creates everyone's hand
     * and then sets the public objectives. Finally, it gives the Players' two objective cards, it lets them choose
     * which one they want to keep (concurrently with threads) and once everyone is done it sets the current phase to
     * PLAYING.
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied, and the thread is
     *                              interrupted, either before or during the activity. Occasionally a method may wish to
     *                              test whether the current thread has been interrupted, and if so, to immediately throw
     *                              this exception.
     * @throws NoCardsException Thrown when the list of cards in the deck is empty, so that the user knows he will not
     *                          be able to draw again from this deck.
     * @throws AlreadyAssignedException Thrown when trying to assign an attribute that has already been assigned and
     *                                  can only be assigned once.
     */
    public void preparationPhase() throws InterruptedException, NoCardsException, AlreadyAssignedException {
        setAvailableCards();

        //ExecutorService s = Executors.newFixedThreadPool(4);
        for (Player p : participantsInOrder) {
           /*s.submit(() -> {
               try {*/
                   giveStartCard(p);
               /*} catch (NoCardsException | AlreadyAssignedException e) {
                   throw new RuntimeException(e);
               }
           });*/
        }
        /*s.shutdown();
        while(!s.awaitTermination(1, TimeUnit.SECONDS));
        s.close();*/

        for (Player p : participantsInOrder) {
            createHand(p);
        }

        setPublicObjectives();

        //ExecutorService s1 = Executors.newFixedThreadPool(4);
        for (Player p : participantsInOrder) {
            /*s1.submit(() -> {
                try {*/
                    giveObjectiveCards(p);
                /*} catch (NoCardsException | AlreadyAssignedException e) {
                    throw new RuntimeException(e);
                }
            });*/
        }
        /*s1.shutdown();
        while(!s1.awaitTermination(1, TimeUnit.SECONDS));*/

        setCurrentPhase(GamePhase.PLAYING);
    }

    private void setAvailableCards() throws NoCardsException {
        availableRCards = new ArrayList<>();
        availableGCards = new ArrayList<>();
        while(getAvailableGCards().size() < 2)
            availableGCards.add((GoldCard) gDeck.drawCard());

        while(getAvailableRCards().size() < 2)
            availableRCards.add((ResourceCard) rDeck.drawCard());
    }

    private void setPublicObjectives() throws NoCardsException {
        publicObjectives = new ObjectiveCard[2];
        publicObjectives[0] = (ObjectiveCard) oDeck.drawCard();
        publicObjectives[1] = (ObjectiveCard) oDeck.drawCard();
    }
    private void createHand(Player p) throws NoCardsException, AlreadyAssignedException {
        List<StandardCard> hand = new ArrayList<>();
        hand.add((StandardCard) rDeck.drawCard());
        hand.add((StandardCard) rDeck.drawCard());
        hand.add((StandardCard) gDeck.drawCard());
        p.setHand(hand);
    }

    private void giveStartCard(Player p) throws NoCardsException, AlreadyAssignedException {
        p.setStartCard((StartCard) sDeck.drawCard());
        p.chooseStartCardSide();
    }

    private void giveObjectiveCards(Player p) throws NoCardsException, AlreadyAssignedException {
        ObjectiveCard[] twoObjCards = new ObjectiveCard[2];
        twoObjCards[0] = (ObjectiveCard) oDeck.drawCard();
        twoObjCards[1] = (ObjectiveCard) oDeck.drawCard();
        p.chooseObjective(twoObjCards);
    }

    /**
     * The playingPhase() method handles the turn sequence and waits for the player to place and draw Cards. Once one
     * of the player reaches 20 points, the method set the currentPhase to ENDGAME.
     */
    public void playingPhase() {
        //TODO
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
            currentTurn = participantsInOrder.get(0);
        }
    }

    /**
     * The endGamePhase() method is called after one of the player has reached 20 points and handles the last turns
     * of the game. It calculates which turn will be the last and once we have reached that turn, it will terminate the
     * game by calculating everyone's final score by verifying if they completed any of the objectives. At last, it will
     * show the results of the game and then set the currentPhase to FINISHED.
     */
    public void endGamePhase() {
        //TODO
    }

    /**
     * The getGameWinner() method calculates everyone's final points and then returns the final scoreboard.
     * @return The final Scoreboard of the game.
     */
    public Hashtable<Player, Integer> getGameWinner() {
        //TODO
        return null;
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
