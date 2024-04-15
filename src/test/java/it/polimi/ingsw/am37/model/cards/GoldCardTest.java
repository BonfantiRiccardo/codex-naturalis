package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.placeable.GoldCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Corner;
import it.polimi.ingsw.am37.model.sides.*;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class GoldCardTest {
    GoldCard gc = new GoldCard(41, new Front(new Corner(false, Resource.EMPTY),new Corner(true, Resource.ANIMAL),
                new Corner(true, Resource.PLANT),new Corner(true, Resource.EMPTY), Resource.PLANT,3,
                createTable(), null), new Back(new Corner(true, Resource.EMPTY),new Corner(true,
                Resource.EMPTY), new Corner(true, Resource.EMPTY),new Corner(true, Resource.EMPTY), Resource.PLANT));

    /**
     * Tests the toString and get methods of a Gold card.
     */
    @Test
    public void goldCardTest() {
        System.out.println("Carta oro creata: ");
        System.out.println("    id: " + gc.getId());
        System.out.println("    front: ");
        System.out.println("        TL: ");
        System.out.println("            isVisible: " + gc.getFront().getTL().getVisibility());
        System.out.println("            Resource: " + gc.getFront().getTL().getResource());
        System.out.println("        TR: ");
        System.out.println("            isVisible: " + gc.getFront().getTR().getVisibility());
        System.out.println("            Resource: " + gc.getFront().getTR().getResource());
        System.out.println("        BL: ");
        System.out.println("            isVisible: " + gc.getFront().getBL().getVisibility());
        System.out.println("            Resource: " + gc.getFront().getBL().getResource());
        System.out.println("        BR: ");
        System.out.println("            isVisible: " + gc.getFront().getBR().getVisibility());
        System.out.println("            Resource: " + gc.getFront().getBR().getResource());
        System.out.println("        pointsOnPlac: " + gc.getFront().getPointsGivenOnPlacement());
        System.out.println("        placemCondit: " + gc.getFront().getResourcePlacementCondition());
        System.out.println("        bonus: " + gc.getFront().getBonus());
        System.out.println("    back: ");
        System.out.println("        TL: ");
        System.out.println("            isVisible: " + gc.getBack().getTL().getVisibility());
        System.out.println("            Resource: " + gc.getBack().getTL().getResource());
        System.out.println("        TR: ");
        System.out.println("            isVisible: " + gc.getBack().getTR().getVisibility());
        System.out.println("            Resource: " + gc.getBack().getTR().getResource());
        System.out.println("        BL: ");
        System.out.println("            isVisible: " + gc.getBack().getBL().getVisibility());
        System.out.println("            Resource: " + gc.getBack().getBL().getResource());
        System.out.println("        BR: ");
        System.out.println("            isVisible: " + gc.getBack().getBR().getVisibility());
        System.out.println("            Resource: " + gc.getBack().getBR().getResource());
        System.out.println("        permRes: " + gc.getBack().getMainResource());

        System.out.println(gc.toString());

        assertEquals(41, gc.getId());
        assertFalse(gc.getFront().getTL().getVisibility());
        assertTrue(gc.getFront().getTR().getVisibility());
        assertTrue(gc.getFront().getBL().getVisibility());
        assertTrue(gc.getFront().getBR().getVisibility());
        assertSame(gc.getFront().getTL().getResource(), Resource.EMPTY);
        assertSame(gc.getFront().getTR().getResource(), Resource.ANIMAL);
        assertSame(gc.getFront().getBL().getResource(), Resource.PLANT);
        assertSame(gc.getFront().getBR().getResource(), Resource.EMPTY);

        assertSame(gc.getFront().getMainResource(), Resource.PLANT);
        assertEquals(3, gc.getFront().getPointsGivenOnPlacement());
        assertSame(createTable().get(Resource.FUNGI), gc.getFront().getResourcePlacementCondition().get(Resource.FUNGI));
        assertNull(gc.getFront().getBonus());

        assertTrue(gc.getBack().getTL().getVisibility());
        assertTrue(gc.getBack().getTR().getVisibility());
        assertTrue(gc.getBack().getBL().getVisibility());
        assertTrue(gc.getBack().getBR().getVisibility());
        assertSame(gc.getBack().getTL().getResource(), Resource.EMPTY);
        assertSame(gc.getBack().getTR().getResource(), Resource.EMPTY);
        assertSame(gc.getBack().getBL().getResource(), Resource.EMPTY);
        assertSame(gc.getBack().getBR().getResource(), Resource.EMPTY);

        assertSame(gc.getBack().getMainResource(), Resource.PLANT);

    }

    public Hashtable<Resource, Integer> createTable() {
        Hashtable<Resource, Integer> placCond = new Hashtable<>();
        placCond.put(Resource.FUNGI, 3);
        return placCond;
    }

}