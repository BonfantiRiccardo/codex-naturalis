package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;

/**
 * the WaitDrawCard class implements the State interface and represents the action of drawing a card by the player.
 */
public class WaitDrawCard implements State {
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the method WaitDrawCard sets the game status at wait_draw.
     * @param controller is the controller of the game.
     */
    public WaitDrawCard(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_DRAW);
    }

    /**
     * the method gamePhaseHandler increases the turn and if the points of the player are under 20, sets the new state to WaitPlaceCard,
     * otherwise it sets up the end game phase and sets the state to WaitEndGamePlace.
     */
    @Override
    public void gamePhaseHandler() {
        if (controller.getGameInstance().getScoreboard().getParticipantsPoints().get(controller.getGameInstance().getCurrentTurn()) < 20 &&
                !(controller.getGameInstance().getRDeck().isEmpty() && controller.getGameInstance().getGDeck().isEmpty()) ) {
            controller.getGameInstance().nextTurn();
            controller.setState(new WaitPlaceCard(controller));
        } else {
            controller.getGameInstance().setupEndGame();
            controller.setEndGameStarted(true);
            controller.getGameInstance().nextTurn();
            controller.setState(new WaitEndGamePlace(controller));
        }
    }
}
