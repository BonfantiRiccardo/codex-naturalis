package it.polimi.ingsw.am37.controller;

import it.polimi.ingsw.am37.controller.states.*;
import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.game.GameStatus;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatesTest {
    Player p = new Player("Ricky");
    Player p2 = new Player("Marzio");
    GameController c = new GameController(p, 2);

    @Test
    void lobbyTest() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        assertEquals(c.getState().getClass(), LobbyState.class);
        c.getState().gamePhaseHandler();
        assertEquals(c.getState().getClass(), LobbyState.class);
        c.addPlayer(p2);

        assertEquals(c.getState().getClass(), WaitStartCardSide.class);

        c.setState(new LobbyState(c));
        assertThrows(RuntimeException.class, () -> c.getState().gamePhaseHandler());
    }

    @Test
    void waitStartCardTest() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        c.addPlayer(p2);
        assertEquals(c.getState().getClass(), WaitStartCardSide.class);

        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        assertEquals(c.getState().getClass(), WaitStartCardSide.class);

        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        assertEquals(c.getState().getClass(), WaitToken.class);
    }

    @Test
    void waitTokenTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c.addPlayer(p2);
        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        assertEquals(c.getState().getClass(), WaitToken.class);

        c.playerChoosesToken(p, Token.BLUE);
        c.playerChoosesToken(p2, Token.YELLOW);
        assertEquals(c.getState().getClass(), WaitObjective.class);

        c.setState(new WaitToken(c));
        assertThrows(AlreadyAssignedException.class, () -> c.getState().gamePhaseHandler());
    }

    @Test
    void waitObjectiveTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c.addPlayer(p2);
        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c.playerChoosesToken(p, Token.BLUE);
        c.playerChoosesToken(p2, Token.YELLOW);
        assertEquals(c.getState().getClass(), WaitObjective.class);

        c.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[0]);
        assertEquals(c.getState().getClass(), WaitObjective.class);

        c.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        assertEquals(c.getState().getClass(), WaitPlaceCard.class);

        assertEquals(1, c.getGameInstance().getTurnCounter());
        assertEquals(c.getGameInstance().getParticipants().getFirst(), c.getGameInstance().getCurrentTurn());
    }

    @Test
    void waitPlaceTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c.addPlayer(p2);
        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c.playerChoosesToken(p, Token.BLUE);
        c.playerChoosesToken(p2, Token.YELLOW);
        c.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[0]);
        c.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        assertEquals(c.getState().getClass(), WaitPlaceCard.class);

        List<Player> participants = c.getGameInstance().getParticipants();
        StandardCard toPlace = participants.get(0).getHand().get(0);
        c.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1));

        assertEquals(c.getState().getClass(), WaitDrawCard.class);
        c.playerDrawsCardFromDeck(participants.get(0), c.getGameInstance().getRDeck());

        c.setState(new WaitEndGamePlace(c));
        toPlace = participants.get(1).getHand().get(2);
        c.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(1,1));
        assertEquals(c.getState().getClass(), WaitEndGameDraw.class);

    }

    @Test
    void waitDrawTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c.addPlayer(p2);
        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c.playerChoosesToken(p, Token.BLUE);
        c.playerChoosesToken(p2, Token.YELLOW);
        c.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[0]);
        c.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        List<Player> participants = c.getGameInstance().getParticipants();
        StandardCard toPlace = participants.get(0).getHand().get(0);
        c.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1));
        assertEquals(c.getState().getClass(), WaitDrawCard.class);

        c.playerDrawsCardFromDeck(participants.get(0), c.getGameInstance().getRDeck());
        assertEquals(c.getState().getClass(), WaitPlaceCard.class);
        assertEquals(2, c.getGameInstance().getTurnCounter());
    }

    @Test
    void waitEndDrawTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c.addPlayer(p2);
        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c.playerChoosesToken(p, Token.BLUE);
        c.playerChoosesToken(p2, Token.YELLOW);
        c.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[0]);
        c.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        List<Player> participants = c.getGameInstance().getParticipants();
        StandardCard toPlace = participants.get(0).getHand().get(0);
        c.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1));
        c.playerDrawsCardFromDeck(participants.get(0), c.getGameInstance().getRDeck());

        c.getGameInstance().getScoreboard().addPoints(participants.get(1), 20);
        toPlace = participants.get(1).getHand().get(1);
        c.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(1,1));
        c.playerDrawsCardFromDeck(participants.get(1), c.getGameInstance().getRDeck());
        assertEquals(c.getState().getClass(), WaitEndGamePlace.class);
        assertEquals(3, c.getGameInstance().getTurnCounter());
        assertEquals(4, c.getGameInstance().getLastTurn());

        toPlace = participants.get(0).getHand().get(0);
        c.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(2,2));
        c.playerDrawsCardFromDeck(participants.get(0), c.getGameInstance().getRDeck());
        assertEquals(c.getState().getClass(), WaitEndGamePlace.class);

        toPlace = participants.get(1).getHand().get(1);
        c.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(2,2));
        c.playerDrawsCardFromDeck(participants.get(1), c.getGameInstance().getRDeck());

        assertEquals(c.getState().getClass(), GameOver.class);
    }

    @Test
    void gameOverTest() throws IncorrectUserActionException, WrongGamePhaseException, AlreadyAssignedException, NoCardsException {
        c.addPlayer(p2);
        c.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getFront());
        c.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getFront());
        c.playerChoosesToken(p, Token.BLUE);
        c.playerChoosesToken(p2, Token.YELLOW);
        c.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[0]);
        c.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        List<Player> participants = c.getGameInstance().getParticipants();
        StandardCard toPlace = participants.get(0).getHand().get(0);
        c.playerPlacesCard(participants.get(0), toPlace, toPlace.getBack(), new Position(1,1));
        c.playerDrawsCardFromDeck(participants.get(0), c.getGameInstance().getRDeck());

        c.getGameInstance().getScoreboard().addPoints(participants.get(1), 20);
        toPlace = participants.get(1).getHand().get(1);
        c.playerPlacesCard(participants.get(1), toPlace, toPlace.getBack(), new Position(1,1));
        c.playerDrawsCardFromDeck(participants.get(1), c.getGameInstance().getRDeck());

        c.setState(new GameOver(c));
        assertEquals(c.getState().getClass(), GameOver.class);
        assertEquals(c.getGameInstance().getCurrentStatus(), GameStatus.OVER);

        c.getState().gamePhaseHandler();
        assertEquals(c.getState().getClass(), GameOver.class);
        assertEquals(c.getGameInstance().getCurrentStatus(), GameStatus.OVER);
    }
}
