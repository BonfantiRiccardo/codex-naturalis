package it.polimi.ingsw.am37.model.cards;

/*import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Kingdom;
import it.polimi.ingsw.am37.model.sides.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;*/

public class PlacementBoundObjectiveTest {
    /* NEEDS KINGDOM STUB AND PLACEMENT CARD REIMPLEMENTATION

    PlacementBoundObjective lshape=new PlacementBoundObjective(2, 3, Resource.FUNGI, Resource.PLANT, Direction.TOPLEFT, Pattern.LSHAPE);
    PlacementBoundObjective diagonal=new PlacementBoundObjective(1,2, Resource.ANIMAL, Resource.ANIMAL, Direction.DOWNRIGHT, Pattern.DIAGONAL);

    Corner useless=new Corner(true, Resource.EMPTY);

    Corner d1_2=new Corner(true, Resource.EMPTY);
    Corner d2_1=new Corner(true, Resource.EMPTY);
    Corner d2_3=new Corner(true, Resource.EMPTY);
    Corner d3_2=new Corner(true, Resource.EMPTY);
    Corner d3_n4=new Corner(true, Resource.EMPTY);
    Corner dn4_3=new Corner(true, Resource.EMPTY);
    Corner d2_n1=new Corner(true, Resource.EMPTY);
    Corner dn1_2=new Corner(true, Resource.EMPTY);
    Corner dn1_n2=new Corner(true, Resource.EMPTY);
    Corner dn2_n1=new Corner(true, Resource.EMPTY);

    Side dside1ok=new Back(useless, useless, useless, d1_2, Resource.ANIMAL);
    Side dside2ok=new Back(d2_1, d2_n1, useless, d2_3, Resource.ANIMAL);
    Side dside3ok=new Back(d3_2, useless, useless, d3_n4, Resource.ANIMAL);
    Side dside1no=new Back(useless, dn1_n2, dn1_2, useless, Resource.ANIMAL);
    Side dside2no=new Back(useless, useless, dn2_n1, useless, Resource.ANIMAL);
    Side dside3no=new Back(useless, useless, useless, useless, Resource.ANIMAL);
    Side dside4no=new Back(dn4_3, useless, useless, useless, Resource.ANIMAL);

    Corner l1_2=new Corner(true, Resource.EMPTY);
    Corner l2_1=new Corner(true, Resource.EMPTY);
    Corner l2_n4=new Corner(true, Resource.EMPTY);
    Corner l3_n4=new Corner(true, Resource.EMPTY);
    Corner ln4_2=new Corner(true, Resource.EMPTY);
    Corner ln4_3=new Corner(true, Resource.EMPTY);
    //n5

    Corner ln1_n2=new Corner(true, Resource.EMPTY);
    Corner ln1_n3=new Corner(true, Resource.EMPTY);
    Corner ln2_n1=new Corner(true, Resource.EMPTY);
    Corner ln3_n1=new Corner(true, Resource.EMPTY);

    Side lside1ok=new Back(l1_2, useless, useless, useless, Resource.PLANT);
    Side lside2ok=new Back(useless, l2_n4, useless, l2_1, Resource.FUNGI);
    Side lside3ok=new Back(useless, useless, useless, l3_n4, Resource.FUNGI);
    Side lside1no=new Back(ln1_n2, ln1_n3, useless, useless, Resource.PLANT);
    Side lside2no=new Back(useless, useless, useless, ln2_n1, Resource.FUNGI);
    Side lside3no=new Back(useless, useless, ln3_n1, useless, Resource.FUNGI);
    Side lside4no=new Back(ln4_3, useless, ln4_2, useless, Resource.PLANT);
    Side lside5no=new Back(useless, useless, useless, useless, Resource.FUNGI);

    List<Side> placedSides;



    @Test
    void calculatePointsToGive(){
        placedSides = new ArrayList<>();

        dside1ok.placeInPosition(0,0);
        dside2ok.placeInPosition(1,-1);
        dside3ok.placeInPosition(2,-2);
        dside4no.placeInPosition(3,-3);
        dside1no.placeInPosition(2,0);
        dside2no.placeInPosition(3, 1);
        dside3no.placeInPosition(1, 8);

        d1_2.setLinkedSide(dside2ok);
        d2_1.setLinkedSide(dside1ok);
        d2_3.setLinkedSide(dside3ok);
        d3_2.setLinkedSide(dside2ok);
        d3_n4.setLinkedSide(dside4no);
        dn4_3.setLinkedSide(dside3ok);
        d2_n1.setLinkedSide(dside1no);
        dn1_2.setLinkedSide(dside2ok);
        dn1_n2.setLinkedSide(dside2no);
        dn2_n1.setLinkedSide(dside1no);

        lside1ok.placeInPosition(10,10);
        lside2ok.placeInPosition(9,11);
        lside3ok.placeInPosition(9,13);
        lside1no.placeInPosition(20,20);
        lside2no.placeInPosition(19,21);
        lside3no.placeInPosition(21,21);
        lside4no.placeInPosition(10,12);
        lside5no.placeInPosition(9,15);

        l1_2.setLinkedSide(lside2ok);
        l2_1.setLinkedSide(lside1ok);
        l2_n4.setLinkedSide(lside4no);
        l3_n4.setLinkedSide(lside4no);
        ln4_2.setLinkedSide(lside2ok);
        ln4_3.setLinkedSide(lside3ok);
        ln1_n2.setLinkedSide(lside2no);
        ln1_n3.setLinkedSide(lside3no);
        ln2_n1.setLinkedSide(lside1no);
        ln3_n1.setLinkedSide(lside1no);

        placedSides.add(dside1ok);
        placedSides.add(dside2ok);
        placedSides.add(dside3ok);
        placedSides.add(dside1no);
        placedSides.add(dside2no);
        placedSides.add(dside3no);
        placedSides.add(dside4no);

        placedSides.add(lside1ok);
        placedSides.add(lside2ok);
        placedSides.add(lside3ok);
        placedSides.add(lside1no);
        placedSides.add(lside2no);
        placedSides.add(lside3no);
        placedSides.add(lside4no);
        placedSides.add(lside5no);

        assertSame(3, lshape.calculatePointsToGive(placedSides));
        assertSame(2, diagonal.calculatePointsToGive(placedSides));
    }

     */
}
