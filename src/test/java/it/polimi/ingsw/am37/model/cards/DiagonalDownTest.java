package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.DiagonalDown;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Corner;
import it.polimi.ingsw.am37.model.sides.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public class DiagonalDownTest {
    DiagonalDown diagonal=new DiagonalDown(1,2, Resource.ANIMAL, Resource.ANIMAL);
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


    Corner dn4_n5=new Corner(true, Resource.EMPTY);
    Corner dn5_n4=new Corner(true, Resource.EMPTY);
    Corner dn5_n6=new Corner(true, Resource.EMPTY);
    Corner dn6_n5=new Corner(true, Resource.EMPTY);

    Side dside1ok=new Back(useless, useless, useless, d1_2, Resource.ANIMAL);
    Side dside2ok=new Back(d2_1, d2_n1, useless, d2_3, Resource.ANIMAL);
    Side dside3ok=new Back(d3_2, useless, useless, d3_n4, Resource.ANIMAL);
    Side dside1no=new Back(useless, dn1_n2, dn1_2, useless, Resource.ANIMAL);
    Side dside2no=new Back(useless, useless, dn2_n1, useless, Resource.ANIMAL);
    Side dside3no=new Back(useless, useless, useless, useless, Resource.ANIMAL);
    Side dside4no=new Back(dn4_3, useless, useless, dn4_n5, Resource.ANIMAL);

    Side dside5no=new Back(dn5_n4, useless, useless, dn5_n6, Resource.ANIMAL);
    Side dside6no=new Back(dn6_n5, useless, useless, useless, Resource.ANIMAL);

    List<Side> placedSides;

    @Test
    void calculateNumOfCompletion(){
        placedSides = new ArrayList<>();

        dside1ok.placeInPosition(0,0);
        dside2ok.placeInPosition(1,-1);
        dside3ok.placeInPosition(2,-2);
        dside4no.placeInPosition(3,-3);
        dside1no.placeInPosition(2,0);
        dside2no.placeInPosition(3, 1);
        dside3no.placeInPosition(1, 8);
        dside5no.placeInPosition(4,-4);
        dside6no.placeInPosition(5,-5);

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
        dn4_n5.setLinkedSide(dside5no);
        dn5_n4.setLinkedSide(dside4no);
        dn5_n6.setLinkedSide(dside6no);
        dn6_n5.setLinkedSide(dside5no);

        placedSides.add(dside1ok);
        placedSides.add(dside2ok);
        placedSides.add(dside3ok);
        placedSides.add(dside1no);
        placedSides.add(dside2no);
        placedSides.add(dside3no);
        placedSides.add(dside4no);
        placedSides.add(dside5no);
        placedSides.add(dside6no);

        assertSame(2, diagonal.calculateNumOfCompletionTest(placedSides));

    }
}
