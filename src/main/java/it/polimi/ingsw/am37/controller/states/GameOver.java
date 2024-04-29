package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

/**
 * the GameOver class implements the State interface and manages the last state of the game.
 */
public class GameOver implements State {
    GameController controller;

    public GameOver(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.OVER);

        this.controller.sendResults(this.controller.getGameInstance().getGameWinner());
    }

    @Override
    public void gamePhaseHandler() {
        //EMPTY BECAUSE THE GAME IS OVER.
    }
}
