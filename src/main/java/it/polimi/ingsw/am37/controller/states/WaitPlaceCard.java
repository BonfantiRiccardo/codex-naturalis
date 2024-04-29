package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

/**
 * the WaitPlaceCard class implements the State interface and represents the action of placing card by the player.
 */
public class WaitPlaceCard implements State{
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the WaitPlaceCard sets the status of the game at wait_place and notify the player that's his turn.
     * @param controller is the controller of the game.
     */
    public WaitPlaceCard(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_PLACE);

        this.controller.notifyTurn(this.controller.getGameInstance().getCurrentTurn());
    }

    /**
     * the method gamePhaseHandler changes the state of the game.
     */
    @Override
    public void gamePhaseHandler() {
        controller.setState(new WaitDrawCard(controller));
    }
}
