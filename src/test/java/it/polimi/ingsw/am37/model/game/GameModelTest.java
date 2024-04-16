package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;

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

    /**
     * Tests the preparationPhase method by checking that the Cards were drawn and assigned correctly.
     */
    @Test
    void preparationTest() throws NoCardsException, AlreadyAssignedException, InterruptedException {
        g.preparationPhase();

        boolean objCheck = true;
        boolean goldCheck = true;
        boolean resCheck = true;
        int handRes;
        int handGold;

        for (ObjectiveCard oc : g.getPublicObjectives())
            if (oc.getId() < 87){
                objCheck = false;
                System.out.println("Objective card is out of bound. Its Id is: " + oc.getId());
                break;
            }
        assertTrue(objCheck);

        for (StandardCard gc : g.getAvailableGCards())
            if (gc.getId() < 40 || gc.getId() > 80){
                goldCheck = false;
                System.out.println("Gold card is out of bound. Its Id is: " + gc.getId());
                break;
            }
        assertTrue(goldCheck);

        for (StandardCard rc : g.getAvailableRCards())
            if (rc.getId() > 40){
                resCheck = false;
                System.out.println("Gold card is out of bound. Its Id is: " + rc.getId());
                break;
            }
        assertTrue(resCheck);

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

        for(Player p : g.getParticipants()){
            assertNotNull(p.getToken());
            assertNotEquals(p.getToken(), Token.BLACK);
        }
    }

    /**
     * Tests the playing phase method by checking that the turn flow progresses as expected.
     */
    @Test
    void playingTest()  {
        g.playingPhase();

        assertTrue(g.getTurnCounter() > 0);
        assertTrue(g.getScoreboard().getParticipantsPoints().get(g.getCurrentTurn()) >= 20);
        assertEquals(g.getCurrentPhase(), GamePhase.ENDGAME);
        System.out.println("current: " + g.getTurnCounter());
        System.out.println("current player:" + g.getCurrentTurn().getNickname());
    }

    /**
     * Tests the endgame Phase method by asserting that the calculation for the last turn is correct and that once the
     * last turn is reached the getGameWinner method is called, and we can declare the winner.
     */
    @Test
    void endgameTest() throws NoCardsException, AlreadyAssignedException {
        g.preparationPhase();
        g.playingPhase();
        assertEquals(95,g.getTurnCounter());
        g.endGamePhase();
        assertEquals(100, g.getTurnCounter());

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

}