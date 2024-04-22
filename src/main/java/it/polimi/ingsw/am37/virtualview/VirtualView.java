package it.polimi.ingsw.am37.virtualview;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.GoldDeck;
import it.polimi.ingsw.am37.model.decks.ResourceDeck;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;

import java.util.EventListener;

public abstract class VirtualView implements EventListener {
    private final GameController controller;

    public VirtualView(GameController controller) {
        this.controller = controller;
    }

    public GameController getController() {
        return controller;
    }

    //SOSTITUIRE TUTTE LE THROW CON DEI CATCH CHE CHIAMANO IL METODO actionNotPermittedMessaging() CHE RISPONDE ALL'UTENTE CON IL MESSAGGIO DI ERRORE
    public void addPlayer (Player newPlayer) throws IncorrectUserActionException, WrongGamePhaseException {
        controller.addPlayer(newPlayer);
    }     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesStartCardSide(Player p, StartCard c, Side s) throws IncorrectUserActionException, WrongGamePhaseException {
        controller.playerChoosesStartCardSide(p, c, s);
    }     //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesToken (Player p, Token t) throws AlreadyAssignedException, IncorrectUserActionException, WrongGamePhaseException {
        controller.playerChoosesToken(p, t);
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerChoosesObjective(Player p, ObjectiveCard c) throws AlreadyAssignedException, WrongGamePhaseException, IncorrectUserActionException {
        controller.playerChoosesObjective(p, c);
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerPlacesCard(Player p, StandardCard c, Side s, Position pos) throws IncorrectUserActionException, WrongGamePhaseException {
         controller.playerPlacesCard(p, c, s, pos);
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, ResourceDeck d) throws IncorrectUserActionException, WrongGamePhaseException {
        controller.playerDrawsCardFromDeck(p, d);
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromDeck(Player p, GoldDeck d) throws IncorrectUserActionException, WrongGamePhaseException {
        controller.playerDrawsCardFromDeck(p, d);
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void playerDrawsCardFromAvailable(Player p, StandardCard c) throws IncorrectUserActionException, WrongGamePhaseException {
        controller.playerDrawsCardFromAvailable(p, c);
    }    //THIS METHOD IS REMOTELY CALLED BY THE CLIENT

    public void actionNotPermittedMessaging(Player p, String errorMessage) {}



    //UPDATES TO BE IMPLEMENTED IN SUBCLASSES
}
