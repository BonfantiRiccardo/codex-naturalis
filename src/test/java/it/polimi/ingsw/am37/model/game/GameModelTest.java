package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.exceptions.IncorrectUserActionException;
import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.objective.ResourcesBoundObjective;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    Player pl1 = new Player("Riccardo");
    GameModel g = new GameModel(createList());


    public List<Player> createList() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(pl1);
        playerList.add(new Player("Dario"));
        playerList.add(new Player("Alberto"));
        return playerList;
    }

    @Test
    void statusTest() {
        assertNull(g.getCurrentStatus());

        g.setCurrentStatus(GameStatus.WAIT_START_CARD_SIDE);

        assertEquals(g.getCurrentStatus(), GameStatus.WAIT_START_CARD_SIDE);
    }

    /**
     * Tests the preparationPhase method by checking that the Cards were drawn and assigned correctly.
     */
    @Test
    void preparationTest() throws NoCardsException, AlreadyAssignedException {
        g.setAvailableCards();
        boolean goldCheck = true;
        boolean resCheck = true;

        for (StandardCard gc : g.getAvailableGCards())
            if (gc.getId() < 40 || gc.getId() > 80){
                goldCheck = false;
                System.out.println("Gold card is out of bound. Its Id is: " + gc.getId());
                assertEquals(gc.getClass(), GoldCard.class);
                break;
            }
        assertTrue(goldCheck);

        for (StandardCard rc : g.getAvailableRCards())
            if (rc.getId() > 40){
                resCheck = false;
                System.out.println("Gold card is out of bound. Its Id is: " + rc.getId());
                assertEquals(rc.getClass(), ResourceCard.class);
                break;
            }
        assertTrue(resCheck);


        g.giveStartCard();
        for(Player p: g.getParticipants()) {
            assertNotNull(p.getStartCard());
        }


        g.createHand();
        int handRes;
        int handGold;

        for(Player p : g.getParticipants()){
            handRes = 0;
            handGold = 0;
            for(Card c : p.getHand())
                if(c.getId() >= 0 && c.getId() <= 40)
                    handRes++;
                else if(c.getId() >= 40 && c.getId() <= 80)
                    handGold++;
            assertEquals(2, handRes);
            assertEquals(1, handGold);
        }


        g.setPublicObjectives();
        boolean objCheck = true;

        for (ObjectiveCard oc : g.getPublicObjectives())
            if (oc.getId() < 87){
                objCheck = false;
                System.out.println("Objective card is out of bound. Its Id is: " + oc.getId());
                break;
            }
        assertTrue(objCheck);
    }

    /**
     * Tests the playing phase method by checking that the turn flow progresses as expected.
     */
    @Test
    void playingTest() {
        g.setupPlayPhase();
        assertEquals(1, g.getTurnCounter());
        assertSame(g.getParticipants().get(0), g.getCurrentTurn());

        g.nextTurn();
        assertEquals(2, g.getTurnCounter());
        assertSame(g.getParticipants().get(1), g.getCurrentTurn());
    }

    /**
     * Tests the endgame Phase method by asserting that the calculation for the last turn is correct and that once the
     * last turn is reached the getGameWinner method is called, and we can declare the winner.
     */
    @Test
    void endgameTest() {
        g.setupPlayPhase();
        for (int i = 1; i < 25; i++)
            g.nextTurn();

        g.setupEndGame();
        assertEquals(25, g.getTurnCounter());
        assertEquals(30, g.getLastTurn());

        g.setupPlayPhase();
        for (int i = 1; i < 30; i++)
            g.nextTurn();

        g.setupEndGame();
        assertEquals(30, g.getTurnCounter());
        assertEquals(33, g.getLastTurn());

        g.setupPlayPhase();
        for (int i = 1; i < 35; i++)
            g.nextTurn();

        g.setupEndGame();
        assertEquals(35, g.getTurnCounter());
        assertEquals(39, g.getLastTurn());
    }

    /**
     * Tests the set and get method for the disconnectedPlayer list.
     */
    @Test
    void reconnectionTest() {
        assertTrue(g.getDisconnectedPlayers().isEmpty());

        g.setDisconnected(pl1);

        assertEquals(1, g.getDisconnectedPlayers().size());
        assertTrue(g.getDisconnectedPlayers().contains(pl1));
        assertTrue(pl1.isDisconnected());

        g.reconnect(pl1);

        assertEquals(0, g.getDisconnectedPlayers().size());
        assertFalse(g.getDisconnectedPlayers().contains(pl1));
        assertFalse(pl1.isDisconnected());
    }

    /**
     * Tests the method that returns the final ranking of the match
     */
    @RepeatedTest (value = 100)//@Test
    void getGameWinnerTest() throws NoCardsException, AlreadyAssignedException, IncorrectUserActionException {
        Player p1 = new Player("a");
        Player p2 = new Player("b");
        ArrayList<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        GameModel g = new GameModel(players);

        g.giveStartCard();
        for (Player p: g.getParticipants()) {
            p.instantiateMyKingdom(p.getStartCard(), p.getStartCard().getFront());
        }
        g.createHand();

        ObjectiveCard privateObj;
        int i = 0;
        while (i < 2) {
            privateObj = g.getODeck().drawCard();
            if (privateObj.getClass().equals(ResourcesBoundObjective.class) && privateObj.getId() <= 98) {
                g.getParticipants().get(i).setPrivateObjective(privateObj);
                i++;
            }
        }

        g.setPublicObjectives();

        g.getScoreboard().addPoints(g.getParticipants().get(0), 10);
        PlayerPoints[] finalPoints = g.getGameWinner();

        assertEquals(finalPoints[0].getPlayer(), g.getParticipants().get(0));
        assertEquals(10, finalPoints[0].getPoints());

        int x = 1;
        int y = 1;
        while (x < 20) {
            p1.placeCard(p1.getHand().get(0), p1.getHand().get(0).getBack(), new Position(x,y));

            x++;
            y++;

            p1.getHand().add(g.getGDeck().drawCard());
        }
        x = 1;
        y = 1;
        while (x < 20) {
            p2.placeCard(p2.getHand().get(0), p2.getHand().get(0).getBack(), new Position(x,y));

            x++;
            y++;

            p2.getHand().add(g.getRDeck().drawCard());
        }

        g.getScoreboard().addPoints(g.getParticipants().get(1), 10);
        PlayerPoints[] finalPoints2 = g.getGameWinner();

        assertTrue(finalPoints2[0].getPoints() >= finalPoints2[1].getPoints());
        if (Objects.equals(finalPoints2[0].getPoints(), finalPoints2[1].getPoints()))
            assertTrue(finalPoints2[0].getNumOfCompletion() >= finalPoints2[1].getNumOfCompletion());

        assertTrue(finalPoints2[0].getPoints() >= 10);
        assertTrue(finalPoints2[1].getPoints() >= 10);
    }
}