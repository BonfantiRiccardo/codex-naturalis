package it.polimi.ingsw.am37.model.player;

//import it.polimi.ingsw.am37.controller.GameController;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
import it.polimi.ingsw.am37.model.exceptions.*;
import it.polimi.ingsw.am37.model.game.*;
import it.polimi.ingsw.am37.model.game.Resource;
import it.polimi.ingsw.am37.model.sides.Bonus;
import it.polimi.ingsw.am37.model.sides.Position;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Testing player attributes and methods")
class PlayerTest {
    Player p = new Player("Riccardo", Token.BLUE);

    /**
     * Testing the get method for the nickname attribute.
     */
    @Test
    @DisplayName("Testing nickname")
    void getNicknameTest() {
        assertSame("Riccardo", p.getNickname());
    }

    /**
     * Testing the get and set method for the token attribute.
     */
    @Test
    void getTokenTest() {
        assertSame(Token.BLUE, p.getToken());
    }

    /**
     * Testing the get and set method for the game attribute.
     */
    @Test
    void setGetGameTest() {
        assertNull(p.getGame());
        GameModel g = new GameModel(createListOfPlayer());
        assertEquals(p.getGame(), g);
        assertThrows(AlreadyAssignedException.class, () -> p.setGame(g));
    }

    /**
     * Creates a list of exactly one player.
     */
    public List<Player> createListOfPlayer () {
        List<Player> lOP = new ArrayList<>();
        lOP.add(p);
        return lOP;
    }

    /**
     * Tests the creation of the hand attribute of the Player.
     * @throws AlreadyAssignedException Tests the Exception after creating the hand.
     */
    @Test
    void setGetHandTest() throws NoCardsException, AlreadyAssignedException {
        assertNull(p.getHand());
        GameModel g = new GameModel(createListOfPlayer());
        List<StandardCard> hand = new ArrayList<>();
        hand.add((StandardCard) g.getGDeck().drawCard());
        hand.add((StandardCard) g.getRDeck().drawCard());
        hand.add((StandardCard) g.getRDeck().drawCard());
        p.setHand(hand);
        assertEquals(p.getHand(), hand);
        assertThrows(AlreadyAssignedException.class, () -> p.setHand(hand));
    }

    /**
     * Tests the set and get of the startCard attribute of the Player.
     * @throws AlreadyAssignedException Tests the Exception after assigning the card.
     */
    @Test
    void setStartCardTest() throws NoCardsException, AlreadyAssignedException {
        assertNull(p.getStartCard());
        GameModel g = new GameModel(createListOfPlayer());
        StartCard sC = (StartCard) g.getSDeck().drawCard();
        p.setStartCard(sC);
        assertEquals(p.getStartCard(), sC);
        assertThrows(AlreadyAssignedException.class, () -> p.setStartCard((StartCard) g.getSDeck().drawCard()));
    }

    /**
     * Tests the creation of the myKingdom attribute of the Player.
     * @throws AlreadyAssignedException Tests the Exception after creating the kingdom.
     */
    @Test
    void chooseStartCardSideTest() throws NoCardsException, AlreadyAssignedException {
        assertNull(p.getMyKingdom());
        GameModel g = new GameModel(createListOfPlayer());
        StartCard sC = (StartCard) g.getSDeck().drawCard();
        p.setStartCard(sC);

        p.chooseStartCardSide();
        assertNotNull(p.getMyKingdom());

        assertThrows(AlreadyAssignedException.class, () -> p.instantiateMyKingdom(sC, sC.getFront()));
    }

    /**
     * Tests the set and get of the privateObjective attribute of the Player.
     */
    @Test
    void chooseObjectiveTest() throws NoCardsException {
        assertNull(p.getPrivateObjective());
        GameModel g = new GameModel(createListOfPlayer());

        ObjectiveCard[] param = new ObjectiveCard[2];
        param[0] = (ObjectiveCard) g.getODeck().drawCard();
        param[1] = (ObjectiveCard) g.getODeck().drawCard();

        p.chooseObjective(param);
        assertNotNull(p.getPrivateObjective());
    }

    /**
     * Tests all the possible outcomes of the invocation of the drawCardFromDeck method.
     */
    @Test
    void drawCardFromDeckTest() throws NoCardsException, AlreadyAssignedException {
        GameModel g = new GameModel(createListOfPlayer());
        List<StandardCard> hand = new ArrayList<>();
        hand.add((StandardCard) g.getGDeck().drawCard());
        hand.add((StandardCard) g.getRDeck().drawCard());
        hand.add((StandardCard) g.getRDeck().drawCard());
        p.setHand(hand);
        assertEquals(3, p.getHand().size());
        System.out.println("The following text should be printed in the next line: \"You cannot draw, you already have 3 Cards in your hand\"");
        p.drawCardFromDeck(g.getGDeck());

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        p.drawCardFromDeck(g.getGDeck());
        assertEquals(3, p.getHand().size());

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        p.drawCardFromDeck(g.getRDeck());
        assertEquals(3, p.getHand().size());
    }

