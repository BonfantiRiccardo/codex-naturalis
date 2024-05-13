package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.controller.states.*;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class GameController's duty is to manage the phases of the game and to give directions to the model, maintaining the view updated.
 */
public class GameController implements Observable {
    /**
     * the attribute participants is the list of players who will take part in the game
     */
    private final List<Player> participants;
    /**
     * the numOfPlayers attribute is an Integer number which memorize the number of the players playing.
     */
    private final int numOfPlayers;
    /**
     * the isGameStarted attribute is a boolean which becomes true when the game is started.
     */
    private boolean isGameStarted;
    /**
     * the endGameStarted attribute is a boolean which becomes true when the game enters the final phase.
     */
    private boolean endGameStarted;
    /**
     * the gameInstance attribute is the instance of the game created.
     */
    private GameModel gameInstance;
    /**
     * the playerViews attribute is a Map that we use to keep track of each player's VirtualView
     */
    private final Map<Player, VirtualView> playerViews;
    /**
     * the state attribute gives the information about the actual state of the game
     */
    private State state;

    /**
     * the method GameController sets the game to be started adding the new players and redirecting them to the new lobby.
     * @param creator is the player who created the game.
     * @param numOfPlayers is the number of players participating.
     */
    public GameController(Player creator, int numOfPlayers) {
        isGameStarted = false;
        endGameStarted = false;

        participants = new ArrayList<>();
        participants.add(creator);
        playerViews = new HashMap<>();

        this.numOfPlayers = numOfPlayers;       //BEFORE WE  CHECK THAT IS BETWEEN 0 AND 4 IN THE DE-CODIFICATION.

        state = new LobbyState(this);
    }

    /**
     * the method getAddedPlayers returns the list of players who got added in the game.
     * @return the participants attribute.
     */
    public List<Player> getAddedPlayers() {
        return participants;
    }

    /**
     * the method getNumOfPlayers returns the number of players participating in the game.
     * @return the numberOfPlayers attribute.
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * the getGameInstance method returns the instance of the game created.
     * @return gameInstance attribute.
     */
    public GameModel getGameInstance() {
        return gameInstance;
    }

    /**
     * the setGameInstance method sets the model as the instance of the game created.
     * @param model is the instance given to the game.
     * @throws AlreadyAssignedException if the has already been assigned to a model.
     */
    public void setGameInstance(GameModel model) throws AlreadyAssignedException {
        if (this.gameInstance != null)
            throw new AlreadyAssignedException("The game has already been created");
        else
            this.gameInstance = model;
    }

    /**
     * the method getState returns the actual state of the game.
     * @return the state attribute.
     */
    public State getState() {
        return state;
    }

    /**
     * the setState attribute sets the actual state of the game passed by parameter.
     * @param state is the updated state.
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * the isGameStarted method returns true if the game started, or returns false otherwise.
     * @return isGameStarted attribute.
     */
    public boolean isGameStarted() {
        return isGameStarted;
    }

    /**
     * the method setGameStarted set the attribute isGameStarted to true if the game is started and instanced.
     * @param gameStarted is the boolean used to pass the information.
     */
    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    /**
     * the isEndGameStarted method returns true if the game is in the final phase, or returns false otherwise.
     * @return endGameStarted attribute.
     */
    public boolean isEndGameStarted() {
        return endGameStarted;
    }

    /**
     * the method setEndGameStarted set the attribute endGameStarted to true if the game has entered the final phase.
     * @param endGameStarted is the boolean used to pass the information.
     */
    public void setEndGameStarted(boolean endGameStarted) {
        this.endGameStarted = endGameStarted;
    }

    /**
     * the method getPlayerViews returns the Virtual View of each player.
     * @return the PlayerViews attribute.
     */
    public Map<Player, VirtualView> getPlayerViews() {
        return playerViews;
    }

    public void setVirtualView(Player p, VirtualView vv) { playerViews.put(p, vv); }


    //------------------------------------------------------------------------------------------------

