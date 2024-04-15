package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.cards.objective.DiagonalDown;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.game.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    Position p = new Position(-3,2);

    /**
     * Test the get and equals method of the position.
     */
    @Test
    void positionGetsTest() {
        assertEquals(-3, p.getX());
        assertEquals(2, p.getY());

        assertEquals(p, new Position(-3,2));

        ObjectiveCard c = new DiagonalDown(100, 1, Resource.ANIMAL, Resource.ANIMAL);

        assertNotEquals(p, c);
    }

}