    /**
     * Tests all the possible outcomes of the invocation of the drawCardFromAvailable method.
     */
    @Test
    void drawCardFromAvailableTest() throws AlreadyAssignedException, NoCardsException, InterruptedException {
        GameModel g = new GameModel(createListOfPlayer());
        g.preparationPhase();
        assertEquals(3, p.getHand().size());
        System.out.println("The following text should be printed in the next line: \"You cannot draw, you already have 3 Cards in your hand\"");
        p.drawCardFromAvailable(g.getAvailableGCards().get(0));

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        System.out.println("The following text should be printed in the next line: \"The Card you want to draw is not available\"");
        p.drawCardFromAvailable((StandardCard) g.getGDeck().drawCard());
        assertEquals(2, p.getHand().size());

        p.drawCardFromAvailable(g.getAvailableGCards().get(0));
        assertEquals(3, p.getHand().size());
        assertEquals(2, g.getAvailableGCards().size());


        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        System.out.println("The following text should be printed in the next line: \"The Card you want to draw is not available\"");
        p.drawCardFromAvailable((StandardCard) g.getRDeck().drawCard());
        assertEquals(2, p.getHand().size());

        p.drawCardFromAvailable(g.getAvailableRCards().get(0));
        assertEquals(3, p.getHand().size());
        assertEquals(2, g.getAvailableGCards().size());


        while (!g.getGDeck().isEmpty()) g.getGDeck().drawCard();
        assertThrows(NoCardsException.class, () -> g.getGDeck().drawCard());

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        p.drawCardFromAvailable(g.getAvailableGCards().get(0));
        assertEquals(3, p.getHand().size());
        assertEquals(2, g.getAvailableGCards().size());


        while (!g.getRDeck().isEmpty()) g.getRDeck().drawCard();
        assertThrows(NoCardsException.class, () -> g.getRDeck().drawCard());

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        p.drawCardFromAvailable(g.getAvailableGCards().get(0));
        assertEquals(3, p.getHand().size());
        assertEquals(1, g.getAvailableGCards().size());
    }

