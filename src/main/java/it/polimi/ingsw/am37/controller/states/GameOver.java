package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

/**
 * the GameOver class implements the State interface and manages the last state of the game.
 */
public class GameOver implements State {
    /**
     * the controller attribute is the reference to the controller of the game.
     */
    GameController controller;

    /**
     * the GameOver method sets the current status of the game to "over"
     * @param controller is the reference to the controller of the game.
     */
    public GameOver(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.OVER);
    }

    /**
     * the gamePhaseHandler method is empty because the game is over.
     */
    @Override
    public void gamePhaseHandler() {
    }
}
