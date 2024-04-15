package it.polimi.ingsw.am37.model.sides;

import it.polimi.ingsw.am37.model.game.Resource;
import org.junit.jupiter.api.Test;

public class BackTest {
    //card 1
    Back backResource = new Back(new Corner (true, Resource.EMPTY), new Corner (true , Resource.EMPTY),
            new Corner(true, Resource.EMPTY), new Corner(true, Resource.EMPTY), Resource.INSECT);

    //card 53
    Back backGold = new Back(new Corner (true, Resource.EMPTY), new Corner (true , Resource.EMPTY),
            new Corner(true, Resource.EMPTY), new Corner(true, Resource.EMPTY), Resource.ANIMAL);

    /**
     * Tests the toString method.
     */
    @Test
    void toStringTest() {
        System.out.println(backResource.toString());
        System.out.println(backGold.toString());
    }

}