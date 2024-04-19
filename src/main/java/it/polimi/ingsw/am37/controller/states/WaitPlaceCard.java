package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;

public class WaitPlaceCard implements State{
    private final GameController controller;

    public WaitPlaceCard(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void gamePhaseHandler() {
        controller.setState(new WaitDrawCard(controller));
    }
}
