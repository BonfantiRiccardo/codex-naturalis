package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.*;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.model.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.game.GameModel;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.*;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class DiagonalDownTest {
    Player p = new Player("Ricky");
    GameModel g = new GameModel(createListOfPlayer());

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
     * Tests the creation, get methods and toString method of the Diagonal Down Objective Card.
     */
    @Test
    void createAndGetTest() {
        DiagonalDown dd = new DiagonalDown(88,2, Resource.PLANT, Resource.PLANT);

        assertEquals(88, dd.getId());
        assertEquals(2, dd.getPointsGiven());
        assertSame(Resource.PLANT, dd.getOtherResource());
        assertSame(Resource.PLANT, dd.getCardColourThatTriggersCheck());

        System.out.println(dd);
    }

    /**
     * Tests 5 times that the calculateNumOfCompletion(kingdom) method returns the correct number if 3 random cards of
     * the correct colour are placed in a way that fulfills the objective requirement.
     * @throws NoCardsException if the deck is empty.
     * @throws AlreadyAssignedException if the kingdom of the player has already been assigned.
     */
    //@Test
    @RepeatedTest(value = 5)
    void calculateNumOfCompletionTest1() throws NoCardsException, AlreadyAssignedException {
        StartCard sC = (StartCard) g.getSDeck().drawCard();

        p.instantiateMyKingdom(sC, sC.getFront());

        ObjectiveCard oC = (ObjectiveCard) g.getODeck().drawCard();
        boolean check = false;
        while (!check) {
            if (oC.getClass().equals(DiagonalDown.class))
                check = true;
            else
                oC = (ObjectiveCard) g.getODeck().drawCard();
        }

        StandardCard rC = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC.getBack().getMainResource().equals(((DiagonalDown) oC).getCardColourThatTriggersCheck())) {
                 rC.getBack().placeInPosition(1, 1);
                 sC.getFront().getTR().setLinkedSide(rC.getBack());
                check = true;
            } else
                rC = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC, rC.getBack(), rC.getBack().getPositionInKingdom());

        StandardCard rC2 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC2.getBack().getMainResource().equals(((DiagonalDown) oC).getOtherResource())) {
                rC2.getBack().placeInPosition(2, 0);
                rC.getBack().getBR().setLinkedSide(rC2.getBack());
                check = true;
            } else
                rC2 = (StandardCard) g.getRDeck().drawCard();
        }
        p.getMyKingdom().updateKingdom(rC2, rC2.getBack(), rC2.getBack().getPositionInKingdom());

        StandardCard rC3 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC3.getBack().getMainResource().equals(((DiagonalDown) oC).getOtherResource())) {
                rC3.getBack().placeInPosition(3,-1);
                rC2.getBack().getBR().setLinkedSide(rC3.getBack());
                check = true;
            } else
                rC3 = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC3, rC3.getBack(), rC3.getBack().getPositionInKingdom());

        assertSame(1, oC.calculateNumOfCompletion(p.getMyKingdom()));

    }

    /**
     * Tests 5 times that the calculateNumOfCompletion(kingdom) method returns the correct number if 6 random cards of
     * the correct colour are placed in a way that fulfills the objective requirement twice.
     * @throws NoCardsException if the deck is empty.
     * @throws AlreadyAssignedException if the kingdom of the player has already been assigned.
     */
    @RepeatedTest(value = 5)
    //@Test
    void calculateNumOfCompletionTest2() throws NoCardsException, AlreadyAssignedException {
        StartCard sC = (StartCard) g.getSDeck().drawCard();

        p.instantiateMyKingdom(sC, sC.getFront());

        ObjectiveCard oC = (ObjectiveCard) g.getODeck().drawCard();
        boolean check = false;
        while (!check) {
            if (oC.getClass().equals(DiagonalDown.class))
                check = true;
            else
                oC = (ObjectiveCard) g.getODeck().drawCard();
        }

        StandardCard rC = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC.getBack().getMainResource().equals(((DiagonalDown) oC).getCardColourThatTriggersCheck())) {
                rC.getBack().placeInPosition(1, 1);
                sC.getFront().getTR().setLinkedSide(rC.getBack());
                check = true;
            } else
                rC = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC, rC.getBack(), rC.getBack().getPositionInKingdom());

        StandardCard rC2 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC2.getBack().getMainResource().equals(((DiagonalDown) oC).getOtherResource())) {
                rC2.getBack().placeInPosition(2, 0);
                rC.getBack().getBR().setLinkedSide(rC2.getBack());
                check = true;
            } else
                rC2 = (StandardCard) g.getRDeck().drawCard();
        }
        p.getMyKingdom().updateKingdom(rC2, rC2.getBack(), rC2.getBack().getPositionInKingdom());

        StandardCard rC3 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC3.getBack().getMainResource().equals(((DiagonalDown) oC).getOtherResource())) {
                rC3.getBack().placeInPosition(3,-1);
                rC2.getBack().getBR().setLinkedSide(rC3.getBack());
                check = true;
            } else
                rC3 = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC3, rC3.getBack(), rC3.getBack().getPositionInKingdom());

        int done = 0;
        StandardCard card = (StandardCard) g.getRDeck().drawCard();
        StandardCard previous = rC;
        int x = 0;      int y = 2;
        while (done < 3) {
            if (card.getBack().getMainResource().equals(((DiagonalDown) oC).getOtherResource())) {
                card.getBack().placeInPosition(x,y);
                x--;
                y++;
                previous.getBack().getTL().setLinkedSide(card.getBack());
                previous = card;
                done++;
                p.getMyKingdom().updateKingdom(card, card.getBack(), card.getBack().getPositionInKingdom());
            }

            card = (StandardCard) g.getRDeck().drawCard();
        }

        assertSame(2, oC.calculateNumOfCompletion(p.getMyKingdom()));
    }
}
