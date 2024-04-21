package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.game.GameModel;

public class LobbyState implements State{
    private final GameController controller;
    public LobbyState(GameController controller) {
        this.controller = controller;
        //this.controller.getGameInstance().setCurrentStatus(GameStatus.LOBBY);
    }

    @Override
    public void gamePhaseHandler() {
        if (controller.getAddedPlayers().size() == controller.getNumOfPlayers()) {
            try {
                controller.setGameInstance(new GameModel(controller.getAddedPlayers(), controller));
                controller.setGameStarted(true);
                controller.getGameInstance().setAvailableCards();
                controller.getGameInstance().giveStartCard();
                controller.setState(new WaitStartCardSide(controller));
            } catch (NoCardsException | AlreadyAssignedException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