    /**
     * Tests all the possible outcomes of the invocation of the placeCard method. It starts with easy assertion that
     * regulate the error messages and then creates a kingdom such that is always possible to place a GoldCard's Front
     * in order to test the isPlacementConditionSatisfied method.
     */
    //@Test
    @RepeatedTest(value = 100)
    void placeCardTest() throws NoCardsException, AlreadyAssignedException, InterruptedException {
        GameModel g = new GameModel(createListOfPlayer());
        g.preparationPhase();
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        StandardCard notInHand = (StandardCard) g.getGDeck().drawCard();
        System.out.println("The following text should be printed in the next line: \"You do not possess this Card, you cannot place it.\"");
        p.placeCard(notInHand, notInHand.getBack(), new Position(1,1));
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        System.out.println("The following text should be printed in the next line: \"You cannot place the Card in this position.\"");
        p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(0,0));
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        System.out.println("The following text should be printed in the next line: \"The Side given as a parameter does not belong to the Card given as a parameter, you cannot place it\"");
        p.placeCard(p.getHand().get(0), p.getHand().get(1).getBack(), new Position(1,1));
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(1,1));
        assertEquals(2, p.getMyKingdom().getPlacedSides().size());

        StandardCard gold = (StandardCard) g.getGDeck().drawCard();
        p.getHand().add(gold);
        boolean check = true;
        for (Resource r: gold.getFront().getResourcePlacementCondition().keySet())
            if (p.getMyKingdom().getOnFieldResources().get(r) < gold.getFront().getResourcePlacementCondition().get(r))
                check = false;

        if (check) {
            p.placeCard(gold, gold.getFront(), new Position(-1,1));
            assertEquals(3, p.getMyKingdom().getPlacedSides().size());
        } else {
            System.out.println("The following text should be printed in the next line: \"You don't have enough resources to satisfy the placement condition for the front of this card, you cannot place it.\"");
            p.placeCard(gold, gold.getFront(), new Position(-1,1));

            if (gold.getFront().getResourcePlacementCondition().keySet().size() == 1) {
                p.getHand().remove(gold);
                p.getHand().add((StandardCard) g.getRDeck().drawCard());

                Resource goal = null;
                for (Resource r: gold.getFront().getResourcePlacementCondition().keySet())
                    goal =  r;
                int numRes = p.getMyKingdom().getOnFieldResources().get(goal);
                int x = 2;
                int y = 2;
                while (numRes < gold.getFront().getResourcePlacementCondition().get(goal)) {
                    if (p.getHand().get(0).getBack().getMainResource().equals(goal)) {
                        p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(x,y));
                        x++;
                        y++;
                        numRes++;
                        p.getHand().add((StandardCard) g.getRDeck().drawCard());
                    } else {
                        p.getHand().remove(0);
                        p.getHand().add((StandardCard) g.getRDeck().drawCard());
                    }
                }

                p.getHand().remove(0);
                p.getHand().add(gold);

                assertTrue(gold.getFront().isPlacementConditionSatisfied(p.getMyKingdom().getOnFieldResources()));
                p.placeCard(gold, gold.getFront(), new Position(-1,1));
                assertEquals(x+1, p.getMyKingdom().getPlacedSides().size());
            }
        }

    }

    /**
     * Tests all the possible outcomes of the invocation of the addPoints method. It creates a kingdom such that is
     * possible to place the Front of a GoldCard and assert if the points were added correctly.
     */
    //@Test
    @RepeatedTest(value = 100)
    void addPointsTest() throws NoCardsException, AlreadyAssignedException, InterruptedException {
        GameModel g = new GameModel(createListOfPlayer());
        g.preparationPhase();

        int x = 1;
        int y = 1;
        while (x < 20) {
            p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(x,y));

            x++;
            y++;

            p.getHand().add((StandardCard) g.getRDeck().drawCard());
        }

        p.getHand().clear();

        p.getHand().add((StandardCard) g.getGDeck().drawCard());
        p.getHand().add((StandardCard) g.getGDeck().drawCard());
        p.getHand().add((StandardCard) g.getGDeck().drawCard());

        int numQ; int numI; int numM ;

        int points = g.getScoreboard().getParticipantsPoints().get(p.getToken());
        int i = 0;
        StandardCard toPlace = p.getHand().get(i);
        p.placeCard(p.getHand().get(i), p.getHand().get(i).getFront(), new Position(x,y));
        if (toPlace.getFront().isPlacementConditionSatisfied(p.getMyKingdom().getOnFieldResources())) {
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + toPlace.getFront().getPointsGivenOnPlacement());
        } else {
            i++;
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points);
        }

        numQ = p.getMyKingdom().getOnFieldResources().get(Resource.QUILL); numI = p.getMyKingdom().getOnFieldResources().get(Resource.INKWELL);  numM = p.getMyKingdom().getOnFieldResources().get(Resource.MANUSCRIPT);

        if (i == 0) {
            points = g.getScoreboard().getParticipantsPoints().get(p.getToken());
            if (toPlace.getFront().getTR().getVisibility()) {
                x++;
                y++;
            } else if (toPlace.getFront().getTL().getVisibility()) {
                x--;
                y++;
            } else if (toPlace.getFront().getBR().getVisibility()) {
                x++;
                y--;
            } else {
                x--;
                x--;
            }
        }

        toPlace = p.getHand().get(i);
        p.placeCard(p.getHand().get(i), p.getHand().get(i).getFront(), new Position(x,y));

        int j = 0;
        if (toPlace.getFront().getIsPlaced()) {
            if (toPlace.getFront().getBonus() == Bonus.QUILL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + (numQ+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.INKWELL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + (numI+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.MANUSCRIPT)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + (numM+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + toPlace.getFront().getPointsGivenOnPlacement());
        } else {
            j++;
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points);
        }

        numQ = p.getMyKingdom().getOnFieldResources().get(Resource.QUILL); numI = p.getMyKingdom().getOnFieldResources().get(Resource.INKWELL);  numM = p.getMyKingdom().getOnFieldResources().get(Resource.MANUSCRIPT);

        if (j == 0) {
            points = g.getScoreboard().getParticipantsPoints().get(p.getToken());
            x = -1;
            y = 1;
        }

        toPlace = p.getHand().get(i + j);
        p.placeCard(p.getHand().get(i + j), p.getHand().get(i + j).getFront(), new Position(x,y));
        if (toPlace.getFront().getIsPlaced()) {
            if (toPlace.getFront().getBonus() == Bonus.QUILL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + (numQ+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.INKWELL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + (numI+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.MANUSCRIPT)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + (numM+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points + toPlace.getFront().getPointsGivenOnPlacement());
        } else {
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p.getToken()), points);
        }

    }

    /**
     * Tests the get and set methods for the isDisconnected attribute.
     */
    @Test
    void testConnection() {
        assertFalse(p.isDisconnected());
        p.setDisconnected(true);
        assertTrue(p.isDisconnected());
        p.setDisconnected(false);
        assertFalse(p.isDisconnected());
    }

}