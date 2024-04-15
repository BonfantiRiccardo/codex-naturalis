package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

class KingdomTest {
    Player p = new Player("Riccardo");

    CardCreator cc = new CardCreator();
    Deck gD = new GoldDeck(cc);
    Deck rD = new ResourceDeck(cc);
    Deck sD = new StartDeck(cc);

    /**
     * Test the correct placing of sides of the cards in the kingdom. It does not use the placeCard method of the Player
     * class because it was implemented later and because it adds useless complexity to the test.
     * The test initializes a kingdom by placing a StartCard Side (the Front). Then it places the Back of a ResourceCard,
     * and finally it places a Front of a GoldCard (without checking for the condition).
     * In the meantime it checks with assertions that the placedSides list is correctly updated.
     */
    @Test
    void testPlacedSides() {
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

        assertEquals(1, p.getMyKingdom().getPlacedSides().size());
        assertSame(placed, p.getMyKingdom().getPlacedSides().get(0));


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

        assertEquals(2, p.getMyKingdom().getPlacedSides().size());
        assertSame(placed, p.getMyKingdom().getPlacedSides().get(0));
        assertSame(rC.getBack(), p.getMyKingdom().getPlacedSides().get(1));


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

        assertEquals(3, p.getMyKingdom().getPlacedSides().size());
        assertSame(placed, p.getMyKingdom().getPlacedSides().get(0));
        assertSame(rC.getBack(), p.getMyKingdom().getPlacedSides().get(1));
        assertSame(gC.getFront(), p.getMyKingdom().getPlacedSides().get(2));
    }

