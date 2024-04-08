package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.ResourcesBoundObjective;
import it.polimi.ingsw.am37.model.game.Resource;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceBoundObjectiveTest {
    ResourcesBoundObjective trisRes = new ResourcesBoundObjective(95, 2, createTableRes());
    ResourcesBoundObjective trisGold = new ResourcesBoundObjective(99, 3, createTableTrisGold());
    ResourcesBoundObjective duoGold = new ResourcesBoundObjective(101, 2, createTableDuoGold());
    ResourcesBoundObjective none = new ResourcesBoundObjective(1, 0, null);

    public Hashtable<Resource, Integer> myResPoints(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.FUNGI, 14);
        placemCond.put(Resource.INKWELL, 11);
        placemCond.put(Resource.QUILL, 5);
        placemCond.put(Resource.MANUSCRIPT, 7);
        placemCond.put(Resource.PLANT, 1);
        placemCond.put(Resource.ANIMAL, 5);
        placemCond.put(Resource.INSECT, 1);
        return placemCond;
    }

    public Hashtable<Resource, Integer> myResNoPoints(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.FUNGI, 2);
        placemCond.put(Resource.PLANT, 13);
        placemCond.put(Resource.INKWELL, 1);
        placemCond.put(Resource.QUILL, 5);
        placemCond.put(Resource.ANIMAL, 15);
        placemCond.put(Resource.INSECT, 1);
        placemCond.put(Resource.MANUSCRIPT, 0);
        return placemCond;
    }

    public Hashtable<Resource, Integer> createTableRes(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.FUNGI, 3);
        return placemCond;
    }

    public Hashtable<Resource, Integer> createTableTrisGold(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.INKWELL, 1);
        placemCond.put(Resource.QUILL, 1);
        placemCond.put(Resource.MANUSCRIPT, 1);
        return placemCond;
    }
    public Hashtable<Resource, Integer> createTableDuoGold(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.INKWELL, 2);
        return placemCond;
    }

    /*
    NEEDS KINGDOM STUB
    @Test
    void trisResTest() {
        assertEquals(4, trisRes.calculatePointsToGive(myResPoints()));
        assertEquals(0, trisRes.calculatePointsToGive(myResNoPoints()));
    }

    @Test
    void trisGoldTest() {
        assertEquals(5, trisGold.calculatePointsToGive(myResPoints()));
        assertEquals(0, trisGold.calculatePointsToGive(myResNoPoints()));
    }

    @Test
    void duoGoldTest() {
        assertEquals(5, duoGold.calculatePointsToGive(myResPoints()));
        assertEquals(0, duoGold.calculatePointsToGive(myResNoPoints()));
    }

    NEEDS KINGDOM STUB
    */
}
