package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

public class GameOver implements State {
    GameController controller;

    public GameOver(GameController controller) {
        this.controller = controller;
        controller.getGameInstance().setCurrentStatus(GameStatus.OVER);
        controller.getGameInstance().handleResults();
    }

    @Override
    public void gamePhaseHandler() {
        //EMPTY BECAUSE THE GAME IS OVER.
    }
}
