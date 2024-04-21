package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.model.player.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {
    List<Player> pList = createListOfPlayer();
    Scoreboard sb = new Scoreboard(pList);

    /**
     * Tests the creation of the scoreboard object.
     */
    @Test
    void getParticipantsPointsTest() {
        System.out.println(sb.getParticipantsPoints());

        assertTrue(sb.getParticipantsPoints().containsKey(pList.get(0)));
        assertTrue(sb.getParticipantsPoints().containsKey(pList.get(1)));
        assertTrue(sb.getParticipantsPoints().containsKey(pList.get(2)));
        assertTrue(sb.getParticipantsPoints().containsKey(pList.get(3)));

        assertEquals(sb.getParticipantsPoints().get(pList.get(0)), 0);
        assertEquals(sb.getParticipantsPoints().get(pList.get(1)), 0);
        assertEquals(sb.getParticipantsPoints().get(pList.get(2)), 0);
        assertEquals(sb.getParticipantsPoints().get(pList.get(3)), 0);

    }

    /**
     * Tests the addPoints(token, points) method.
     */
    @Test
    void addPointsTest() {
        sb.addPoints(pList.get(0), 3);
        sb.addPoints(pList.get(1), 5);
        sb.addPoints(pList.get(0), 1);

        assertEquals(sb.getParticipantsPoints().get(pList.get(0)), 4);
        assertEquals(sb.getParticipantsPoints().get(pList.get(1)), 5);
        assertEquals(sb.getParticipantsPoints().get(pList.get(2)), 0);
        assertEquals(sb.getParticipantsPoints().get(pList.get(3)), 0);
        System.out.println(sb.getParticipantsPoints());
    }

    /**
     * Creates the list of participants.
     * @return the list of participants
     */
    public List<Player> createListOfPlayer () {
        List<Player> lOP = new ArrayList<>();
        lOP.add(new Player("Ricky"));
        lOP.add(new Player("Dario"));
        lOP.add(new Player("Alberto"));
        lOP.add(new Player("Marzio"));

        try {
            lOP.get(0).setToken(Token.BLUE);
            lOP.get(1).setToken(Token.RED);
            lOP.get(2).setToken(Token.GREEN);
            lOP.get(3).setToken(Token.YELLOW);
        } catch (AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }

        return lOP;
    }

}