package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceBoundObjectiveTest {
    CardCreator cc = new CardCreator();
    Player p = new Player("Riccardo", Token.BLUE);
    Deck rD = new ResourceDeck(cc);
    Deck sD = new StartDeck(cc);
    Deck gD = new GoldDeck(cc);
    ResourcesBoundObjective rC1;
    ResourcesBoundObjective rC2;
    ResourcesBoundObjective rC3;

    @BeforeEach
    void setupKingdom() {
        StartCard sC;
        try {
            sC = (StartCard) sD.drawCard();
        } catch (NoCardsException e) {
            throw new RuntimeException(e);
        }

        try {
            p.setStartCard(sC);
        } catch (AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }
        p.chooseStartCardSide();
        Side placed = sC.getFront();


        ResourceCard rC;
        try {
            rC = (ResourceCard) rD.drawCard();
        } catch (NoCardsException e) {
            throw new RuntimeException(e);
        }
        //LEAVE UNTIL PLACECARD() METHOD IS IMPLEMENTED
        Position pos = p.getMyKingdom().getActivePositions().get(0);
        rC.getBack().placeInPosition(pos.getX(), pos.getY());

        for (Direction d: Direction.values()) {
            if (createPosition(d, placed.getPositionInKingdom()).equals(rC.getBack().getPositionInKingdom())) {
                placed.getCorners().get(d).setLinkedSide(rC.getBack());
            }
        }

        p.getMyKingdom().updateKingdom(rC, rC.getBack(), pos);


        GoldCard gC;
        try {
            gC = (GoldCard) gD.drawCard();
        } catch (NoCardsException e) {
            throw new RuntimeException(e);
        }
        //LEAVE UNTIL PLACECARD() METHOD IS IMPLEMENTED
        Position pos2 = p.getMyKingdom().getActivePositions().get(0);
        gC.getFront().placeInPosition(pos2.getX(), pos2.getY());

        for (Direction d: Direction.values()) {
            Position check = createPosition(d, pos2);
            if (check.equals(placed.getPositionInKingdom()))
                placed.getCorners().get(d.opposite()).setLinkedSide(gC.getFront());
            else if (check.equals(rC.getBack().getPositionInKingdom()))
                rC.getBack().getCorners().get(d.opposite()).setLinkedSide(gC.getFront());
        }

        p.getMyKingdom().updateKingdom(gC, gC.getFront(), pos2);
    }

    @BeforeEach
    void setupObjectives () {
        rC1  = new ResourcesBoundObjective(95,2, createTableRes());
        rC2  = new ResourcesBoundObjective(99,3, createTableTrisGold());
        rC3  = new ResourcesBoundObjective(101,2, createTableDuoGold());
    }

    @Test
    //@RepeatedTest(value = 10)
    void testResourceObjective() {
        int check1 = rC1.calculateNumOfCompletion(p.getMyKingdom());
        assertTrue(p.getMyKingdom().getOnFieldResources().get(Resource.FUNGI) >= check1*3);
        int check2 = rC2.calculateNumOfCompletion(p.getMyKingdom());
        assertTrue(p.getMyKingdom().getOnFieldResources().get(Resource.INKWELL) >= check2 ||
                p.getMyKingdom().getOnFieldResources().get(Resource.QUILL) >= check2 ||
                p.getMyKingdom().getOnFieldResources().get(Resource.MANUSCRIPT) >= check2);
        int check3 = rC3.calculateNumOfCompletion(p.getMyKingdom());
        assertTrue(p.getMyKingdom().getOnFieldResources().get(Resource.INKWELL) >= check3*2);
    }

    public Position createPosition(Direction d, Position p) {
        return switch (d) {
            case Direction.TOPLEFT -> new Position(p.getX() - 1, p.getY() + 1);
            case Direction.TOPRIGHT -> new Position(p.getX() + 1, p.getY() + 1);
            case Direction.BOTTOMLEFT -> new Position(p.getX() - 1, p.getY() - 1);
            case Direction.BOTTOMRIGHT -> new Position(p.getX() + 1, p.getY() - 1);
        };
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
}
