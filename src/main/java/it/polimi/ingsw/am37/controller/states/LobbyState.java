package it.polimi.ingsw.am37.controller.states;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.game.GameModel;

/**
 * the LobbyState class implements the State interface and is the first phase of the game, where the game is instanced.
 */
public class LobbyState implements State{
    /**
     * the controller attribute is used to have the access to the GameController functions.
     */
    private final GameController controller;

    /**
     * the LobbyState method initialize the controller.
     * @param controller is the controller of the game.
     */
    public LobbyState(GameController controller) {
        this.controller = controller;
    }

    /**
     * the gamePhaseHandler method instances the new game, and it initializes it, once the desired number of players
     * is reached.
     * @throws AlreadyAssignedException if we accidentally try to initialize the game twice.
     * @throws NoCardsException if there's no cards to set up the game.
     */
    @Override   //SYNCHRONIZED
    public synchronized void gamePhaseHandler() throws AlreadyAssignedException, NoCardsException {
        if (controller.getAddedPlayers().size() == controller.getNumOfPlayers()) {
            controller.setGameInstance(new GameModel(controller.getAddedPlayers()));
            controller.setGameStarted(true);

            controller.getGameInstance().setAvailableCards();
            controller.getGameInstance().giveStartCard();
            controller.getGameInstance().createHand();
            controller.getGameInstance().setPublicObjectives();
            controller.getGameInstance().giveObjectiveCards();


            controller.setState(new WaitStartCardSide(controller));
        }
    }
}
