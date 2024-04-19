package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

public class WaitEndGameDraw implements State {
    private final GameController controller;

    public WaitEndGameDraw(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void gamePhaseHandler() {
        if (controller.getGameInstance().getLastTurn() == controller.getGameInstance().getTurnCounter()) {
            controller.getGameInstance().handleResults();
            controller.getGameInstance().setCurrentStatus(GameStatus.OVER);
        } else {
            controller.getGameInstance().nextTurn();
            controller.setState(new WaitEndGamePlace(controller));
        }
    }
}
