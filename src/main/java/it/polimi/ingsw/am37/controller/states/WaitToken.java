package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.model.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;

public class WaitToken implements State {
    GameController controller;

    public WaitToken(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_TOKEN);
    }

    @Override
    public void gamePhaseHandler() {
        int i = 0;
        for (Player p: controller.getGameInstance().getParticipants()) {
            if (p.getToken() == null) {
                break;
            }
            i++;
        }
        if (i == controller.getNumOfPlayers()) {
            try {
                controller.getGameInstance().createHand();
                controller.getGameInstance().setPublicObjectives();
                controller.getGameInstance().giveObjectiveCards();
            } catch (NoCardsException | AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }
            controller.setState(new WaitObjective(controller));
        }
    }
}
