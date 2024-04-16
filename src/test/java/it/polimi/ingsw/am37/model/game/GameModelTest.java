package it.polimi.ingsw.am37.model.game;

import com.sun.javafx.charts.ChartLayoutAnimator;
import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.objective.ResourcesBoundObjective;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;

import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Corner;
import it.polimi.ingsw.am37.model.sides.Position;
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
    void playingTest() throws AlreadyAssignedException {
        g.playingPhase();

        assertTrue(g.getTurnCounter() > 0);
        assertTrue(g.getScoreboard().getParticipantsPoints().get(g.getCurrentTurn()) >= 20);
        assertEquals(g.getCurrentPhase(), GamePhase.ENDGAME);
    }

    /**
     * Tests the endgame Phase method by asserting that the calculation for the last turn is correct and that once the
     * last turn is reached the getGameWinner method is called, and we can declare the winner.
     */
    @Test
    void endgameTest() {}

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
    void getGameWinnerTest() throws NoCardsException, AlreadyAssignedException {
        Player p1=new Player("a");
        Player p2=new Player("b");
        Corner useless=new Corner(true,Resource.EMPTY);
        Back bsc=new Back(useless,useless,useless,useless,Resource.EMPTY);
        StartCard sc=new StartCard(0,null,bsc,null);
        //Kingdom k1=new Kingdom(sc, bsc);
        //Kingdom k2=new Kingdom(sc, bsc);



        ArrayList<Player> giocatori=new ArrayList<>();
        giocatori.add(p1);
        giocatori.add(p2);

        GameModel g = new GameModel(giocatori);
        /*p1.instantiateMyKingdom(sc,bsc);
        p2.instantiateMyKingdom(sc,bsc);

        p1.drawCardFromDeck(g.getRDeck());
        p1.drawCardFromDeck(g.getRDeck());
        p1.drawCardFromDeck(g.getRDeck());
        p2.drawCardFromDeck(g.getRDeck());
        p2.drawCardFromDeck(g.getRDeck());
        p2.drawCardFromDeck(g.getRDeck());

        ObjectiveCard red=;
        ObjectiveCard blue=;
        ObjectiveCard purp=;
        ObjectiveCard green=;

        g.set


        public obj
        private obj
         */
        g.preparationPhase();

        int x = 1;
        int y = 1;
        while (x < 15) {
            p1.placeCard(p1.getHand().get(0), p1.getHand().get(0).getBack(), new Position(x,y));

            x++;
            y++;

            p1.getHand().add((StandardCard) g.getRDeck().drawCard());
        }
        x = 1;
        y = 1;
        while (x < 15) {
            p2.placeCard(p2.getHand().get(0), p2.getHand().get(0).getBack(), new Position(x,y));

            x++;
            y++;

            p2.getHand().add((StandardCard) g.getRDeck().drawCard());
        }

        PlayerPoints[] listafinale=g.getGameWinner();
        if(listafinale[0].getPoints()==listafinale[1].getPoints()){
            assertTrue(listafinale[0].getNumOfCompletion()>=listafinale[1].getNumOfCompletion());
        }
        else{
            assertTrue(listafinale[0].getPoints()>listafinale[1].getPoints());
        }

    }
}