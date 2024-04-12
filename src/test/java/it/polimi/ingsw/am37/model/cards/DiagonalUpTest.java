package it.polimi.ingsw.am37.model.cards;

import it.polimi.ingsw.am37.model.cards.objective.DiagonalUp;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.model.exceptions.AlreadyAssignedException;
import it.polimi.ingsw.am37.model.exceptions.NoCardsException;
import it.polimi.ingsw.am37.model.game.GameModel;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.player.Player;
import it.polimi.ingsw.am37.model.player.Token;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiagonalUpTest {

    Player p = new Player("Ricky", Token.BLUE);
    GameModel g = new GameModel(createListOfPlayer());

    public List<Player> createListOfPlayer () {
        List<Player> lOP = new ArrayList<>();
        lOP.add(p);
        return lOP;
    }

    @Test
    void createAndGetTest() {
        DiagonalUp du = new DiagonalUp(89,2, Resource.ANIMAL, Resource.ANIMAL);

        assertEquals(89, du.getId());
        assertEquals(2, du.getPointsGiven());
        assertSame(Resource.ANIMAL, du.getOtherResource());
        assertSame(Resource.ANIMAL, du.getCardColourThatTriggersCheck());

        System.out.println(du);
    }

    //@Test
    @RepeatedTest(value = 5)
    void calculateNumOfCompletionTest1() throws NoCardsException, AlreadyAssignedException {
        StartCard sC = (StartCard) g.getSDeck().drawCard();

        p.instantiateMyKingdom(sC, sC.getFront());

        ObjectiveCard oC = (ObjectiveCard) g.getODeck().drawCard();
        boolean check = false;
        while (!check) {
            if (oC.getClass().equals(DiagonalUp.class))
                check = true;
            else
                oC = (ObjectiveCard) g.getODeck().drawCard();
        }

        StandardCard rC = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC.getBack().getMainResource().equals(((DiagonalUp) oC).getCardColourThatTriggersCheck())) {
                rC.getBack().placeInPosition(-1, 1);
                sC.getFront().getTL().setLinkedSide(rC.getBack());
                check = true;
            } else
                rC = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC, rC.getBack(), rC.getBack().getPositionInKingdom());

        StandardCard rC2 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC2.getBack().getMainResource().equals(((DiagonalUp) oC).getOtherResource())) {
                rC2.getBack().placeInPosition(-2, 0);
                rC.getBack().getBL().setLinkedSide(rC2.getBack());
                check = true;
            } else
                rC2 = (StandardCard) g.getRDeck().drawCard();
        }
        p.getMyKingdom().updateKingdom(rC2, rC2.getBack(), rC2.getBack().getPositionInKingdom());

        StandardCard rC3 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC3.getBack().getMainResource().equals(((DiagonalUp) oC).getOtherResource())) {
                rC3.getBack().placeInPosition(-3,-1);
                rC2.getBack().getBL().setLinkedSide(rC3.getBack());
                check = true;
            } else
                rC3 = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC3, rC3.getBack(), rC3.getBack().getPositionInKingdom());

        assertSame(1, oC.calculateNumOfCompletion(p.getMyKingdom()));
    }

    @RepeatedTest(value = 5)
    void calculateNumOfCompletionTest2() throws NoCardsException, AlreadyAssignedException {
        StartCard sC = (StartCard) g.getSDeck().drawCard();

        p.instantiateMyKingdom(sC, sC.getFront());

        ObjectiveCard oC = (ObjectiveCard) g.getODeck().drawCard();
        boolean check = false;
        while (!check) {
            if (oC.getClass().equals(DiagonalUp.class))
                check = true;
            else
                oC = (ObjectiveCard) g.getODeck().drawCard();
        }

        StandardCard rC = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC.getBack().getMainResource().equals(((DiagonalUp) oC).getCardColourThatTriggersCheck())) {
                rC.getBack().placeInPosition(-1, 1);
                sC.getFront().getTR().setLinkedSide(rC.getBack());
                check = true;
            } else
                rC = (StandardCard) g.getRDeck().drawCard();
        }

        p.getMyKingdom().updateKingdom(rC, rC.getBack(), rC.getBack().getPositionInKingdom());

        StandardCard rC2 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC2.getBack().getMainResource().equals(((DiagonalUp) oC).getOtherResource())) {
                rC2.getBack().placeInPosition(-2, 0);
                rC.getBack().getBR().setLinkedSide(rC2.getBack());
                check = true;
            } else
                rC2 = (StandardCard) g.getRDeck().drawCard();
        }
        p.getMyKingdom().updateKingdom(rC2, rC2.getBack(), rC2.getBack().getPositionInKingdom());

        StandardCard rC3 = (StandardCard) g.getRDeck().drawCard();
        check = false;
        while (!check) {
            if (rC3.getBack().getMainResource().equals(((DiagonalUp) oC).getOtherResource())) {
                rC3.getBack().placeInPosition(-3,-1);
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
            if (card.getBack().getMainResource().equals(((DiagonalUp) oC).getOtherResource())) {
                card.getBack().placeInPosition(x,y);
                x++;
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