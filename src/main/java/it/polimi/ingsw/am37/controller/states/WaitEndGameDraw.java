package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

public class WaitEndGameDraw implements State {
    private final GameController controller;

    public WaitEndGameDraw(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_DRAW);
    }

    @Override
    public void gamePhaseHandler() {
        if (controller.getGameInstance().getLastTurn() == controller.getGameInstance().getTurnCounter()) {
            controller.setState(new GameOver(controller));
        } else {
            controller.getGameInstance().nextTurn();
            controller.setState(new WaitEndGamePlace(controller));
        }
    }
}
