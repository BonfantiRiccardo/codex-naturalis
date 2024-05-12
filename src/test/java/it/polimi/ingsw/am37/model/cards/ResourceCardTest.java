package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.placeable.ResourceCard;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceCardTest {
    ResourceCard rc = new ResourceCard(1, new Front(new Corner(false, Resource.EMPTY),new Corner(true, Resource.ANIMAL),
            new Corner(true, Resource.PLANT),new Corner(true, Resource.EMPTY), Resource.PLANT,1,
            null, null), new Back(new Corner(true, Resource.EMPTY),new Corner(true,
            Resource.EMPTY), new Corner(true, Resource.EMPTY),new Corner(true, Resource.EMPTY), Resource.PLANT));

    /**
     * Testing get and toString methods.
     */
    @Test
    public void goldCardTest() {
        System.out.println("Carta oro creata: ");
        System.out.println("    id: " + rc.getId());
        System.out.println("    front: ");
        System.out.println("        TL: ");
        System.out.println("            isVisible: " + rc.getFront().getTL().getVisibility());
        System.out.println("            Resource: " + rc.getFront().getTL().getResource());
        System.out.println("        TR: ");
        System.out.println("            isVisible: " + rc.getFront().getTR().getVisibility());
        System.out.println("            Resource: " + rc.getFront().getTR().getResource());
        System.out.println("        BL: ");
        System.out.println("            isVisible: " + rc.getFront().getBL().getVisibility());
        System.out.println("            Resource: " + rc.getFront().getBL().getResource());
        System.out.println("        BR: ");
        System.out.println("            isVisible: " + rc.getFront().getBR().getVisibility());
        System.out.println("            Resource: " + rc.getFront().getBR().getResource());
        System.out.println("        pointsOnPlac: " + rc.getFront().getPointsGivenOnPlacement());
        System.out.println("        placemCondit: " + rc.getFront().getResourcePlacementCondition());
        System.out.println("        bonus: " + rc.getFront().getBonus());
        System.out.println("    back: ");
        System.out.println("        TL: ");
        System.out.println("            isVisible: " + rc.getBack().getTL().getVisibility());
        System.out.println("            Resource: " + rc.getBack().getTL().getResource());
        System.out.println("        TR: ");
        System.out.println("            isVisible: " + rc.getBack().getTR().getVisibility());
        System.out.println("            Resource: " + rc.getBack().getTR().getResource());
        System.out.println("        BL: ");
        System.out.println("            isVisible: " + rc.getBack().getBL().getVisibility());
        System.out.println("            Resource: " + rc.getBack().getBL().getResource());
        System.out.println("        BR: ");
        System.out.println("            isVisible: " + rc.getBack().getBR().getVisibility());
        System.out.println("            Resource: " + rc.getBack().getBR().getResource());
        System.out.println("        permRes: " + rc.getBack().getMainResource());

        System.out.println(rc.toString());

        assertEquals(1, rc.getId());
        assertFalse(rc.getFront().getTL().getVisibility());
        assertTrue(rc.getFront().getTR().getVisibility());
        assertTrue(rc.getFront().getBL().getVisibility());
        assertTrue(rc.getFront().getBR().getVisibility());
        assertSame(rc.getFront().getTL().getResource(), Resource.EMPTY);
        assertSame(rc.getFront().getTR().getResource(), Resource.ANIMAL);
        assertSame(rc.getFront().getBL().getResource(), Resource.PLANT);
        assertSame(rc.getFront().getBR().getResource(), Resource.EMPTY);

        assertSame(rc.getFront().getMainResource(), Resource.PLANT);
        assertEquals(1, rc.getFront().getPointsGivenOnPlacement());
        assertNull(rc.getFront().getResourcePlacementCondition());
        assertNull(rc.getFront().getBonus());

        assertTrue(rc.getBack().getTL().getVisibility());
        assertTrue(rc.getBack().getTR().getVisibility());
        assertTrue(rc.getBack().getBL().getVisibility());
        assertTrue(rc.getBack().getBR().getVisibility());
        assertSame(rc.getBack().getTL().getResource(), Resource.EMPTY);
        assertSame(rc.getBack().getTR().getResource(), Resource.EMPTY);
        assertSame(rc.getBack().getBL().getResource(), Resource.EMPTY);
        assertSame(rc.getBack().getBR().getResource(), Resource.EMPTY);

        assertSame(rc.getBack().getMainResource(), Resource.PLANT);

    }

    @Test
    void toStringTest(){
        Corner usl=new Corner(true, Resource.EMPTY);
        Corner anm=new Corner(true, Resource.ANIMAL);
        Corner no=new Corner(false, Resource.EMPTY);
        Front f=new Front(anm, anm, usl, no, Resource.ANIMAL,0,null, null);
        Back b=new Back(usl, usl, usl, usl, Resource.ANIMAL);
        ResourceCard c=new ResourceCard(2,f,b);
        System.out.println(c.toString());
    }

}