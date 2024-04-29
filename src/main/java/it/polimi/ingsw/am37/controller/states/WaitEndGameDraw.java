package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

/**
 * the WaitEndGameDraw class implements the State interface and represents the lasts drawings of each player.
 */
public class WaitEndGameDraw implements State {
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the WaitEndGameDraw sets the status of the game at wait_draw.
     * @param controller is the controller of the game.
     */
    public WaitEndGameDraw(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_DRAW);
    }

    /**
     * the gamePhaseHandler method checks if it's the last turn, if it is, it changes the state of the game to GameOver which is the last
     * phase, otherwise it keeps increasing the turn and changes the state to WaitEndGamePlace.
     */
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
