package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.controller.states.State;
import it.polimi.ingsw.am37.controller.states.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    private final List<Player> participants;
    private final int numOfPlayers;
    private boolean isGameStarted;
    private GameModel gameInstance;
    private State state;

    public GameController(Player creator, int numOfPlayers) {
        isGameStarted = false;

        participants = new ArrayList<>();
        participants.add(creator);
        this.numOfPlayers = numOfPlayers;

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


    //------------------------------------------------------------------------------------------------
    public void addPlayer (Player newPlayer) throws IncorrectUserActionException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.LOBBY)) {
            if (participants.size() < numOfPlayers) {
                for (Player p: participants) {
                    if (newPlayer.getNickname().equals(p.getNickname()))
                        throw new IncorrectUserActionException("This username is already in use");
                }

                participants.add(newPlayer);
                state.gamePhaseHandler();
            } else
                System.out.println("The game is full");
        }
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

    }     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesToken (Player p, Token t) throws AlreadyAssignedException, IncorrectUserActionException, WrongGamePhaseException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_TOKEN)) {
            boolean check = true;
            for (Player pl: gameInstance.getParticipants()) {
                if (pl.getToken() != null && pl.getToken().equals(t)) {
                    check = false;
                    break;
                }
            }
            if (check)
                p.setToken(t);
            else
                throw new IncorrectUserActionException("The token has been chosen by another player.");
        } else throw new WrongGamePhaseException("You cannot choose your token now.");

    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesObjective(Player p, ObjectiveCard c) throws AlreadyAssignedException, WrongGamePhaseException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_OBJECTIVE)) {
            if (p.getPrivateObjective() != null) {
                throw new AlreadyAssignedException("The player has already chosen his private objective.");
            } else
                    p.setPrivateObjective(c);
        } else throw new WrongGamePhaseException("You cannot choose your objective now.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) throws IncorrectUserActionException {
         /*Checks if it is the player turn,
         if the gameModel is in status wait place card
         try to place said Card in the kingdom
         if it is successfully placed continue,
         else throw IncorrectUserActionException */
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_PLACE)) {
                p.placeCard(c, s, pos);
                gameInstance.setCurrentStatus(GameStatus.WAIT_DRAW);        //FORSE MEGLIO SPOSTARE FUORI VISTO CHE PER DRAW è COSì
            } else throw new IncorrectUserActionException("You cannot place a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, ResourceDeck d) throws IncorrectUserActionException {
        /*Checks if it is the player turn,
         if the gameModel is in status wait draw card
         try to draw from said deck
         if it is successfully drawn continue,
         else throw IncorrectUserActionException */
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                try {
                    p.drawCardFromDeck(d);      //TRY CATCH FOR OTHER EXCEPTIONS
                    //gameInstance.setCurrentStatus(GameStatus.WAIT_PLACE); NON VA BENE QUI PERCHè IL TURNO DEVE FINIRE PRIMA
                } catch (NoCardsException e) {          //DI POTER PASSARE ALLA PROSSIMA FASE.
                    throw new RuntimeException(e);
                }
            } throw new IncorrectUserActionException("You cannot draw a card now");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, GoldDeck d) throws IncorrectUserActionException {
        /*Checks if it is the player turn,
         if the gameModel is in status wait draw card
         try to draw from said deck
         if it is successfully drawn continue,
         else throw IncorrectUserActionException */
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                try {
                    p.drawCardFromDeck(d);      //TRY CATCH FOR OTHER EXCEPTIONS
                    //gameInstance.setCurrentStatus(GameStatus.WAIT_PLACE); NON VA BENE QUI PERCHè IL TURNO DEVE FINIRE PRIMA
                } catch (NoCardsException e) {          //DI POTER PASSARE ALLA PROSSIMA FASE.
                    throw new RuntimeException(e);
                }
            } throw new IncorrectUserActionException("You cannot draw a card now");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) throws IncorrectUserActionException {
        /*Checks if it is the player turn,
         if the gameModel is in status wait draw card
         try to draw said Card from the available
         if it is successfully drawn continue,
          else throw IncorrectUserActionException */
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                try {
                    p.drawCardFromAvailable(c);
                    //gameInstance.setCurrentStatus(GameStatus.WAIT_PLACE); NON VA BENE QUI PERCHè IL TURNO DEVE FINIRE PRIMA
                } catch (NoCardsException e) {          //DI POTER PASSARE ALLA PROSSIMA FASE.
                    throw new RuntimeException(e);
                }
            } else throw new IncorrectUserActionException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");


    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT


    public void updatesDeckView(Deck d, Side s) {
        /*Sends the new back side that is visible from the top of the deck.
         * OR SEND THE RESOURCE SO THAT THE SIDE REMAINS UNKNOWN*/
    }

    public void updatePlayerHandView(Player p, StandardCard c) {
        /*Sends the new card that the player has drawn and that should appear in his hand.*/
    }

    public void updatesCardView(List<StandardCard> cList, StandardCard c) {
        /* Sends the new Card that is available for anyone to draw.*/
    }

    public void updatesPlayerKingdomView(Player p, Kingdom k) {
        /* Sends the kingdom that has been modified after the placing of the card.
         * Probably better to only send the diffs to the previous state.*/
    }


    public void actionNotPermittedMessaging(Player p, String errorMessage) {}

    public boolean handleDisconnection(Player p) {return true;}

    public boolean checkConnection(Player p) {return true;}

    public boolean handleReconnection(Player p) {return true;}
}
