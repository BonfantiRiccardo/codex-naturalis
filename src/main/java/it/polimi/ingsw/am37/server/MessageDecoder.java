package it.polimi.ingsw.am37.server;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.model.player.Player;

public class MessageDecoder {
    private GameController c;

    public void decodeAndExecute(String s, int num) {
        c = new GameController(new Player(s), num);
        System.out.println("correctly created");
    }

    public void add(String s) {
        try {
            c.addPlayer(new  Player(s));
            System.out.println("correctly added");
        } catch (IncorrectUserActionException | WrongGamePhaseException | NoCardsException | AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }
    }
}
