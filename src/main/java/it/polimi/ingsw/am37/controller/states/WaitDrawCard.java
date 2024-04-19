package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;

public class WaitDrawCard implements State {
    private final GameController controller;

    public WaitDrawCard(GameController controller) {
        this.controller = controller;
    }

    @Override
    public void gamePhaseHandler() {
        if (controller.getGameInstance().getScoreboard().getParticipantsPoints().get(controller.getGameInstance().getCurrentTurn()) < 20) {
            controller.getGameInstance().nextTurn();
            controller.setState(new WaitPlaceCard(controller));
        } else {
            controller.getGameInstance().setupEndGame();
            controller.getGameInstance().nextTurn();
            controller.setState(new WaitEndGamePlace(controller));
        }
    }
}
