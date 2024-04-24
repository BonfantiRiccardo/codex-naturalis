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
    public void addPlayer (Player newPlayer) throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        if (gameInstance == null) {
            if (participants.size() < numOfPlayers) {
                for (Player p: participants) {
                    if (newPlayer.getNickname().equals(p.getNickname()))
                        throw new IncorrectUserActionException("This username is already in use.");
                }

                participants.add(newPlayer);
                playerViews.put(newPlayer, new VirtualView(this));
                state.gamePhaseHandler();

                for (Player p: participants)                                    //SINGLE VIRTUAL VIEW OR 1 FOR EACH PLAYER?
                    playerViews.get(p).updateLobbyView(newPlayer, participants.size(), numOfPlayers);

            } else
                throw new IncorrectUserActionException("The game is full.");
        } else throw new WrongGamePhaseException("This game has started.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_START_CARD_SIDE)) {
            if (!p.getStartCard().equals(c) || (!c.getBack().equals(s) && !c.getFront().equals(s))) {
                throw new IncorrectUserActionException("You tried to place a Card that is not your StartCard.");
            } else {
                p.instantiateMyKingdom(c, s);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?
                playerViews.get(p).acknowledgePlayer(p);
                for (Player pl: gameInstance.getParticipants())
                    if (!pl.equals(p))
                        playerViews.get(pl).updatesPlayersKingdomView(p, c, s, s.getPositionInKingdom());

            }
        } else throw new WrongGamePhaseException("You cannot place your start card now.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

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

                // UPDATE VIEW DIRECTLY HERE?
                playerViews.get(p).acknowledgePlayer(p);
                for (Player pl: gameInstance.getParticipants())
                    if (!pl.equals(p))
                        playerViews.get(pl).nowUnavailableToken(t);

            } else
                throw new IncorrectUserActionException("The token has been chosen by another player.");
        } else throw new WrongGamePhaseException("You cannot choose your token now.");

    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    public void playerChoosesObjective(Player p, ObjectiveCard c) throws AlreadyAssignedException, WrongGamePhaseException, IncorrectUserActionException, NoCardsException {
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_OBJECTIVE)) {
            if (p.getPrivateObjective() != null) {
                throw new AlreadyAssignedException("The player has already chosen his private objective.");
            } else if (p.getObjectivesToChooseFrom()[0].equals(c) || p.getObjectivesToChooseFrom()[1].equals(c)) {
                p.setPrivateObjective(c);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?
                playerViews.get(p).acknowledgePlayer(p);

            } else throw new IncorrectUserActionException("The objective you chose was not one of the two assigned to you.");
        } else throw new WrongGamePhaseException("You cannot choose your objective now.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_PLACE)) {
                p.placeCard(c, s, pos);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?
                for (Player pl: gameInstance.getParticipants())
                    playerViews.get(pl).updatesPlayersKingdomView(p, c, s, pos);

            } else throw new WrongGamePhaseException("You cannot place a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    public void playerDrawsCardFromDeck(Player p, ResourceDeck d) throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                p.drawCardFromDeck(d);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?   HOW DO I SEND CLIENT THE NEW CARD?
                for (Player pl: gameInstance.getParticipants())      //ACKNOWLEDGE CLIENT?
                    playerViews.get(pl).updatesDeckView(d, d.firstBack());

            } else throw new WrongGamePhaseException("You cannot draw a card now");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    public void playerDrawsCardFromDeck(Player p, GoldDeck d) throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        if(checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                p.drawCardFromDeck(d);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?   HOW DO I SEND CLIENT THE NEW CARD?
                for (Player pl: gameInstance.getParticipants())      //ACKNOWLEDGE CLIENT?
                    playerViews.get(pl).updatesDeckView(d, d.firstBack());

            } else throw new WrongGamePhaseException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        if (checkCurrentTurn(p)) {
            if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
                p.drawCardFromAvailable(c);
                state.gamePhaseHandler();

                // UPDATE VIEW DIRECTLY HERE?
                playerViews.get(p).acknowledgePlayer(p);           //OTHERWISE REUSE METHOD SEND AVAILABLE
                for (Player pl: gameInstance.getParticipants())    //HOW DO I KNOW WHICH LIST CHANGED? RETURN A LIST FROM METHOD
                    playerViews.get(pl).updatesCardView(gameInstance.getAvailableGCards());

            } else throw new WrongGamePhaseException("You cannot draw a card now.");
        } else throw new IncorrectUserActionException("It is not your turn.");
    }    //THIS METHOD IS CALLED BY THE VIRTUAL VIEW

    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }



    //VIRTUAL VIEW DIRECTLY CALLED AFTER MODIFICATION IN PREVIOUS CONTROLLER METHODS, SO PROBABLY DELETE AFTER THIS

    public void sendAvailable(List<StandardCard> cGold, List<StandardCard> cResource) {     //PROBABLY NEEDED
        /* Sends the two objectives card that the player can draw from.*/
        for (Player pl: gameInstance.getParticipants())
            playerViews.get(pl).sendAvailables(cGold, cResource);
    }

    public void sendStartCard(Player p, StartCard sc) {     //PROBABLY NEEDED
        /*Sends the start card that was assigned to the player*/
        playerViews.get(p).sendStartCard(p, sc);
    }

    public void generateHandView(Player p, List<StandardCard> hand) {     //PROBABLY NEEDED
        /* Sends all the list of cards that were assigned to the initial hand of the player*/
        playerViews.get(p).generateHandView(p, hand);
    }

    public void generatePublicObjectivesView(ObjectiveCard[] publicObjectives) {
        //SENDS THE TWO PUBLIC OBJECTIVE CARDS
        for (Player p:  gameInstance.getParticipants())
            playerViews.get(p).generatePublicObjectivesView(p, publicObjectives);
    }

    public void notifyTurn(Player p) {     //PROBABLY NEEDED
        playerViews.get(p).notifyTurn(p);   //PASSING p AS PARAMETER SHOULDN'T BE NECESSARY
    }

    public void sendResults(PlayerPoints[] results) {     //PROBABLY NEEDED
        /* Sends the results of the game that have been calculated by the getGameWinner() method.*/
        for (Player p: gameInstance.getParticipants())
            playerViews.get(p).sendResults(results);
    }


    //IMPLEMENT DISCONNECTIONS WITH TIMEOUTS HANDLING IN SERVER
    public boolean handleDisconnection(Player p) {return true;}

    public boolean checkConnection(Player p) {return true;}

    public boolean handleReconnection(Player p) {return true;}
}
