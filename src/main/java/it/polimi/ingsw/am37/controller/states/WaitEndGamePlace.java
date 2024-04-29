package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

/**
 * the WaitEndGamePlace class implements the state interface and represents the lasts placings of each player.
 */
public class WaitEndGamePlace implements State {
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the WaitEndGamePlace class sets the status of the game at wait_place.
     * @param controller is the controller of the game.
     */
    public WaitEndGamePlace(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_PLACE);
    }

    /**
     * the gamePhaseHandler changes the state of the game.
     */
    @Override
    public void gamePhaseHandler() {
        controller.setState(new WaitEndGameDraw(controller));
    }
}
