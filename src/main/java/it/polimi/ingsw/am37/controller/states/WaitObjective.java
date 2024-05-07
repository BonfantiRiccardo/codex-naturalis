package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;

/**
 * the WaitObjective class implements the State interface and is the forth phase of the game. In this phase the player gets
 * his objectives.
 */
public class WaitObjective implements State{
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the WaitObjective method sets the game status at wait_objective and makes the player visualize his hand and the
     * public objectives.
     * @param controller is the controller of the game.
     */
    public WaitObjective(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_OBJECTIVE);
    }

    /**
     * the gamePhaseHandler method check if each player has their private objectives, then proceed to set up the playing phase
     * and proceed to change the state of the game.
     */
    @Override
    public synchronized void gamePhaseHandler() {
        int i = 0;
        for (Player p: controller.getGameInstance().getParticipants()) {
            if (p.getPrivateObjective() == null) {
                break;
            }
            i++;
        }
        if (i == controller.getNumOfPlayers()) {
            controller.getGameInstance().setupPlayPhase();
            controller.setState(new WaitPlaceCard(controller));
        }
    }
}