    /**
     * Test that the placing of sides of the cards in the kingdom is coherent with the activePositions list. It does not
     * use the placeCard method of the Player class because it was implemented later and because it adds useless
     * complexity to the test. The test initializes a kingdom by placing a StartCard Side (the Front). Then it places
     * the Back of a ResourceCard, and finally it places a Front of a GoldCard (without checking for the condition).
     * In the meantime it checks with assertions that the activePosition list is correctly updated.
     */
    @Test
    void testActivePositions() {
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

        Side placed = sC.getFront();    //STUB PLACES FRONT

        for (Direction d: Direction.values()) {
            if (placed.getCorners().get(d).getVisibility()) {
                assertTrue(p.getMyKingdom().getActivePositions().contains(createPosition(d, placed.getPositionInKingdom())));
            } else if (!placed.getCorners().get(d).getVisibility())
                assertFalse(p.getMyKingdom().getActivePositions().contains(createPosition(d, placed.getPositionInKingdom())));
        }


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

        for (Direction d: Direction.values()) {
            if (placed.getCorners().get(d).getVisibility() && placed.getCorners().get(d).getLinkedSide() == null) {
                assertTrue(p.getMyKingdom().getActivePositions().contains(createPosition(d, placed.getPositionInKingdom())));
            } else if (!placed.getCorners().get(d).getVisibility() || placed.getCorners().get(d).getLinkedSide() != null)
                assertFalse(p.getMyKingdom().getActivePositions().contains(createPosition(d, placed.getPositionInKingdom())));

            if (rC.getBack().getCorners().get(d).getVisibility() && !createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom())) {
                assertTrue(p.getMyKingdom().getActivePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));
            } else if (!rC.getBack().getCorners().get(d).getVisibility() || createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom()))
                assertFalse(p.getMyKingdom().getActivePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));
        }


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

        for (Direction d: Direction.values()) {
            if (placed.getCorners().get(d).getVisibility() && placed.getCorners().get(d).getLinkedSide() == null) {
                assertTrue(p.getMyKingdom().getActivePositions().contains(createPosition(d, placed.getPositionInKingdom())));
            } else if (!placed.getCorners().get(d).getVisibility() || placed.getCorners().get(d).getLinkedSide() != null)
                assertFalse(p.getMyKingdom().getActivePositions().contains(createPosition(d, placed.getPositionInKingdom())));

            if (rC.getBack().getCorners().get(d).getVisibility() && rC.getBack().getCorners().get(d).getLinkedSide() == null && !createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom()) && !p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, pos))) {
                assertTrue(p.getMyKingdom().getActivePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));
            } else if (!rC.getBack().getCorners().get(d).getVisibility() || rC.getBack().getCorners().get(d).getLinkedSide() != null || createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom()))
                assertFalse(p.getMyKingdom().getActivePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));

            if (gC.getFront().getCorners().get(d).getVisibility() && !createPosition(d, gC.getFront().getPositionInKingdom()).equals(placed.getPositionInKingdom())) {
                assertTrue(p.getMyKingdom().getActivePositions().contains(createPosition(d, gC.getFront().getPositionInKingdom())));
            } else if (!gC.getFront().getCorners().get(d).getVisibility() || createPosition(d, gC.getFront().getPositionInKingdom()).equals(placed.getPositionInKingdom()))
                assertFalse(p.getMyKingdom().getActivePositions().contains(createPosition(d, gC.getFront().getPositionInKingdom())));
        }
    }

    /**
     * Test that the placing of sides of the cards in the kingdom is coherent with the impossiblePositions list. It does
     * not use the placeCard method of the Player class because it was implemented later and because it adds useless
     * complexity to the test. The test initializes a kingdom by placing a StartCard Side (the Front). Then it places
     * the Back of a ResourceCard, and finally it places a Front of a GoldCard (without checking for the condition).
     * In the meantime it checks with assertions that the impossiblePositions list is correctly updated.
     */
    @Test
    void testImpossiblePositions() {
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

        Side placed = sC.getFront();    //STUB PLACES FRONT

        for (Direction d: Direction.values()) {
            if (placed.getCorners().get(d).getVisibility()) {
                assertFalse(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, placed.getPositionInKingdom())));
            } else if (!placed.getCorners().get(d).getVisibility())
                assertTrue(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, placed.getPositionInKingdom())));
        }
        assertTrue(p.getMyKingdom().getImpossiblePositions().contains(placed.getPositionInKingdom()));

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

        assertTrue(p.getMyKingdom().getImpossiblePositions().contains(rC.getBack().getPositionInKingdom()));
        for (Direction d: Direction.values()) {
            if (placed.getCorners().get(d).getVisibility() && placed.getCorners().get(d).getLinkedSide() == null) {
                assertFalse(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, placed.getPositionInKingdom())));
            } else if (!placed.getCorners().get(d).getVisibility() || placed.getCorners().get(d).getLinkedSide() != null)
                assertTrue(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, placed.getPositionInKingdom())));

            if (rC.getBack().getCorners().get(d).getVisibility() && !createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom())) {
                assertFalse(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));
            } else if (!rC.getBack().getCorners().get(d).getVisibility() || createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom()))
                assertTrue(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));
        }


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

        assertTrue(p.getMyKingdom().getImpossiblePositions().contains(gC.getFront().getPositionInKingdom()));
        for (Direction d: Direction.values()) {
            if (placed.getCorners().get(d).getVisibility() && placed.getCorners().get(d).getLinkedSide() == null) {
                assertFalse(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, placed.getPositionInKingdom())));
            } else if (!placed.getCorners().get(d).getVisibility() || placed.getCorners().get(d).getLinkedSide() != null)
                assertTrue(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, placed.getPositionInKingdom())));

            if (rC.getBack().getCorners().get(d).getVisibility() && rC.getBack().getCorners().get(d).getLinkedSide() == null && !createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom()) && !p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, pos))) {
                assertFalse(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));
            } else if (!rC.getBack().getCorners().get(d).getVisibility() || rC.getBack().getCorners().get(d).getLinkedSide() != null || createPosition(d, rC.getBack().getPositionInKingdom()).equals(placed.getPositionInKingdom()))
                assertTrue(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, rC.getBack().getPositionInKingdom())));

            if (gC.getFront().getCorners().get(d).getVisibility() && !createPosition(d, gC.getFront().getPositionInKingdom()).equals(placed.getPositionInKingdom())) {
                assertFalse(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, gC.getFront().getPositionInKingdom())));
            } else if (!gC.getFront().getCorners().get(d).getVisibility() || createPosition(d, gC.getFront().getPositionInKingdom()).equals(placed.getPositionInKingdom()))
                assertTrue(p.getMyKingdom().getImpossiblePositions().contains(createPosition(d, gC.getFront().getPositionInKingdom())));
        }

    }

    /**
     * The same method was later implemented in the Direction Enum. See that for JavaDoc.
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
     * Test that the placing of sides of the cards in the kingdom is coherent with the onFieldResources table. It does
     * not use the placeCard method of the Player class because it was implemented later and because it adds useless
     * complexity to the test. The test initializes a kingdom by placing a StartCard Side (the Front). Then it places
     * the Back of a ResourceCard, and finally it places a Front of a GoldCard (without checking for the condition).
     * In the meantime it checks with assertions that the onFieldResources table is correctly updated.
     */
    @RepeatedTest(value = 100) //@Test
    void testUpdateOnFieldResources() {
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

        Side placed = sC.getFront();    //STUB PLACES FRONT


        Hashtable<Resource, Integer> test = new Hashtable<>();

        for (Direction d: Direction.values())
            if (placed.getCorners().get(d).getVisibility() && !placed.getCorners().get(d).getResource().equals(Resource.EMPTY))
                test.put(placed.getCorners().get(d).getResource(), 1);

        for (Resource r: sC.getBackResource()) {
            if (test.get(r) != null)
                test.replace(r, test.get(r) + 1);
            else
                test.put(r, 1);
        }

        for (Resource r: test.keySet()) {
            assertEquals(test.get(r), p.getMyKingdom().getOnFieldResources().get(r));
        }


        ResourceCard rC;
        try {
            rC = (ResourceCard) rD.drawCard();
        } catch (NoCardsException e) {
            throw new RuntimeException(e);
        }
        Position pos = p.getMyKingdom().getActivePositions().get(0);
        rC.getFront().placeInPosition(pos.getX(), pos.getY());

        for (Direction d : Direction.values()) {
            if (createPosition(d, placed.getPositionInKingdom()).equals(rC.getFront().getPositionInKingdom())) {
                placed.getCorners().get(d).setLinkedSide(rC.getFront());
            }
        }

        p.getMyKingdom().updateKingdom(rC, rC.getFront(), pos);

        for (Direction d: Direction.values()) {
            if (rC.getFront().getCorners().get(d).getVisibility() && !rC.getFront().getCorners().get(d).getResource().equals(Resource.EMPTY))
                if (test.containsKey(rC.getFront().getCorners().get(d).getResource()))
                    test.replace(rC.getFront().getCorners().get(d).getResource(), test.get(rC.getFront().getCorners().get(d).getResource()) + 1);
                else
                    test.put(rC.getFront().getCorners().get(d).getResource(), 1);

            if (placed.getCorners().get(d).getLinkedSide() != null && !placed.getCorners().get(d).getResource().equals(Resource.EMPTY))
                test.replace(placed.getCorners().get(d).getResource(), test.get(placed.getCorners().get(d).getResource()) - 1);
        }

        for (Resource r: test.keySet()) {
            assertEquals(test.get(r), p.getMyKingdom().getOnFieldResources().get(r));
        }

    }
}