package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

public class WaitEndGamePlace implements State {
    private final GameController controller;

    public WaitEndGamePlace(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_PLACE);
    }

    @Override
    public void gamePhaseHandler() {
        controller.setState(new WaitEndGameDraw(controller));
    }
}
