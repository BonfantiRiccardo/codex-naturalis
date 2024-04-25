package it.polimi.ingsw.am37.virtualview;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VirtualViewTest {
    Player p = new Player("Ricky");
    Player p2 = new Player("Dario");
    Player p3 = new Player("Alberto");
    GameController c = new GameController(p, 3);
    VirtualView v1;
    VirtualView v2;
    VirtualView v3;

    @BeforeEach
    void virtualViewCreation() throws IncorrectUserActionException, WrongGamePhaseException, NoCardsException, AlreadyAssignedException {
        v1 = c.getPlayerViews().get(p);
        assertNotNull(c.getPlayerViews().get(p));
        c.addPlayer(p2);
        assertNotNull(c.getPlayerViews().get(p2));
        v2 = c.getPlayerViews().get(p2);
        c.addPlayer(p3);
        assertNotNull(c.getPlayerViews().get(p3));
        v3 = c.getPlayerViews().get(p3);
    }

    @Test
    void placingStartCardTest() {
        v3.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        assertTrue(p3.getMyKingdom().getPlacedSides().contains(p3.getStartCard().getFront()));

        v3.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getBack());  //prints "The Kingdom cannot be created twice."
        v1.playerChoosesStartCardSide(p, p2.getStartCard(), p2.getStartCard().getBack());   //prints "The start card you want to place is not the one assigned to you."
        v1.playerChoosesStartCardSide(p, p.getStartCard(), p2.getStartCard().getBack());    //prints "The side you want to place does not correspond to the one of your start card."
        assertNull(p.getMyKingdom());

        v1.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
        assertTrue(p.getMyKingdom().getPlacedSides().contains(p.getStartCard().getBack()));

        v2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getBack());
        assertTrue(p2.getMyKingdom().getPlacedSides().contains(p2.getStartCard().getBack()));

        v2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getBack());  //prints "You cannot place your start card now."
    }

    @Test
    void choosingTokenTest() {
        v3.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        v1.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
        v2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getBack());

        v2.playerChoosesToken(p2, Token.YELLOW);
        assertEquals(p2.getToken(), Token.YELLOW);

        v2.playerChoosesToken(p2, Token.BLUE);  //prints "You already chose your token."
        v1.playerChoosesToken(p, Token.YELLOW); //prints "The token has been chosen by another player."
        v1.playerChoosesToken(p, Token.BLACK);  //prints "You can't choose the black token."
        assertNull(p.getToken());

        v1.playerChoosesToken(p, Token.BLUE);
        assertEquals(p.getToken(), Token.BLUE);

        v3.playerChoosesToken(p3, Token.RED);
        assertEquals(p3.getToken(), Token.RED);

        v3.playerChoosesToken(p3, Token.GREEN); //prints "You cannot choose your token now."
    }

    @Test
    void choosingPrivateObjectivesTest() {
        v3.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        v1.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
        v2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getBack());
        v2.playerChoosesToken(p2, Token.YELLOW);
        v1.playerChoosesToken(p, Token.BLUE);
        v3.playerChoosesToken(p3, Token.RED);

        v1.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        assertEquals(p.getObjectivesToChooseFrom()[1], p.getPrivateObjective());

        v1.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[0]);     //prints "The player has already chosen his private objective."
        v3.playerChoosesObjective(p3, p2.getObjectivesToChooseFrom()[0]);   //prints "The objective you chose was not one of the two assigned to you."
        assertNull(p3.getPrivateObjective());


        v3.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]);
        assertEquals(p3.getObjectivesToChooseFrom()[0], p3.getPrivateObjective());

        v2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[1]);
        assertEquals(p2.getObjectivesToChooseFrom()[1], p2.getPrivateObjective());

        v2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[0]);   //prints "You cannot choose your objective now."
    }

    @Test
    void placingCardTest() {
        v3.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        v1.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
        v2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getBack());
        v2.playerChoosesToken(p2, Token.YELLOW);
        v1.playerChoosesToken(p, Token.BLUE);
        v3.playerChoosesToken(p3, Token.RED);
        v1.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        v3.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]);
        v2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[1]);

        Player first = c.getGameInstance().getCurrentTurn();
        Player notHisTurn = c.getGameInstance().getParticipants().get(2);

        c.getPlayerViews().get(notHisTurn).playerPlacesCard(notHisTurn, notHisTurn.getHand().get(0), notHisTurn.getHand().get(0).getBack(), new Position(1,1));
        //prints "It is not your turn."
        assertFalse(notHisTurn.getMyKingdom().getPlacedSides().contains(notHisTurn.getHand().get(0).getBack()));

        c.getPlayerViews().get(first).playerDrawsCardFromDeck(first, c.getGameInstance().getRDeck());
        //prints "You cannot draw a card now"

        c.getPlayerViews().get(first).playerPlacesCard(first, first.getHand().get(1), first.getHand().get(1).getBack(), new Position(2,2));
        //prints "You cannot place the Card in this position."

        c.getPlayerViews().get(first).playerPlacesCard(first, notHisTurn.getHand().get(1), notHisTurn.getHand().get(1).getBack(), new Position(2,2));
        //prints "You do not possess this Card, you cannot place it."

        StandardCard toPlace = first.getHand().get(1);
        c.getPlayerViews().get(first).playerPlacesCard(first, toPlace, toPlace.getBack(), new Position(1,1));
        assertTrue(first.getMyKingdom().getPlacedSides().contains(toPlace.getBack()));

        c.getPlayerViews().get(first).playerPlacesCard(first, first.getHand().get(0), first.getHand().get(0).getBack(), new Position(1,1));
        //prints "You cannot place a card now."
    }

    @Test
    void drawingCardTest() {
        v3.playerChoosesStartCardSide(p3, p3.getStartCard(), p3.getStartCard().getFront());
        v1.playerChoosesStartCardSide(p, p.getStartCard(), p.getStartCard().getBack());
        v2.playerChoosesStartCardSide(p2, p2.getStartCard(), p2.getStartCard().getBack());
        v2.playerChoosesToken(p2, Token.YELLOW);
        v1.playerChoosesToken(p, Token.BLUE);
        v3.playerChoosesToken(p3, Token.RED);
        v1.playerChoosesObjective(p, p.getObjectivesToChooseFrom()[1]);
        v3.playerChoosesObjective(p3, p3.getObjectivesToChooseFrom()[0]);
        v2.playerChoosesObjective(p2, p2.getObjectivesToChooseFrom()[1]);

        Player nowHisTurn = c.getGameInstance().getCurrentTurn();
        Player notHisTurn = c.getGameInstance().getParticipants().get(2);
        StandardCard toPlace = nowHisTurn.getHand().get(1);
        c.getPlayerViews().get(nowHisTurn).playerPlacesCard(nowHisTurn, toPlace, toPlace.getBack(), new Position(1,1));

        c.getPlayerViews().get(notHisTurn).playerDrawsCardFromDeck(notHisTurn, c.getGameInstance().getRDeck());
        //prints "It is not your turn."

        c.getPlayerViews().get(nowHisTurn).playerPlacesCard(nowHisTurn, nowHisTurn.getHand().get(0), nowHisTurn.getHand().get(0).getBack(), new Position(2,2));
        //prints "You cannot place a card now."

        assertEquals(2, nowHisTurn.getHand().size());
        c.getPlayerViews().get(nowHisTurn).playerDrawsCardFromDeck(nowHisTurn, c.getGameInstance().getRDeck());
        assertEquals(3, nowHisTurn.getHand().size());

        nowHisTurn = c.getGameInstance().getCurrentTurn();
        toPlace = nowHisTurn.getHand().get(2);
        c.getPlayerViews().get(nowHisTurn).playerPlacesCard(nowHisTurn, toPlace, toPlace.getBack(), new Position(1,1));

        assertEquals(2, nowHisTurn.getHand().size());
        c.getPlayerViews().get(nowHisTurn).playerDrawsCardFromDeck(nowHisTurn, c.getGameInstance().getGDeck());
        assertEquals(3, nowHisTurn.getHand().size());

        nowHisTurn = c.getGameInstance().getCurrentTurn();
        toPlace = nowHisTurn.getHand().get(0);
        c.getPlayerViews().get(nowHisTurn).playerPlacesCard(nowHisTurn, toPlace, toPlace.getBack(), new Position(1,1));

        assertEquals(2, nowHisTurn.getHand().size());
        c.getPlayerViews().get(nowHisTurn).playerDrawsCardFromAvailable(nowHisTurn, c.getGameInstance().getAvailableRCards().get(0));
        assertEquals(3, nowHisTurn.getHand().size());
        assertEquals(2, c.getGameInstance().getAvailableRCards().size());
    }

}