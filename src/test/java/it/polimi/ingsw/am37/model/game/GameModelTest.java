package it.polimi.ingsw.am37.model.game;

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
    GameModel g = new GameModel(createList());
    //private final CardCreator cc = new CardCreator();

    @Test
    public void preparationTest() throws NoCardsException, AlreadyAssignedException, InterruptedException {
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



        public List<Player> createList() {
            List<Player> playerList = new ArrayList<>();
            playerList.add(new Player("Riccardo", Token.BLUE));
            playerList.add(new Player("Dario", Token.RED));
            playerList.add(new Player("Alberto", Token.GREEN));
            return playerList;
        }
    }