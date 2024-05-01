package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.controller.states.*;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.GameModel;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Position;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.net.Socket;
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
    void setGetGameInstanceTest() throws AlreadyAssignedException {
        GameModel g = new GameModel(createList());
        c.setGameInstance(g);
        assertEquals(c.getGameInstance(), g);

        assertThrows(AlreadyAssignedException.class, () -> c.setGameInstance(g));
    }

    @Test
    void fillLobbyTest() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        c2.addPlayer(p2);
        assertThrows(IncorrectUserActionException.class, () -> c2.addPlayer(new Player("Dario")));
        c2.addPlayer(p3);

        assertThrows(WrongGamePhaseException.class, () -> c2.addPlayer(new Player("Marzio")));
        assertTrue(c2.isGameStarted());
        assertEquals(c2.getState().getClass(), WaitStartCardSide.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_START_CARD_SIDE);
        assertNotNull(c2.getGameInstance().getAvailableGCards());
        assertNotNull(c2.getGameInstance().getAvailableRCards());
        for (Player p: c2.getGameInstance().getParticipants())
            assertNotNull(p.getStartCard());
    }

    @Test
    void chooseStartCardSideTest() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);
        c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());

        assertThrows(AlreadyAssignedException.class, () -> c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront()));

        assertThrows(IncorrectUserActionException.class, () -> c2.playerChoosesStartCardSide(p2, c2.getGameInstance().getSDeck().drawCard(), p.getStartCard().getFront()));

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
    void chooseTokenTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
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
    void chooseObjectiveTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
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

        assertEquals(1, c2.getGameInstance().getTurnCounter());
        assertEquals(c2.getGameInstance().getParticipants().get(0), c2.getGameInstance().getCurrentTurn());
    }

    @Test
    void placeCardTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);
        c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c2.playerChoosesToken(p3, Token.BLUE);
        c2.playerChoosesToken(p2, Token.RED);
        c2.playerChoosesToken(p, Token.GREEN);
        c2.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[0]);
        c2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[1]);
        c2.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]);

        //GET ORDER OF PLAYERS BECAUSE I DON'T KNOW THE ORDER (IT CHANGES EVERY TIME).
        List<Player> participants = c2.getGameInstance().getParticipants();
        StandardCard toPlace = participants.get(0).getHand().get(0);
        c2.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1));

        assertTrue(toPlace.getBack().getIsPlaced());
        assertTrue(participants.get(0).getMyKingdom().getPlacedSides().contains(toPlace.getBack()));

        assertThrows(IncorrectUserActionException.class, () -> c2.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(1,1)));
        assertThrows(WrongGamePhaseException.class, () -> c2.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1)));

        assertEquals(c2.getState().getClass(), WaitDrawCard.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_DRAW);
    }

    @Test
    void drawCardEndGameTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);
        c2.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        c2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c2.playerChoosesToken(p3, Token.BLUE);
        c2.playerChoosesToken(p2, Token.RED);
        c2.playerChoosesToken(p, Token.GREEN);
        c2.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[0]);
        c2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[1]);
        c2.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]);
        List<Player> participants = c2.getGameInstance().getParticipants();
        StandardCard toPlace = participants.get(0).getHand().get(0);
        c2.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1));

        //FROM AVAILABLE
        StandardCard toDrawR = c2.getGameInstance().getAvailableRCards().get(0);
        assertThrows(IncorrectUserActionException.class, () -> c2.playerDrawsCardFromAvailable(participants.get(1), toDrawR));

        c2.playerDrawsCardFromAvailable(participants.get(0), toDrawR);

        assertTrue(participants.get(0).getHand().contains(toDrawR));
        assertFalse(c2.getGameInstance().getAvailableRCards().contains(toDrawR));
        assertEquals(c2.getState().getClass(), WaitPlaceCard.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_PLACE);
        assertThrows(WrongGamePhaseException.class, () -> c2.playerDrawsCardFromAvailable(participants.get(1), toDrawR));

        toPlace = participants.get(1).getHand().get(0);
        c2.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(1,1));

        StandardCard toDrawG = c2.getGameInstance().getAvailableGCards().get(0);
        c2.playerDrawsCardFromAvailable(participants.get(1), toDrawG);

        assertTrue(participants.get(1).getHand().contains(toDrawG));
        assertFalse(c2.getGameInstance().getAvailableGCards().contains(toDrawG));


        //FROM DECK
        assertThrows(WrongGamePhaseException.class, () -> c2.playerDrawsCardFromDeck(participants.get(2), c2.getGameInstance().getRDeck()));
        toPlace = participants.get(2).getHand().get(0);
        c2.playerPlacesCard(participants.get(2), toPlace, toPlace.getBack(), new Position(1,1));
        Back b = c2.getGameInstance().getRDeck().firstBack();

        c2.playerDrawsCardFromDeck(participants.get(2), c2.getGameInstance().getRDeck());
        assertThrows(IncorrectUserActionException.class, () -> c2.playerDrawsCardFromDeck(participants.get(1), c2.getGameInstance().getRDeck()));

        assertEquals(3, participants.get(2).getHand().size());
        assertNotEquals(c2.getGameInstance().getRDeck().firstBack(), b);

        toPlace = participants.get(0).getHand().get(0);
        c2.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(2,2));
        b = c2.getGameInstance().getGDeck().firstBack();

        assertThrows(IncorrectUserActionException.class, () -> c2.playerDrawsCardFromDeck(participants.get(1), c2.getGameInstance().getGDeck()));
        c2.playerDrawsCardFromDeck(participants.get(0), c2.getGameInstance().getGDeck());
        assertThrows(WrongGamePhaseException.class, () -> c2.playerDrawsCardFromDeck(participants.get(1), c2.getGameInstance().getGDeck()));

        assertEquals(3, participants.get(0).getHand().size());
        assertNotEquals(c2.getGameInstance().getGDeck().firstBack(), b);


        //START ENDGAME PHASE
        c2.getGameInstance().getScoreboard().addPoints(participants.get(1), 21);        //SHOULD TRIGGER THE CHECK IN WAIT DRAW CARD

        toPlace = participants.get(1).getHand().get(0);
        c2.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(2,2));
        c2.playerDrawsCardFromDeck(participants.get(1), c2.getGameInstance().getGDeck());

        assertEquals(c2.getState().getClass(), WaitEndGamePlace.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_PLACE);

        assertEquals(c2.getGameInstance().getTurnCounter() + 3, c2.getGameInstance().getLastTurn());

        toPlace = participants.get(2).getHand().get(0);
        c2.playerPlacesCard(participants.get(2), toPlace, toPlace.getBack(), new Position(2,2));

        assertEquals(c2.getState().getClass(), WaitEndGameDraw.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_DRAW);
        c2.playerDrawsCardFromDeck(participants.get(2), c2.getGameInstance().getGDeck());
        assertEquals(c2.getState().getClass(), WaitEndGamePlace.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.WAIT_PLACE);

        //GAME OVER
        c2.getGameInstance().nextTurn();
        c2.getGameInstance().nextTurn();

        toPlace = participants.get(2).getHand().get(0);
        c2.playerPlacesCard(participants.get(2), toPlace, toPlace.getBack(), new Position(3,3));
        c2.playerDrawsCardFromDeck(participants.get(2), c2.getGameInstance().getRDeck());
        assertEquals(c2.getState().getClass(), GameOver.class);
        assertEquals(c2.getGameInstance().getCurrentStatus(), GameStatus.OVER);
    }

    @Test
    void virtualViewCreationTest() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        assertNotNull(c2.getPlayerViews().get(p));
        c2.addPlayer(p2);
        assertNotNull(c2.getPlayerViews().get(p2));
        c2.addPlayer(p3);
        assertNotNull(c2.getPlayerViews().get(p3));
    }

    @Test
    void disconnectionTest() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        c2.addPlayer(p2);
        c2.addPlayer(p3);

        c2.handleDisconnection(p);

        assertTrue(p.isDisconnected());
        assertTrue(c2.getGameInstance().getDisconnectedPlayers().contains(p));

        c2.handleReconnection(p);

        assertFalse(p.isDisconnected());
        assertFalse(c2.getGameInstance().getDisconnectedPlayers().contains(p));
    }
}