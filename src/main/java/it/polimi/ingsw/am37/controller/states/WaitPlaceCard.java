package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

public class WaitPlaceCard implements State{
    private final GameController controller;

    public WaitPlaceCard(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_PLACE);

        this.controller.notifyTurn(this.controller.getGameInstance().getCurrentTurn());
    }

    @Override
    public void gamePhaseHandler() {
        controller.setState(new WaitDrawCard(controller));
    }
}
