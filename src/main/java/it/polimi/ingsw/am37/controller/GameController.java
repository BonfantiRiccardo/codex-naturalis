package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameController {   //THE CLASS IS ONLY A PROTOTYPE
    private final List<Player> addedPlayers;
    private final int numOfPlayers;
    private GameModel gameInstance;

    public GameController(Player creator, int numOfPlayers) {
        addedPlayers = new ArrayList<>();
        addedPlayers.add(creator);

        this.numOfPlayers = numOfPlayers;
    }

    public List<Player> getAddedPlayers() {
        return addedPlayers;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public GameModel getGameInstance() {
        return gameInstance;
    }

    public void addPlayer(Player newPlayer) {
        if (addedPlayers.size() < numOfPlayers)
            addedPlayers.add(newPlayer);
        else
            System.out.println("The game is full");
    }

    public void setGameInstance(GameModel model) throws AlreadyAssignedException {
        if (this.gameInstance != null)
            throw new AlreadyAssignedException("The game has already been created");
        else {
            Collections.shuffle(addedPlayers);
            this.gameInstance = model;
        }
    }

    public void gamePhaseHandler() {
        boolean prep = false;   boolean play = false;   boolean end = false;
        while (!gameInstance.getCurrentPhase().equals(GamePhase.FINISHED)) {
            if (gameInstance.getCurrentPhase().equals(GamePhase.PREPARATION) && !prep) {
                try {
                    gameInstance.preparationPhase();
                    prep = true;
                } catch (NoCardsException | AlreadyAssignedException | IncorrectUserActionException e) {
                    throw new RuntimeException(e);
                }
            }

            if (gameInstance.getCurrentPhase().equals(GamePhase.PLAYING) && !play) {
                gameInstance.playingPhase();
                play = true;
            }

            if (gameInstance.getCurrentPhase().equals(GamePhase.ENDGAME) && !end) {
                gameInstance.endGamePhase();
                end = true;
            }
        }

    }

    //------------------------------------------------------------------------------------------------

    public void playerHasToChooseStartCardSide(Player p, StartCard c) throws WrongGamePhaseException {
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            System.out.println("The player " + p.getNickname() + " has to choose one of the two Side of the StartCard given to him.");
            System.out.println(c.getFront().toString());        //3 lines of debug.
            System.out.println(c.getBack().toString());
            //Calls the method on the client with RMI or communicates with Socket TCP
            gameInstance.setCurrentStatus(GameStatus.WAIT_START_CARD_SIDE);
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");

    }

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) throws IncorrectUserActionException {     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_START_CARD_SIDE)) {
            gameInstance.setCurrentStatus(GameStatus.LOADING);
            if (!p.getStartCard().equals(c) || (!c.getBack().equals(s) && !c.getFront().equals(s))) {
                throw new IncorrectUserActionException("You tried to place a Card that is not your StartCard.");
            } else {
                try {
                    p.instantiateMyKingdom(c, s);
                } catch (AlreadyAssignedException e) {
                    throw new RuntimeException(e);
                }
            }
        } else throw new IncorrectUserActionException("You cannot place your start card now.");
    }

    public void playerHasToChooseToken(Player p) throws WrongGamePhaseException {
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            //Calls the method on the client with RMI or communicates with Socket TCP
            System.out.println("Player " + p.getNickname() + "choose your token");  //debug
            gameInstance.setCurrentStatus(GameStatus.WAIT_TOKEN);
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");
    }

    public void playerChoosesToken (Player p, Token t) throws AlreadyAssignedException, IncorrectUserActionException {     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_TOKEN)) {
            gameInstance.setCurrentStatus(GameStatus.LOADING);
            boolean check = true;
            for (Player pl: gameInstance.getParticipants()) {
                if (pl.getToken().equals(t)) {
                    check = false;
                    break;
                }
            }
            if (check)
                p.setToken(t);
            else
                throw new IncorrectUserActionException("The token has been chosen by another player.");
        } else throw new IncorrectUserActionException("You cannot choose your token now.");
    }

    public void playerHasToChooseObjective(Player p, ObjectiveCard[] cArray) throws WrongGamePhaseException { //STUB FOR TESTING
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            System.out.println("The player " + p.getNickname() + " has to choose one of the two objective card given to him as his private objective.");
            System.out.println(cArray[0].toString()); // lines of debug.
            System.out.println(cArray[1].toString());
            //Calls the method on the client with RMI or communicates with Socket TCP
            gameInstance.setCurrentStatus(GameStatus.WAIT_OBJECTIVE);
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");
    }

    public void playerChoosesObjective(Player p, ObjectiveCard c) throws IncorrectUserActionException, AlreadyAssignedException {
        //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_OBJECTIVE)) {
            gameInstance.setCurrentStatus(GameStatus.LOADING);
            if (p.getPrivateObjective() != null) {
                throw new AlreadyAssignedException("The player has already chosen his private objective.");
            } else
                    p.setPrivateObjective(c);
        } else throw new IncorrectUserActionException("You cannot choose your objective now.");
    }

    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }

    public void notifyTurn(Player p) {
        /*
        * Notify the player that it is now his turn.
        *
        * */
    }

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) throws IncorrectUserActionException {
         /*Checks if it is the player turn,
         if the gameModel is in status wait place card
         try to place said Card in the kingdom
         if it is successfully placed continue,
         else throw IncorrectUserActionException */
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_PLACE)) {
            gameInstance.setCurrentStatus(GameStatus.LOADING);
            p.placeCard(c, s, pos);     //TRY CATCH FOR EXCEPTIONS
            gameInstance.setCurrentStatus(GameStatus.WAIT_DRAW);        //FORSE MEGLIO SPOSTARE FUORI VISTO CHE PER DRAW è COSì
        } else throw new IncorrectUserActionException("You cannot place a card now.");
    }

    public void playerDrawsCardFromDeck(Player p, Deck d) throws IncorrectUserActionException {
        /*Checks if it is the player turn,
         if the gameModel is in status wait draw card
         try to draw from said deck
         if it is successfully drawn continue,
         else throw IncorrectUserActionException */
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
            gameInstance.setCurrentStatus(GameStatus.LOADING);
            try {
                p.drawCardFromDeck(d);      //TRY CATCH FOR OTHER EXCEPTIONS
                //gameInstance.setCurrentStatus(GameStatus.WAIT_PLACE); NON VA BENE QUI PERCHè IL TURNO DEVE FINIRE PRIMA
            } catch (NoCardsException e) {          //DI POTER PASSARE ALLA PROSSIMA FASE.
                throw new RuntimeException(e);
            }
        } throw new IncorrectUserActionException("You cannot draw a card now");

    }

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) throws IncorrectUserActionException {
        /*Checks if it is the player turn,
         if the gameModel is in status wait draw card
         try to draw said Card from the available
         if it is successfully drawn continue,
          else throw IncorrectUserActionException */
        if (gameInstance.getCurrentStatus().equals(GameStatus.WAIT_DRAW)) {
            gameInstance.setCurrentStatus(GameStatus.LOADING);
            try {
                p.drawCardFromAvailable(c);
                //gameInstance.setCurrentStatus(GameStatus.WAIT_PLACE); NON VA BENE QUI PERCHè IL TURNO DEVE FINIRE PRIMA
            } catch (NoCardsException e) {          //DI POTER PASSARE ALLA PROSSIMA FASE.
                throw new RuntimeException(e);
            }
        } else throw new IncorrectUserActionException("You cannot draw a card now.");

    }


    public void actionNotPermittedMessaging(Player p, String errorMessage) {}

    public boolean tryReconnection(Player p) {return true;}

    public boolean checkConnection(Player p) {return true;}


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
}
