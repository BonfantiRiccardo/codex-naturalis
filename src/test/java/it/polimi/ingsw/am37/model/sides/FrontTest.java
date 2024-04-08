package it.polimi.ingsw.am37.model.sides;

import java.util.Hashtable;
import it.polimi.ingsw.am37.model.game.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FrontTest {
    //card 1
    Front frontResource = new Front(new Corner (true, Resource.FUNGI), new Corner (true , Resource.EMPTY),
            new Corner(true, Resource.FUNGI), new Corner(false, Resource.EMPTY), Resource.PLANT,0,
            null, null);

    //card 53
    Front frontGold = new Front(new Corner (true, Resource.EMPTY), new Corner (false , Resource.EMPTY),
            new Corner(true, Resource.INKWELL), new Corner(true, Resource.EMPTY), Resource.PLANT, 1,
            createTable(), Bonus.INKWELL);

    public Hashtable<Resource, Integer> createTable(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.PLANT, 2);
        placemCond.put(Resource.ANIMAL, 1);
        return placemCond;
    }

    @Test
    void resourceGetsTest() {
        assertSame(0, frontResource.getPointsGivenOnPlacement());
        assertNull(frontResource.getResourcePlacementCondition());
        assertNull(frontResource.getBonus());
    }
    @Test
    void goldGetsTest(){
        assertSame(1, frontGold.getPointsGivenOnPlacement());
        assertEquals(frontGold.getResourcePlacementCondition(), createTable());
        assertSame(frontGold.getBonus(), Bonus.INKWELL);
    }

    public Hashtable<Resource, Integer> enoughResourcesTable() {
        Hashtable<Resource, Integer> myRes = new Hashtable<>();
        myRes.put(Resource.PLANT, 5);
        myRes.put(Resource.ANIMAL, 2);
        myRes.put(Resource.INSECT, 3);
        return myRes;
    }
    public Hashtable<Resource, Integer> notEnoughResourcesTable() {
        Hashtable<Resource, Integer> myRes = new Hashtable<>();
        myRes.put(Resource.PLANT, 1);
        myRes.put(Resource.ANIMAL, 2);
        myRes.put(Resource.INSECT, 3);
        return myRes;
    }
    public Hashtable<Resource, Integer> missingResourcesTable() {
        Hashtable<Resource, Integer> myRes = new Hashtable<>();
        myRes.put(Resource.ANIMAL, 2);
        myRes.put(Resource.INSECT, 3);
        return myRes;
    }

    @Test
    void placementConditionSatisfied() {
        assertTrue(frontGold.isPlacementConditionSatisfied(enoughResourcesTable()));
    }

    @Test
    void placementConditionNotSatisfied() {
        assertFalse(frontGold.isPlacementConditionSatisfied((notEnoughResourcesTable())));
    }

    @Test
    void placementConditionMissingResource() {
        assertFalse(frontGold.isPlacementConditionSatisfied((missingResourcesTable())));
    }

    @Test
    void placementConditionResource() {
        assertTrue(frontResource.isPlacementConditionSatisfied((enoughResourcesTable())));
        assertTrue(frontResource.isPlacementConditionSatisfied((notEnoughResourcesTable())));
    }

    @Test
    void toStringTest() {
        System.out.println(frontGold.toString());
        System.out.println(frontResource.toString());
    }

}
