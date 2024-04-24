package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;

public interface State {
    void gamePhaseHandler() throws AlreadyAssignedException, NoCardsException;

}
