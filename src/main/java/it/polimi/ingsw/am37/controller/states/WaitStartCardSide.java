package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;

/**
 * the WaitStartCardSide class implements the State interface and manages the second state of the game, in this state to the player
 * will be given the starting card of which he will choose the side, and it will be initialized his kingdom.
 */
public class WaitStartCardSide implements State {
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the method WaitStartCardSide sets the status of the game at wait_start_card_side  and initialize the field
     * giving the Gold and Resource available cards, proceeding than to give the starting card to each player.
     * @param controller is the controller of the game.
     */
    public WaitStartCardSide(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_START_CARD_SIDE);

        this.controller.sendAvailable(this.controller.getGameInstance().getAvailableGCards(), this.controller.getGameInstance().getAvailableRCards());
        for (Player p: this.controller.getGameInstance().getParticipants())
            this.controller.sendStartCard(p, p.getStartCard());
    }

    /**
     * the method gamePhaseHandler checks if the kingdom of each player has been initialized, then proceed to change the state of the game.
     */
    @Override
    public void gamePhaseHandler() {
        int i = 0;
        for (Player p: controller.getGameInstance().getParticipants()) {
            if (p.getMyKingdom() == null) {
                break;
            }
            i++;
        }
        if (i == controller.getNumOfPlayers()) {
            controller.setState(new WaitToken(controller));
        }
    }
}
