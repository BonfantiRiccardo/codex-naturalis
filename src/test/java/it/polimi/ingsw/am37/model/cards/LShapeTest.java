package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import it.polimi.ingsw.am37.model.sides.Position;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LShapeTest {

    Player p = new Player("Ricky");
    GameController c = new GameController(p, 1);
    GameModel g = new GameModel(createListOfPlayer(), c);

    /**
     * Generates the list of player that fills the game.
     * @return The list of participants.
     */
    public List<Player> createListOfPlayer () {
        List<Player> lOP = new ArrayList<>();
        try {
            p.setToken(Token.BLUE);
        } catch (AlreadyAssignedException e) {
            throw new RuntimeException(e);
        }
        lOP.add(p);
        return lOP;
    }

    /**
     * Tests the creation, get methods and toString method of the L Shape Objective Card.
     */
    @Test
    void createAndGetTest() {
        LShape ls = new LShape(93,3, Resource.ANIMAL, Resource.FUNGI, Direction.BOTTOMLEFT, new Position(-1,-3));

        assertEquals(93, ls.getId());
        assertEquals(3, ls.getPointsGiven());
        assertSame(Resource.ANIMAL, ls.getOtherResource());
        assertSame(Resource.FUNGI, ls.getCardColourThatTriggersCheck());
        assertSame(Direction.BOTTOMLEFT, ls.getDirection());
        assertEquals(new Position(-1,-3), ls.getRequestedPosition());
        System.out.println(ls);
    }

    /**
     * Tests 10 times that the calculateNumOfCompletion(kingdom) method returns the correct number if 3 random cards of
     * the correct colour are placed in a way that fulfills the objective requirement.
     * @throws NoCardsException if the deck is empty.
     * @throws AlreadyAssignedException if the kingdom of the player has already been assigned.
     */
    //@Test
    @RepeatedTest(value = 10)
    void calculateNumOfCompletionTest() throws NoCardsException, AlreadyAssignedException, IncorrectUserActionException {
        StartCard sC = g.getSDeck().drawCard();
        p.setStartCard(sC);
        p.instantiateMyKingdom(sC, sC.getFront());

        ObjectiveCard oC = g.getODeck().drawCard();
        boolean check = false;
        while (!check) {
            if (oC.getClass().equals(LShape.class))
                check = true;
            else
                oC = g.getODeck().drawCard();
        }

        StandardCard rC = g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC.getBack().getMainResource().equals(((LShape) oC).getCardColourThatTriggersCheck())) {
                if (((LShape) oC).getDirection().equals(Direction.BOTTOMLEFT)) {
                    rC.getBack().placeInPosition(-1, 1);
                    sC.getFront().getTL().setLinkedSide(rC.getBack());
                } else {
                    rC.getBack().placeInPosition(1, 1);
                    sC.getFront().getTR().setLinkedSide(rC.getBack());
                }
                check = true;
            } else
                rC = g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC, rC.getBack(), rC.getBack().getPositionInKingdom());

        StandardCard rC2 = g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC2.getBack().getMainResource().equals(((LShape) oC).getOtherResource())) {
                for (Direction d: Direction.values()) {
                    if (((LShape) oC).getDirection().equals(d)) {
                        rC2.getBack().placeInPosition(d.createPosition(rC.getBack().getPositionInKingdom()).getX(), d.createPosition(rC.getBack().getPositionInKingdom()).getY());
                        rC.getBack().getCorners().get(d).setLinkedSide(rC2.getBack());
                    }
                }
                check = true;
            } else if (!g.getRDeck().isEmpty())
                rC2 = g.getRDeck().drawCard();
            else
                rC2 = g.getGDeck().drawCard();
        }
        p.getMyKingdom().updateKingdom(rC2, rC2.getBack(), rC2.getBack().getPositionInKingdom());

        StandardCard support;
        if (!g.getRDeck().isEmpty())
            support = g.getRDeck().drawCard();
        else
            support = g.getGDeck().drawCard();
        for (Direction d: Direction.values()) {
            if (((LShape) oC).getDirection().equals(d)) {
                support.getBack().placeInPosition(d.createPosition(rC2.getBack().getPositionInKingdom()).getX(), d.createPosition(rC2.getBack().getPositionInKingdom()).getY());
                rC2.getBack().getCorners().get(d).setLinkedSide(support.getBack());
            }
        }
        p.getMyKingdom().updateKingdom(support, support.getBack(), support.getBack().getPositionInKingdom());

        StandardCard rC3 = g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC3.getBack().getMainResource().equals(((LShape) oC).getOtherResource())) {
                rC3.getBack().placeInPosition(rC.getBack().getPositionInKingdom().getX() + ((LShape) oC).getRequestedPosition().getX(), rC.getBack().getPositionInKingdom().getY() + ((LShape) oC).getRequestedPosition().getY());
                if (((LShape) oC).getDirection().equals(Direction.TOPLEFT))
                    support.getBack().getTR().setLinkedSide(rC3.getBack());
                else if (((LShape) oC).getDirection().equals(Direction.TOPRIGHT))
                    support.getBack().getTL().setLinkedSide(rC3.getBack());
                else if (((LShape) oC).getDirection().equals(Direction.BOTTOMLEFT))
                    support.getBack().getBR().setLinkedSide(rC3.getBack());
                else if (((LShape) oC).getDirection().equals(Direction.BOTTOMRIGHT))
                    support.getBack().getBL().setLinkedSide(rC3.getBack());

                check = true;
            } else if (!g.getRDeck().isEmpty())
                rC3 = g.getRDeck().drawCard();
            else
                rC3 = g.getGDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC3, rC3.getBack(), rC3.getBack().getPositionInKingdom());

        assertSame(1, oC.calculateNumOfCompletion(p.getMyKingdom()));
    }
}
