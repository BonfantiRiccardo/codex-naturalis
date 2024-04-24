package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;

public class WaitStartCardSide implements State {
    private final GameController controller;

    public WaitStartCardSide(GameController controller) {
        this.controller = controller;
        this.controller.getGameInstance().setCurrentStatus(GameStatus.WAIT_START_CARD_SIDE);

        this.controller.sendAvailable(this.controller.getGameInstance().getAvailableGCards(), this.controller.getGameInstance().getAvailableRCards());
        for (Player p: this.controller.getGameInstance().getParticipants())
            this.controller.sendStartCard(p, p.getStartCard());
    }

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
