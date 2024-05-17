package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.decks.StartDeck;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StartCardTest {
    StartCard sc = new StartCard(81, new Front(new Corner(true, Resource.EMPTY),new Corner(true, Resource.ANIMAL),
            new Corner(true, Resource.INSECT),new Corner(true, Resource.EMPTY), Resource.EMPTY, 0,
            null, null), new Back(new Corner(true, Resource.PLANT),new Corner(true,
            Resource.FUNGI), new Corner(true, Resource.ANIMAL),new Corner(true, Resource.INSECT), Resource.EMPTY),
            createList());

    /**
     * Testing get and toString methods.
     */
    @Test
    public void startCardTest() {
        System.out.println("Carta start creata: ");
        System.out.println("    id: " + sc.getId());
        System.out.println("    front: ");
        System.out.println("        TL: ");
        System.out.println("            isVisible: " + sc.getFront().getTL().getVisibility());
        System.out.println("            Resource: " + sc.getFront().getTL().getResource());
        System.out.println("        TR: ");
        System.out.println("            isVisible: " + sc.getFront().getTR().getVisibility());
        System.out.println("            Resource: " + sc.getFront().getTR().getResource());
        System.out.println("        BL: ");
        System.out.println("            isVisible: " + sc.getFront().getBL().getVisibility());
        System.out.println("            Resource: " + sc.getFront().getBL().getResource());
        System.out.println("        BR: ");
        System.out.println("            isVisible: " + sc.getFront().getBR().getVisibility());
        System.out.println("            Resource: " + sc.getFront().getBR().getResource());
        System.out.println("        pointsOnPlac: " + sc.getFront().getPointsGivenOnPlacement());
        System.out.println("        placemCondit: " + sc.getFront().getResourcePlacementCondition());
        System.out.println("        bonus: " + sc.getFront().getBonus());
        System.out.println("    back: ");
        System.out.println("        TL: ");
        System.out.println("            isVisible: " + sc.getBack().getTL().getVisibility());
        System.out.println("            Resource: " + sc.getBack().getTL().getResource());
        System.out.println("        TR: ");
        System.out.println("            isVisible: " + sc.getBack().getTR().getVisibility());
        System.out.println("            Resource: " + sc.getBack().getTR().getResource());
        System.out.println("        BL: ");
        System.out.println("            isVisible: " + sc.getBack().getBL().getVisibility());
        System.out.println("            Resource: " + sc.getBack().getBL().getResource());
        System.out.println("        BR: ");
        System.out.println("            isVisible: " + sc.getBack().getBR().getVisibility());
        System.out.println("            Resource: " + sc.getBack().getBR().getResource());
        System.out.println("        backRes: " + sc.getBackResource());

        System.out.println(sc.toString());

        assertEquals(81, sc.getId());
        assertTrue(sc.getFront().getTL().getVisibility());
        assertTrue(sc.getFront().getTR().getVisibility());
        assertTrue(sc.getFront().getBL().getVisibility());
        assertTrue(sc.getFront().getBR().getVisibility());
        assertSame(sc.getFront().getTL().getResource(), Resource.EMPTY);
        assertSame(sc.getFront().getTR().getResource(), Resource.ANIMAL);
        assertSame(sc.getFront().getBL().getResource(), Resource.INSECT);
        assertSame(sc.getFront().getBR().getResource(), Resource.EMPTY);

        assertSame(sc.getFront().getMainResource(), Resource.EMPTY);
        assertEquals(0, sc.getFront().getPointsGivenOnPlacement());
        assertNull(sc.getFront().getResourcePlacementCondition());
        assertNull(sc.getFront().getBonus());

        assertTrue(sc.getBack().getTL().getVisibility());
        assertTrue(sc.getBack().getTR().getVisibility());
        assertTrue(sc.getBack().getBL().getVisibility());
        assertTrue(sc.getBack().getBR().getVisibility());
        assertSame(sc.getBack().getTL().getResource(), Resource.PLANT);
        assertSame(sc.getBack().getTR().getResource(), Resource.FUNGI);
        assertSame(sc.getBack().getBL().getResource(), Resource.ANIMAL);
        assertSame(sc.getBack().getBR().getResource(), Resource.INSECT);

        assertSame(sc.getBack().getMainResource(), Resource.EMPTY);

    }

    public List<Resource> createList() {
        List<Resource> res = new ArrayList<>();
        res.add(Resource.ANIMAL);
        res.add(Resource.INSECT);
        return res;
    }

    @Test
    void printingTest() throws NoCardsException {
        CardCreator cc = new CardCreator();
        StartDeck sd = new StartDeck(cc);

        while (!sd.isEmpty())
            System.out.println(sd.drawCard());
    }
}