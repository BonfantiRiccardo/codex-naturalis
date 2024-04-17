package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.*;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.*;


import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceBoundObjectiveTest {
    CardCreator cc = new CardCreator();
    Player p = new Player("Riccardo");
    Deck rD = new ResourceDeck(cc);
    Deck sD = new StartDeck(cc);
    Deck gD = new GoldDeck(cc);
    ResourcesBoundObjective rC1;
    ResourcesBoundObjective rC2;
    ResourcesBoundObjective rC3;

    /**
     * Creates a kingdom with 3 cards placed.
     */
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
            p.instantiateMyKingdom(sC, sC.getFront());
        } catch (AlreadyAssignedException | IncorrectUserActionException e) {
            throw new RuntimeException(e);
        }

        Side placed = sC.getFront();


        ResourceCard rC;
        try {
            rC = (ResourceCard) rD.drawCard();
        } catch (NoCardsException e) {
            throw new RuntimeException(e);
        }
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

    /**
     * Creates 3 different Resource Objective Cards.
     */
    @BeforeEach
    void setupObjectives () {
        rC1  = new ResourcesBoundObjective(95,2, createTableRes());
        rC2  = new ResourcesBoundObjective(99,3, createTableTrisGold());
        rC3  = new ResourcesBoundObjective(101,2, createTableDuoGold());
    }

    /**
     * Test 10 times if the objectives are fulfilled given the kingdom
     */
    //@Test
    @RepeatedTest(value = 10)
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

    /**
     * method to create the position that corresponds to a direction (later implemented in Direction Enum).
     * @param d Direction.
     * @param p Base position.
     * @return New position.
     */
    public Position createPosition(Direction d, Position p) {
        return switch (d) {
            case Direction.TOPLEFT -> new Position(p.getX() - 1, p.getY() + 1);
            case Direction.TOPRIGHT -> new Position(p.getX() + 1, p.getY() + 1);
            case Direction.BOTTOMLEFT -> new Position(p.getX() - 1, p.getY() - 1);
            case Direction.BOTTOMRIGHT -> new Position(p.getX() + 1, p.getY() - 1);
        };
    }

    /**
     * Table of resources for objective 1.
     * @return Table
     */
    public Hashtable<Resource, Integer> createTableRes(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.FUNGI, 3);
        return placemCond;
    }

    /**
     * Table of resources for objective 2.
     * @return Table
     */
    public Hashtable<Resource, Integer> createTableTrisGold(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.INKWELL, 1);
        placemCond.put(Resource.QUILL, 1);
        placemCond.put(Resource.MANUSCRIPT, 1);
        return placemCond;
    }

    /**
     * Table of resources for objective 3.
     * @return Table
     */
    public Hashtable<Resource, Integer> createTableDuoGold(){
        Hashtable<Resource, Integer> placemCond = new Hashtable<>();
        placemCond.put(Resource.INKWELL, 2);
        return placemCond;
    }
}
