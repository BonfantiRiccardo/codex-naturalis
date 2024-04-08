package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.game.Resource;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class CornerTest {
    Corner c = new Corner(true, Resource.FUNGI);
    Front front = new Front(new Corner(false, Resource.EMPTY),new Corner(true, Resource.ANIMAL),
            new Corner(true, Resource.PLANT),new Corner(true, Resource.EMPTY), Resource.PLANT, 2,
            createTable(), Bonus.CORNER);

    @Test
    void cornerGetsTest() {
        assertTrue(c.getVisibility());
        assertSame(c.getResource(), Resource.FUNGI);

        assertNull(c.getLinkedSide());
    }

    @Test
    void setLinkedSideTest() {
        c.setLinkedSide(front);

        assertSame(c.getLinkedSide(), front);

        System.out.println(c.toString());
    }


    public Hashtable<Resource, Integer> createTable() {
        Hashtable<Resource, Integer> placCond = new Hashtable<>();
        placCond.put(Resource.FUNGI, 3);
        return placCond;
    }
}