package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;

public class WaitEndGamePlace implements State {
    private final GameController controller;

    public WaitEndGamePlace(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void gamePhaseHandler() {
        controller.setState(new WaitEndGameDraw(controller));
    }
}
