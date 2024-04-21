package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;

public class WaitObjective implements State{
    private final GameController controller;

    public WaitObjective(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_OBJECTIVE);
    }

    @Override
    public void gamePhaseHandler() {
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
