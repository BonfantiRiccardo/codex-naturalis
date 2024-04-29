package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;

/**
 * the WaitToken class implements the State interface and manages the third phase of the game. In this phase the player will
 * pick his token.
 */
public class WaitToken implements State {
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    GameController controller;

    /**
     * the WaitToken method sets the status of the game in wait_token
     * @param controller is the controller of the game
     */
    public WaitToken(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_TOKEN);
    }

    /**
     * the gamePhaseHandler method checks if any player already has a token, if not, it creates the hand of each player,
     * sets the public objectives and gives the personal objective cards.
     * @throws NoCardsException if there's no cards available during the creation of the hands or during the setting of the objectives.
     * @throws AlreadyAssignedException if the hands or the personal objectives were already given to a player.
     */
    @Override
    public void gamePhaseHandler() throws NoCardsException, AlreadyAssignedException {
        int i = 0;
        for (Player p: controller.getGameInstance().getParticipants()) {
            if (p.getToken() == null) {
                break;
            }
            i++;
        }
        if (i == controller.getNumOfPlayers()) {
            controller.getGameInstance().createHand();
            controller.getGameInstance().setPublicObjectives();
            controller.getGameInstance().giveObjectiveCards();

            controller.setState(new WaitObjective(controller));
        }
    }
}
