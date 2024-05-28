package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;

/**
 * the State interface represents each state of the game.
 */
public interface State {
    /**
     * the GamesPhaseHandler method is different in each implementation and handles the game based on the state of it.
     * @throws AlreadyAssignedException if a card has already been assigned.
     * @throws NoCardsException if there are no cards left.
     */
    void gamePhaseHandler() throws AlreadyAssignedException, NoCardsException;

}
