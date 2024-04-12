package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.cards.objective.LShape;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Back;
import it.polimi.ingsw.am37.model.sides.Corner;
import it.polimi.ingsw.am37.model.sides.Position;
import it.polimi.ingsw.am37.model.sides.Side;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertSame;

public class LShapeTest {
    Position requested = new Position(-1, 3);
    LShape lshape=new LShape(2, 3, Resource.FUNGI, Resource.PLANT, Direction.TOPLEFT, requested);
    Corner useless=new Corner(true, Resource.EMPTY);
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
    List<Side> placedSides=new ArrayList<>();

    @Test
    void calculateNumOfCompletion(){

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

        placedSides.add(lside1ok);
        placedSides.add(lside2ok);
        placedSides.add(lside3ok);
        placedSides.add(lside1no);
        placedSides.add(lside2no);
        placedSides.add(lside3no);
        placedSides.add(lside4no);
        placedSides.add(lside5no);

        assertSame(1, lshape.calculateNumOfCompletionTest(placedSides));
    }
}
