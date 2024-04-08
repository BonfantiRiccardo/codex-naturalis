package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.game.Resource;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class SideTest {
    Side side = new Front(new Corner(false, Resource.EMPTY),new Corner(false, Resource.EMPTY),
            new Corner(true, Resource.PLANT),new Corner(true, Resource.EMPTY), Resource.PLANT,3,
            createTable(), null);
    Front front = new Front(new Corner(false, Resource.EMPTY),new Corner(true, Resource.ANIMAL),
            new Corner(true, Resource.PLANT),new Corner(true, Resource.EMPTY), Resource.PLANT,2,
            createTable(), Bonus.CORNER);

    Back back = new Back(new Corner(true, Resource.EMPTY),new Corner(true, Resource.EMPTY),
            new Corner(true, Resource.EMPTY),new Corner(true, Resource.EMPTY), Resource.PLANT);

    public Hashtable<Resource, Integer> createTable() {
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.FUNGI, 3);
        return placemCond;
    }

    public Hashtable<Resource, Integer> enoughResourcesTable() {
        Hashtable<Resource, Integer> myRes = new Hashtable<>();
        myRes.put(Resource.FUNGI, 5);
        myRes.put(Resource.ANIMAL, 2);
        myRes.put(Resource.INSECT, 3);
        return myRes;
    }

    public Hashtable<Resource, Integer> notEnoughResourcesTable() {
        Hashtable<Resource, Integer> myRes = new Hashtable<>();
        myRes.put(Resource.FUNGI, 1);
        myRes.put(Resource.ANIMAL, 4);
        myRes.put(Resource.INSECT, 2);
        return myRes;
    }

    public Hashtable<Resource, Integer> nullResourceTable() {
        Hashtable<Resource, Integer> myRes = new Hashtable<>();
        myRes.put(Resource.ANIMAL, 4);
        myRes.put(Resource.INSECT, 2);
        return myRes;
    }

    @Test
    void sidesGetCornerTest() {
        assertSame(side.getTL(), side.getCorners().get(Direction.TOPLEFT));
        assertSame(side.getTR(), side.getCorners().get(Direction.TOPRIGHT));
        assertSame(side.getBL(), side.getCorners().get(Direction.BOTTOMLEFT));
        assertSame(side.getBR(), side.getCorners().get(Direction.BOTTOMRIGHT));
        assertFalse(side.getTL().getVisibility());
        assertSame(side.getTL().getResource(), Resource.EMPTY);
        assertFalse(side.getTR().getVisibility());
        assertSame(side.getTR().getResource(), Resource.EMPTY);
        assertTrue(side.getBL().getVisibility());
        assertSame(side.getBL().getResource(), Resource.PLANT);
        assertTrue(side.getBR().getVisibility());
        assertSame(side.getBR().getResource(), Resource.EMPTY);


        assertSame(front.getTL(), front.getCorners().get(Direction.TOPLEFT));
        assertSame(front.getTR(), front.getCorners().get(Direction.TOPRIGHT));
        assertSame(front.getBL(), front.getCorners().get(Direction.BOTTOMLEFT));
        assertSame(front.getBR(), front.getCorners().get(Direction.BOTTOMRIGHT));
        assertFalse(front.getTL().getVisibility());
        assertSame(front.getTL().getResource(), Resource.EMPTY);
        assertTrue(front.getTR().getVisibility());
        assertSame(front.getTR().getResource(), Resource.ANIMAL);
        assertTrue(front.getBL().getVisibility());
        assertSame(front.getBL().getResource(), Resource.PLANT);
        assertTrue(front.getBR().getVisibility());
        assertSame(front.getBR().getResource(), Resource.EMPTY);

        assertEquals(2, front.getPointsGivenOnPlacement());
        assertSame(front.getBonus(), Bonus.CORNER);
        assertSame(front.getResourcePlacementCondition().get(Resource.FUNGI), createTable().get(Resource.FUNGI));


        assertSame(back.getTL(), back.getCorners().get(Direction.TOPLEFT));
        assertSame(back.getTR(), back.getCorners().get(Direction.TOPRIGHT));
        assertSame(back.getBL(), back.getCorners().get(Direction.BOTTOMLEFT));
        assertSame(back.getBR(), back.getCorners().get(Direction.BOTTOMRIGHT));
        assertTrue(back.getTL().getVisibility());
        assertSame(back.getTL().getResource(), Resource.EMPTY);
        assertTrue(back.getTR().getVisibility());
        assertSame(back.getTR().getResource(), Resource.EMPTY);
        assertTrue(back.getBL().getVisibility());
        assertSame(back.getBL().getResource(), Resource.EMPTY);
        assertTrue(back.getBR().getVisibility());
        assertSame(back.getBR().getResource(), Resource.EMPTY);


        assertSame(Resource.PLANT, side.getMainResource());
        assertSame(Resource.PLANT, front.getMainResource());
        assertSame(Resource.PLANT, back.getMainResource());
    }

    @Test
    void sideUsedGetSetTest() {
        assertFalse(side.getUsedLLeg());
        assertFalse(side.getUsedDiagonal());
        assertFalse(side.getUsedLCorner());

        assertFalse(front.getUsedLLeg());
        assertFalse(front.getUsedDiagonal());
        assertFalse(front.getUsedLCorner());

        assertFalse(back.getUsedLLeg());
        assertFalse(back.getUsedDiagonal());
        assertFalse(back.getUsedLCorner());

        side.setUsedDiagonal(true);
        assertTrue(side.getUsedDiagonal());
        side.setUsedLCorner(true);
        assertTrue(side.getUsedLCorner());
        side.setUsedLLeg(true);
        assertTrue(side.getUsedLLeg());

        front.setUsedDiagonal(true);
        assertTrue(front.getUsedDiagonal());
        front.setUsedLCorner(true);
        assertTrue(front.getUsedLCorner());
        front.setUsedLLeg(true);
        assertTrue(front.getUsedLLeg());

        back.setUsedDiagonal(true);
        assertTrue(back.getUsedDiagonal());
        back.setUsedLCorner(true);
        assertTrue(back.getUsedLCorner());
        back.setUsedLLeg(true);
        assertTrue(back.getUsedLLeg());

        side.setUsedDiagonal(false);
        assertFalse(side.getUsedDiagonal());
    }

    @Test
    void sidePlacingTest () {
        assertFalse(front.getIsPlaced());
        assertNull(front.getPositionInKingdom());

        assertTrue(front.isPlacementConditionSatisfied(enoughResourcesTable()));
        assertFalse(front.isPlacementConditionSatisfied(notEnoughResourcesTable()));
        assertFalse(front.isPlacementConditionSatisfied(nullResourceTable()));

        front.placeInPosition(5, -2);
        front.getTL().setLinkedSide(back);

        assertTrue(front.getIsPlaced());
        assertEquals(5, front.getPositionInKingdom().getX());
        assertEquals(-2, front.getPositionInKingdom().getY());
        assertSame(back, front.getTL().getLinkedSide());


        assertFalse(back.getIsPlaced());
        assertNull(back.getPositionInKingdom());

        back.placeInPosition(2,4);
        back.getBR().setLinkedSide(side);


        assertTrue(back.getIsPlaced());
        assertEquals(2, back.getPositionInKingdom().getX());
        assertEquals(4, back.getPositionInKingdom().getY());
        assertSame(side, back.getBR().getLinkedSide());
    }

    @Test
    void toStringTest () {
        System.out.println(side.toString());
        System.out.println(front.toString());
        System.out.println(back.toString());
    }
}