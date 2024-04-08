package it.polimi.ingsw.am37.model.sides;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {
    Position p = new Position(-3,2);
    @Test
    void positionGetsTest() {
        assertEquals(-3, p.getX());
        assertEquals(2, p.getY());
    }

}