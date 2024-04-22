package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.controller.states.*;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;
import it.polimi.ingsw.am37.virtualview.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private final List<Player> participants;
    private final int numOfPlayers;
    private boolean isGameStarted;
    private GameModel gameInstance;
    private final Map<Player, VirtualView> playerViews;
    private State state;

    public GameController(Player creator, int numOfPlayers) {
        isGameStarted = false;

        participants = new ArrayList<>();
        participants.add(creator);
        playerViews = new HashMap<>();
        playerViews.put(creator, new VirtualView(this));

        this.numOfPlayers = numOfPlayers;       //BEFORE WE NEED TO CHECK THAT IS BETWEEN 0 AND 4.

        state = new LobbyState(this);
    }

    public List<Player> getAddedPlayers() {
        return participants;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameModel getGameInstance() {
        return gameInstance;
    }

    public void setGameInstance(GameModel model) throws AlreadyAssignedException {
        if (this.gameInstance != null)
            throw new AlreadyAssignedException("The game has already been created");
        else
            this.gameInstance = model;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        isGameStarted = gameStarted;
    }

    public Map<Player, VirtualView> getPlayerViews() {
        return playerViews;
    }


    //------------------------------------------------------------------------------------------------
    public void addPlayer (Player newPlayer) throws IncorrectUserActionException, WrongGamePhaseException {
        if (gameInstance == null) {
            if (participants.size() < numOfPlayers) {
                for (Player p: participants) {
                    if (newPlayer.getNickname().equals(p.getNickname()))
                        throw new IncorrectUserActionException("This username is already in use.");
                }

                participants.add(newPlayer);
                state.gamePhaseHandler();
                //playerViews.put(newPlayer, new VirtualView(this));
                //for (Player p: participants)
                    //playerViews.get(p).updateLobbyView(newPlayer, participants.size(), numOfPlayers);

            } else
                throw new IncorrectUserActionException("The game is full.");
        } else throw new WrongGamePhaseException("This game has started.");
    }

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) throws IncorrectUserActionException, WrongGamePhaseException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_START_CARD_SIDE)) {
            if (!p.getStartCard().equals(c) || (!c.getBack().equals(s) && !c.getFront().equals(s))) {
                throw new IncorrectUserActionException("You tried to place a Card that is not your StartCard.");
            } else {
                try {
                    p.instantiateMyKingdom(c, s);
                    state.gamePhaseHandler();
                } catch (AlreadyAssignedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else throw new WrongGamePhaseException("You cannot place your start card now.");

    }

    public void playerChoosesToken (Player p, Token t) throws AlreadyAssignedException, IncorrectUserActionException, WrongGamePhaseException {
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

    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesObjective(Player p, ObjectiveCard c) throws AlreadyAssignedException, WrongGamePhaseException, IncorrectUserActionException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_OBJECTIVE)) {
            if (p.getPrivateObjective() != null) {
                throw new AlreadyAssignedException("The player has already chosen his private objective.");
            } else if (p.getObjectivesToChooseFrom()[0].equals(c) || p.getObjectivesToChooseFrom()[1].equals(c)) {
                p.setPrivateObjective(c);
                state.gamePhaseHandler();
            } else throw new IncorrectUserActionException("The objective you chose was not one of the two assigned to you.");
        } else throw new WrongGamePhaseException("You cannot choose your objective now.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) throws IncorrectUserActionException, WrongGamePhaseException {
         /*Checks if it is the player turn,
         if the gameModel is in status wait place card
         try to place said Card in the kingdom
         if it is successfully placed continue,
         else throw IncorrectUserActionException */
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_PLACE)) {
                p.placeCard(c, s, pos);
                state.gamePhaseHandler();
            } else throw new WrongGamePhaseException("You cannot place a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, ResourceDeck d) throws IncorrectUserActionException, WrongGamePhaseException {
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                try {
                    p.drawCardFromDeck(d);      //TRY CATCH FOR OTHER EXCEPTIONS
                    state.gamePhaseHandler();
                } catch (NoCardsException e) {
                    throw new RuntimeException(e);
                }
            } else throw new WrongGamePhaseException("You cannot draw a card now");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, GoldDeck d) throws IncorrectUserActionException, WrongGamePhaseException {
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                try {
                    p.drawCardFromDeck(d);      //TRY CATCH FOR OTHER EXCEPTIONS
                    state.gamePhaseHandler();
                } catch (NoCardsException e) {
                    throw new RuntimeException(e);
                }
            } else throw new WrongGamePhaseException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) throws IncorrectUserActionException, WrongGamePhaseException {
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                try {
                    p.drawCardFromAvailable(c);
                    state.gamePhaseHandler();
                } catch (NoCardsException e) {
                    throw new RuntimeException(e);
                }
            } else throw new WrongGamePhaseException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT


    //IN VIRTUAL VIEW? CALLED FROM MODEL OR FROM CONTROLLER?

    public void sendStartCard(Player p, StartCard sc) {
        /*Sends the start card that was assigned to the player*/
    }

    public void notifyTurn(Player p) {
        playerViews.get(p).notifyTurn(p);   //PASSING p AS PARAMETER SHOULDN'T BE NECESSARY
    }

    public void generateHandView(Player p, List<StandardCard> hand) {
        /* Sends all the list of cards that were assigned to the initial hand of the player*/
        playerViews.get(p).generateHandView(p, hand);
    }

    public void updatesDeckView(Deck d, Back s) {
        /*Sends the new back side that is visible from the top of the deck.
         * OR SEND THE RESOURCE SO THAT THE SIDE REMAINS UNKNOWN*/
        for (Player p: gameInstance.getParticipants())
            playerViews.get(p).updatesDeckView(d, s);
    }

    public void updatePlayerHandView(Player p, StandardCard c) {
        /*Sends the new card that the player has drawn and that should appear in his hand.*/
        playerViews.get(p).updatePlayerHandView(p, c);
    }

    public void updatesCardView(List<StandardCard> cList, StandardCard c) {
        /* Sends the new Card that is available for anyone to draw.*/
        for (Player p: gameInstance.getParticipants())
            playerViews.get(p).updatesCardView(cList, c);
    }

    public void updatesObjectivesView(Player p, ObjectiveCard[] objToChooseFrom) {
        /* Sends the two objectives card that the player can draw from.*/
        playerViews.get(p).updatesObjectivesView(p, objToChooseFrom);
    }

    public void updatesPlayerKingdomView(Player p, StandardCard placed) {
        /* Sends the kingdom that has been modified after the placing of the card.
         * Probably better to only send the diffs to the previous state.*/
        for (Player pl: gameInstance.getParticipants())
            playerViews.get(pl).updatesPlayersKingdomView(p, placed);
    }

    public void sendResults(PlayerPoints[] results) {
        /* Sends the results of the game that have been calculated by the getGameWinner() method.*/
        for (Player p: gameInstance.getParticipants())
            playerViews.get(p).sendResults(results);
    }



    public boolean handleDisconnection(Player p) {return true;}

    public boolean checkConnection(Player p) {return true;}

    public boolean handleReconnection(Player p) {return true;}
}
