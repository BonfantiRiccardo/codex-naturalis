package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {
    Scoreboard sb = new Scoreboard(createListOfPlayer());

    /**
     * Tests the creation of the scoreboard object.
     */
    @Test
    void getParticipantsPointsTest() {
        System.out.println(sb.getParticipantsPoints());

        assertTrue(sb.getParticipantsPoints().containsKey(Token.BLUE));
        assertTrue(sb.getParticipantsPoints().containsKey(Token.GREEN));
        assertTrue(sb.getParticipantsPoints().containsKey(Token.YELLOW));
        assertTrue(sb.getParticipantsPoints().containsKey(Token.RED));

        assertFalse(sb.getParticipantsPoints().containsKey(Token.BLACK));

        assertEquals(sb.getParticipantsPoints().get(Token.BLUE), 0);
        assertEquals(sb.getParticipantsPoints().get(Token.GREEN), 0);
        assertEquals(sb.getParticipantsPoints().get(Token.YELLOW), 0);
        assertEquals(sb.getParticipantsPoints().get(Token.RED), 0);

    }

    /**
     * Tests the addPoints(token, points) method.
     */
    @Test
    void addPointsTest() {
        sb.addPoints(Token.BLUE, 3);
        sb.addPoints(Token.YELLOW, 5);
        sb.addPoints(Token.BLUE, 1);

        assertEquals(sb.getParticipantsPoints().get(Token.BLUE), 4);
        assertEquals(sb.getParticipantsPoints().get(Token.YELLOW), 5);
        assertEquals(sb.getParticipantsPoints().get(Token.RED), 0);
        assertEquals(sb.getParticipantsPoints().get(Token.GREEN), 0);
        System.out.println(sb.getParticipantsPoints());
    }

    /**
     * Creates the list of participants.
     * @return the list of participants
     */
    public List<Player> createListOfPlayer () {
        List<Player> lOP = new ArrayList<>();
        lOP.add(new Player("Ricky", Token.BLUE));
        lOP.add(new Player("Dario", Token.RED));
        lOP.add(new Player("Alberto", Token.GREEN));
        lOP.add(new Player("Marzio", Token.YELLOW));
        return lOP;
    }

}