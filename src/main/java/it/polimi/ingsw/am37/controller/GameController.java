package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.model.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.model.game.GameModel;
import it.polimi.ingsw.am37.model.game.GamePhase;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.sides.*;

import java.util.ArrayList;
import java.util.List;

public class GameController {   //THE CLASS IS ONLY A PROTOTYPE
    private final List<Player> addedPlayers;
    private final int numOfPlayers;
    private GameModel gameInstance;

    public GameController(Player creator, int numOfPlayers) {
        addedPlayers = new ArrayList<>();
        addedPlayers.add(creator);

        this.numOfPlayers = numOfPlayers;
        //lobbyPhase();
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

    private void lobbyPhase() { //STUB METHOD
        /*while (addedPlayers.size() < numOfPlayers) {
            System.out.println("Waiting for players to fill the lobby.");
            System.out.println("Now joined: " + addedPlayers.size() + "/" + numOfPlayers);
            //INDEFINITELY WAITS FOR PLAYERS TO JOIN
        }*/

        try {
            setGameInstance();
        } catch (AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }

        gamePhaseHandler();
    }

    public void addPlayer(Player newPlayer) {
        if (addedPlayers.size() < numOfPlayers)
            addedPlayers.add(newPlayer);
        else
            System.out.println("The game is full");
    }

    public void setGameInstance() throws AlreadyAssignedException {
        if (this.gameInstance != null)
            throw new AlreadyAssignedException("The game has already been created");
        else
            this.gameInstance = new GameModel(addedPlayers/*, this*/);
    }

    public void gamePhaseHandler() {
        boolean prep = false;   boolean play = false;   boolean end = false;
        while (!gameInstance.getCurrentPhase().equals(GamePhase.FINISHED)) {
            if (gameInstance.getCurrentPhase().equals(GamePhase.PREPARATION) && !prep) {
                try {
                    gameInstance.preparationPhase();
                    prep = true;
                } catch (NoCardsException | AlreadyAssignedException e) {
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

    private boolean checkCurrentTurn(Player p) {
        return gameInstance.getCurrentTurn().equals(p);
    }

    public void notifyTurn(Player p) {
        /*
        * Notify the player that it is now his turn.
        *
        * Wait for that player to place a Card.
        *
        * Wait for that player to draw a card.
        *
        * */
        System.out.println("Player notified");
        System.out.println("Player places card");
        //STUB TO RANDOMLY ADD POINTS TO THE PLAYER
        if (gameInstance.getTurnCounter() % 5 == 0)
            gameInstance.getScoreboard().addPoints(p.getToken(), 3);
        System.out.println("Player draws card");

    }

    public void playerDrawsCardFromDeck(Player p, Deck d) { /*Checks if it is the player turn, checks if the player has already drawn a card.*/ }

    public void updatesDeckView(Deck d, Side s) { Side toShow = d.firstBack(); }

    public void updatePlayerHandView(Player p, StandardCard c) {}

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) { /*Checks if it is the player turn, checks if the player has already drawn a card.*/ }

    public void updatesCardView(List<StandardCard> cList, StandardCard c) {}

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) { /*Checks if it is the player turn, checks if the player has already placed a card.*/ }

    public void updatesPlayerKingdomView(Player p, Kingdom k) {}

    public void playerHasToChooseStartCardSide(Player p, StartCard c) throws WrongGamePhaseException {
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            System.out.println("The player " + p.getNickname() + " has to choose one of the two Side of the StartCard given to him.");
            System.out.println(c.getFront().toString());
            System.out.println(c.getBack().toString());
            //Calls the method on the client with RMI or communicates with Socket TCP
            //IMPLEMENTING A STUB TO TEST THE PLAYER METHODS:
            playerChoosesStartCardSide(p, c, c.getFront());
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");

    }

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) {     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        /*Checks if the player has already chosen a Side.*/
        if (p.getPrivateObjective() != null) {
            System.out.println("The player has already chosen his private objective.");
        } else {
            try {
                p.instantiateMyKingdom(c, s);
            } catch (AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void playerHasToChooseObjective(Player p, ObjectiveCard[] cArray) throws WrongGamePhaseException { //STUB FOR TESTING
        if (gameInstance.getCurrentPhase() == GamePhase.PREPARATION) {
            System.out.println("The player " + p.getNickname() + " has to choose one of the two objective card given to him as his private objective.");
            System.out.println(cArray[0].toString());
            System.out.println(cArray[1].toString());
            //Calls the method on the client with RMI or communicates with Socket TCP
            //IMPLEMENTING A STUB TO TEST THE PLAYER METHODS:
            playerChoosesObjective(p, cArray[0]);
        } else
            throw new WrongGamePhaseException("Method invoked in the wrong GamePhase");
    }

    public void playerChoosesObjective(Player p, ObjectiveCard c) {     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT
        /*Checks if the player has already chosen a card.*/
        if (p.getPrivateObjective() != null) {
            System.out.println("The player has already chosen his private objective.");
        } else {
            try {
                p.setPrivateObjective(c);
            } catch (AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean tryReconnection(Player p) {return true;}

    public boolean checkConnection(Player p) {return true;}

    public void actionNotPermittedMessaging(Player p, String errorMessage) {}

}
