package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.controller.states.*;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.game.GameModel;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {
    Player p = new Player("Ricky");
    Player p2 = new Player("Dario");
    Player p3 = new Player("Alberto");
    GameController c = new GameController(p, 3);
    GameController c2 = new GameController(p, 3);
    @Test
    void createControllerTest() {
        assertSame(c.getAddedPlayers().get(0), p);
        assertEquals(3, c.getNumOfPlayers());
        assertFalse(c.isGameStarted());
        assertEquals(c.getState().getClass(), LobbyState.class);
    }

    public List<Player> createList() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(p);
        playerList.add(new Player("Dario"));
        playerList.add(new Player("Alberto"));
        return playerList;
    }

    @Test
    void setGetGameInstance() throws AlreadyAssignedException {
        GameModel g = new GameModel(createList(), c);
        c.setGameInstance(g);
        assertEquals(c.getGameInstance(), g);

        assertThrows(AlreadyAssignedException.class, () -> c.setGameInstance(g));
    }

    @Test
    void fillLobby() throws IncorrectUserActionException, WrongGamePhaseException {
        c2.addPlayer(p2);
        assertThrows(IncorrectUserActionException.class, () -> c2.addPlayer(new Player("Dario")));
        c2.addPlayer(p3);

        assertThrows(WrongGamePhaseException.class, () -> c2.addPlayer(new Player("Marzio")));
        assertTrue(c2.isGameStarted());
        assertEquals(c2.getState().getClass(), WaitStartCardSide.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_START_CARD_SIDE);
        assertNotNull(c2.getGameInstance().getController());
        assertEquals(c2, c2.getGameInstance().getController());
        assertNotNull(c2.getGameInstance().getAvailableGCards());
        assertNotNull(c2.getGameInstance().getAvailableRCards());
        for (Player p: c2.getGameInstance().getParticipants())
            assertNotNull(p.getStartCard());
    }

    @Test
    void chooseStartCardSide() throws IncorrectUserActionException, WrongGamePhaseException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);
        c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());

        assertThrows(RuntimeException.class, () -> c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront()));

        assertThrows(IncorrectUserActionException.class, () -> c2.playerChoosesStartCardSide(p, c2.getGameInstance().getSDeck().drawCard(), p.getStartCard().getFront()));

        c2.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());

        assertThrows(WrongGamePhaseException.class, () -> c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront()));

        assertTrue(c2.isGameStarted());
        assertEquals(c2.getState().getClass(), WaitToken.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_TOKEN);

        for (Player p: c2.getGameInstance().getParticipants())
            assertNotNull(p.getMyKingdom());
    }

    @Test
    void chooseToken() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);
        c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());

        c2.playerChoosesToken(p3, Token.BLUE);

        assertThrows(AlreadyAssignedException.class, () -> c2.playerChoosesToken(p3, Token.YELLOW));
        assertThrows(IncorrectUserActionException.class, () -> c2.playerChoosesToken(p2, Token.BLACK));
        assertThrows(IncorrectUserActionException.class, () -> c2.playerChoosesToken(p2, Token.BLUE));

        c2.playerChoosesToken(p2, Token.RED);
        c2.playerChoosesToken(p, Token.GREEN);

        assertThrows(WrongGamePhaseException.class, () -> c2.playerChoosesToken(p, Token.GREEN));

        assertTrue(c2.isGameStarted());
        assertEquals(c2.getState().getClass(), WaitObjective.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_OBJECTIVE);

        for (Player p: c2.getGameInstance().getParticipants())
            assertNotNull(p.getToken());
    }

    @Test
    void chooseObjective() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);
        c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c2.playerChoosesToken(p3, Token.BLUE);
        c2.playerChoosesToken(p2, Token.RED);
        c2.playerChoosesToken(p, Token.GREEN);

        c2.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[0]);
        assertThrows(AlreadyAssignedException.class, () -> c2.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]));
        assertThrows(IncorrectUserActionException.class, () -> c2.playerChoosesObjective(p2, c2.getGameInstance().getODeck().drawCard()));

        c2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[1]);
        c2.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]);

        assertThrows(WrongGamePhaseException.class, () -> c2.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]));

        assertTrue(c2.isGameStarted());
        assertEquals(c2.getState().getClass(), WaitPlaceCard.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_PLACE);

        for (Player p: c2.getGameInstance().getParticipants())
            assertNotNull(p.getPrivateObjective());
    }

}