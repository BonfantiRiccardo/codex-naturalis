package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.model.cards.CardCreator;
import it.polimi.ingsw.am37.model.cards.objective.Direction;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.decks.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.sides.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KingdomTest {
    Player p = new Player("Riccardo", Token.BLUE);

    CardCreator cc = new CardCreator();
    Deck gD = new GoldDeck(cc);
    Deck rD = new ResourceDeck(cc);
    Deck sD = new StartDeck(cc);


    @Test
    void testOnFieldResources() {
    }

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

        p.getMyKingdom().updateKingdom(rC.getBack(), pos);

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

        p.getMyKingdom().updateKingdom(gC.getFront(), pos2);

        assertEquals(3, p.getMyKingdom().getPlacedSides().size());
        assertSame(placed, p.getMyKingdom().getPlacedSides().get(0));
        assertSame(rC.getBack(), p.getMyKingdom().getPlacedSides().get(1));
        assertSame(gC.getFront(), p.getMyKingdom().getPlacedSides().get(2));
    }

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

        p.getMyKingdom().updateKingdom(rC.getBack(), pos);

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

        p.getMyKingdom().updateKingdom(gC.getFront(), pos2);

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

        p.getMyKingdom().updateKingdom(rC.getBack(), pos);

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

        p.getMyKingdom().updateKingdom(gC.getFront(), pos2);

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

    public Position createPosition(Direction d, Position p) {
        return switch (d) {
            case Direction.TOPLEFT -> new Position(p.getX() - 1, p.getY() + 1);
            case Direction.TOPRIGHT -> new Position(p.getX() + 1, p.getY() + 1);
            case Direction.BOTTOMLEFT -> new Position(p.getX() - 1, p.getY() - 1);
            case Direction.BOTTOMRIGHT -> new Position(p.getX() + 1, p.getY() - 1);
        };
    }
}