    /**
     * the addPlayer method adds a new player to the game before instancing the game, linking to each of them a VirtualView everytime a player is added,
     * proceeds then to call gamePhaseHandler which will set up the players.
     * @param newPlayer is the player who requested to join the game.
     * @throws IncorrectUserActionException if the nickname the player chose was already taken or if the game is full.
     * @throws WrongGamePhaseException if the game has already started.
     * @throws NoCardsException if there's no cards left.
     * @throws AlreadyAssignedException if the objectives cards were already given to someone else?
     */
    public synchronized void addPlayer (Player newPlayer) throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        if (gameInstance == null) {
            if (participants.size() < numOfPlayers) {
                for (Player p: participants) {
                    if (newPlayer.getNickname().equals(p.getNickname()))
                        throw new IncorrectUserActionException("This username is already in use.");
                }

                participants.add(newPlayer);
                state.gamePhaseHandler();

            } else
                throw new IncorrectUserActionException("The game is full.");
        } else throw new WrongGamePhaseException("This game has started.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the method PlayerChoosesStartCardSide makes the player choose the side of his own starting card, then initiate the Player's Kingdom, calls
     * the gamePhaseHandler to proceed in the player's setup and updates the player's view.
     * @param p is the player who's choosing the starting card.
     * @param c is the starting card assigned to the player.
     * @param s is the side of the card the player chose.
     * @throws IncorrectUserActionException if the player tries to place another player's starting card.
     * @throws WrongGamePhaseException if the players tries to place the starting card in the wrong phase of the game.
     * @throws NoCardsException if there's no cards left.
     * @throws AlreadyAssignedException if the card was already assigned.
     */
    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_START_CARD_SIDE)) {
            if (!p.getStartCard().equals(c))
                throw new IncorrectUserActionException("The start card you want to place is not the one assigned to you.");
            else if (p.getStartCard().getFront().equals(s) || p.getStartCard().getBack().equals(s)) {
                p.instantiateMyKingdom(c, s);
                state.gamePhaseHandler();

            } else throw new IncorrectUserActionException("The side you want to place does not correspond to the one of your start card.");
        } else throw new WrongGamePhaseException("You cannot place your start card now.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the method playerChoosesToken checks if the player already has a token, if not, it makes the player choose a new one.
     * @param p is the player who has to choose the token.
     * @param t is the token chosen by the player.
     * @throws AlreadyAssignedException if the player has already chosen a token, or already has received a hand or the objectives cards.
     * @throws IncorrectUserActionException if the player chooses the black token or an already taken one.
     * @throws WrongGamePhaseException if the player tries to choose the token in the wrong phase of the game.
     * @throws NoCardsException if there's no cards to create a hand or to give an objective or to set the public objectives in the gameHandler.
     */
    public void playerChoosesToken (Player p, Token t) throws AlreadyAssignedException, IncorrectUserActionException, WrongGamePhaseException, NoCardsException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_TOKEN)) {
            boolean check = true;
            for (Player pl: gameInstance.getParticipants()) {
                if (pl.getToken() != null && pl.getToken().equals(t)) {
                    check = false;
                    break;
                }
            }
            if (t.equals(Token.BLACK))
                throw new IncorrectUserActionException("You can't choose the black token.");
            else if (check) {
                p.setToken(t);
                state.gamePhaseHandler();

            } else
                throw new IncorrectUserActionException("The token has been chosen by another player.");
        } else throw new WrongGamePhaseException("You cannot choose your token now.");

    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the method playerChooseObjective checks if the player has already chosen his private objectives, if not, it gives the player
     * 2 objectives and makes the player choose one, it updates the player's virtual view then.
     * @param p is the player who's choosing the objectives.
     * @param c is the objective the player chose.
     * @throws AlreadyAssignedException if the player has already chosen an objective.
     * @throws WrongGamePhaseException if the player tries to choose the objective in the wrong phase of the game.
     * @throws IncorrectUserActionException if the player tries to choose another player's objective.
     */
    public void playerChoosesObjective(Player p, ObjectiveCard c) throws AlreadyAssignedException, WrongGamePhaseException, IncorrectUserActionException, NoCardsException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_OBJECTIVE)) {
            if (p.getPrivateObjective() != null) {
                throw new AlreadyAssignedException("The player has already chosen his private objective.");
            } else if (p.getObjectivesToChooseFrom()[0].equals(c) || p.getObjectivesToChooseFrom()[1].equals(c)) {
                p.setPrivateObjective(c);
                state.gamePhaseHandler();

            } else throw new IncorrectUserActionException("The objective you chose was not one of the two assigned to you.");
        } else throw new WrongGamePhaseException("You cannot choose your objective now.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the method playerPlacesCard makes the player place the card in the position he chose, and with the side he chose.
     * @param p is the player placing the card.
     * @param c is the card the player's placing.
     * @param s is the side of the card that the player chose.
     * @param pos is the position where the player's placing the card.
     * @throws IncorrectUserActionException if it's not the player's turn.
     * @throws WrongGamePhaseException if the player tries to place a card in the wrong phase of the game.
     */
    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_PLACE)) {
                p.placeCard(c, s, pos);
                state.gamePhaseHandler();

            } else throw new WrongGamePhaseException("You cannot place a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the playerDrawsCardFromDeck method makes the player draw a resource card from the deck, then updates the player's virtual view.
     * @param p is the player drawing the card.
     * @param d is the card drawn by the player.
     * @throws IncorrectUserActionException if the player tries to draw the card when it's not his turn.
     * @throws WrongGamePhaseException if the player tries to draw a card imn the wrong phase of the game.
     */
    public void playerDrawsCardFromDeck(Player p, ResourceDeck d) throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                p.drawCardFromDeck(d);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?   HOW DO I SEND CLIENT THE NEW CARD?
                //for (Player pl: gameInstance.getParticipants())      //ACKNOWLEDGE CLIENT?
                //    if(playerViews.get(pl) != null)
                //        playerViews.get(pl).updatesDeckView(d, d.firstBack());

            } else throw new WrongGamePhaseException("You cannot draw a card now");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the playerDrawsCardFromDeck method makes the player draw a gold card from the deck, then updates the player's virtual view.
     * @param p is the player drawing the card.
     * @param d is the card drawn by the player.
     * @throws IncorrectUserActionException if the player tries to draw the card when it's not his turn.
     * @throws WrongGamePhaseException if the player tries to draw a card imn the wrong phase of the game.
     */
    public void playerDrawsCardFromDeck(Player p, GoldDeck d) throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                p.drawCardFromDeck(d);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?   HOW DO I SEND CLIENT THE NEW CARD?
                //for (Player pl: gameInstance.getParticipants())      //ACKNOWLEDGE CLIENT?
                //    if(playerViews.get(pl) != null)
                //        playerViews.get(pl).updatesDeckView(d, d.firstBack());

            } else throw new WrongGamePhaseException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the method playerDrawsCardFromAvailable makes the player draw a card the available ones on the field, proceed then to update the player's
     * virtual view showing the new available ones.
     * @param p is the player who's drawing the card.
     * @param c is the card drawn by the player.
     * @throws IncorrectUserActionException if the player tries to draw when it's not his turn.
     * @throws WrongGamePhaseException if the player tries to draw when in the wrong phase of the game.
     */
    public void playerDrawsCardFromAvailable(Player p, StandardCard c) throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                p.drawCardFromAvailable(c);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?
                //playerViews.get(p).acknowledgePlayer(p);           //OTHERWISE REUSE METHOD SEND AVAILABLE
                //for (Player pl: gameInstance.getParticipants())    //HOW DO I KNOW WHICH LIST CHANGED? RETURN A LIST FROM METHOD
                //    if(playerViews.get(pl) != null)
                //        playerViews.get(pl).updatesAvailableCardView(gameInstance.getAvailableGCards());

            } else throw new WrongGamePhaseException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    /**
     * the method checkCurrentTurn returns the turn of the player.
     * @param p is the player checked.
     * @return the turn of the player.
     */
    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }

    /**
     * the method sendResults sends the results of the game that have been calculated by the getGameWinner method and updates player's view.
     * @param results is the resulting points of each  player.
     */
    public void sendResults(PlayerPoints[] results) {
        /* Sends the results of the game that have been calculated by the getGameWinner() method.*/
        for (Player p: gameInstance.getParticipants())
            if(playerViews.get(p) != null)
                playerViews.get(p).sendResults(results);
    }

    /**
     * the handleDisconnection method set the state of the connection of the player.
     * @param p is the player disconnected.
     */
    public void handleDisconnection(Player p) {
        gameInstance.setDisconnected(p);
    }

    //IMPLEMENT DISCONNECTIONS WITH TIMEOUTS HANDLING IN SERVER
    /**
     * the method checkConnection
     * @param p ...
     * @return ...
     */
    public boolean checkConnection(Player p) {
        return true;
    }

    /**
     *
     * @param p ...
     */
    public void handleReconnection(Player p) {
        gameInstance.reconnect(p);
    }

}
