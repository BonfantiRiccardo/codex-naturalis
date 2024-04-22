package it.polimi.ingsw.am37.virtualview;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.Deck;
import it.polimi.ingsw.am37.model.decks.GoldDeck;
import it.polimi.ingsw.am37.model.decks.ResourceDeck;
import it.polimi.ingsw.am37.model.game.PlayerPoints;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.EventListener;
import java.util.List;

public class VirtualView implements EventListener {
    private final GameController controller;

    public VirtualView(GameController controller) {
        this.controller = controller;
        updateLobbyView(controller.getAddedPlayers().get(0), 1, controller.getNumOfPlayers());
    }

    public GameController getController() {
        return controller;
    }

    public void addPlayer (Player newPlayer) {
        try {
            controller.addPlayer(newPlayer);
        } catch (IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(newPlayer, e.getMessage());
        }
    }     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) {
        try {
            controller.playerChoosesStartCardSide(p, c, s);
        } catch (IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesToken (Player p, Token t) {
        try {
            controller.playerChoosesToken(p, t);
        } catch (AlreadyAssignedException | IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesObjective(Player p, ObjectiveCard c) {
        try {
            controller.playerChoosesObjective(p, c);
        } catch (AlreadyAssignedException | WrongGamePhaseException | IncorrectUserActionException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) {
        try {
            controller.playerPlacesCard(p, c, s, pos);
        } catch (IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, ResourceDeck d) {
        try {
            controller.playerDrawsCardFromDeck(p, d);
        } catch (IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, GoldDeck d) {
        try {
            controller.playerDrawsCardFromDeck(p, d);
        } catch (IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) {
        try {
            controller.playerDrawsCardFromAvailable(p, c);
        } catch (IncorrectUserActionException | WrongGamePhaseException e) {
            actionNotPermittedMessaging(p, e.getMessage());
        }
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT


    public void actionNotPermittedMessaging(Player p, String errorMessage) {
        //SENDS ERROR MESSAGE TO CLIENT'S VIEW
    }

    public void notifyTurn(Player p) {
        //SENDS NOTIFICATION TO THE PLAYER THAT HAS ENTERED HIS TURN
    }

    public void updateLobbyView(Player newPlayer, int numPlayers, int maxPlayers) {
        //SENDS LOBBY UPDATES TO THE REMOTE LOBBY VIEW
    }

    public void updatesDeckView(Deck d, Back s) {
        //SENDS DECKS UPDATES TO THE REMOTE VIEW
    }

    public void generateHandView(Player p, List<StandardCard> hand) {
        //SENDS GENERATED HAND TO THE REMOTE VIEW
    }

    public void updatesObjectivesView(Player p, ObjectiveCard[] objToChooseFrom) {
        //SENDS THE TWO OBJECTIVE CARDS THAT THE PLAYER CAN CHOOSE FROM
    }

    public void updatePlayerHandView(Player p, StandardCard c) {
        //SENDS HAND UPDATES TO THE REMOTE VIEW
    }

    public void updatesCardView(List<StandardCard> cList, StandardCard c) {
        //SENDS AVAILABLE CARDS UPDATES TO THE REMOTE VIEW
    }

    public void updatesPlayersKingdomView(Player p, StandardCard placed) {
        //SENDS KINGDOM UPDATES TO THE REMOTE VIEW
    }

    public void sendResults(PlayerPoints[] results) {
        //SENDS THE RESULTS OF THE GAME TO THE REMOTE VIEW
    }
}
