package it.polimi.ingsw.am37.model.game;

import it.polimi.ingsw.am37.model.player.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreboardTest {
    Scoreboard sb = new Scoreboard(createListOfPlayer());

    @Test
    void getParticipantsPointsTest() {
        System.out.println(sb.getParticipantsPoints());

        assertTrue(sb.getParticipantsPoints().containsKey(Token.BLUE));
        //...
        assertFalse(sb.getParticipantsPoints().containsKey(Token.BLACK));

        assertEquals(sb.getParticipantsPoints().get(Token.BLUE), 0);
        //...
    }

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


    public List<Player> createListOfPlayer () {
        List<Player> lOP = new ArrayList<>();
        lOP.add(new Player("Ricky", Token.BLUE));
        lOP.add(new Player("Davide", Token.RED));
        lOP.add(new Player("Lorenzo", Token.GREEN));
        lOP.add(new Player("Luisa", Token.YELLOW));
        return lOP;
    }

}