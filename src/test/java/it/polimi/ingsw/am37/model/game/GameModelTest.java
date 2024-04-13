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
    Player pl1 = new Player("Riccardo", Token.BLUE);
    GameModel g = new GameModel(createList());


    public List<Player> createList() {
        List<Player> playerList = new ArrayList<>();
        playerList.add(pl1);
        playerList.add(new Player("Dario", Token.RED));
        playerList.add(new Player("Alberto", Token.GREEN));
        return playerList;
    }

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
    }

    @Test
    void playingTest() throws AlreadyAssignedException {
        Player p = new Player("Riccardo", Token.BLUE);
        Player p2 = new Player("Dario", Token.RED);
        Player p3 = new Player("Alberto", Token.GREEN);
        GameController c = new GameController(p, 3);

        c.addPlayer(p2);
        c.addPlayer(p3);
        c.setGameInstance();

        GameModel gm = c.getGameInstance();
        gm.setController(c);

        assertNull(gm.getCurrentTurn());
        gm.playingPhase();

        assertTrue(gm.getTurnCounter() > 0);
        assertTrue(gm.getScoreboard().getParticipantsPoints().get(gm.getCurrentTurn().getToken()) >= 20);
        assertEquals(gm.getCurrentPhase(), GamePhase.ENDGAME);
    }

    @Test
    void endgameTest() {}

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