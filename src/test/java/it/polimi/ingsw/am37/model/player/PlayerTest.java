package it.polimi.ingsw.am37.model.player;

import it.polimi.ingsw.am37.exceptions.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.*;
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
    Player p = new Player("Riccardo");
    Player p2 = new Player("Alberto");

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
    void getSetTokenTest() throws AlreadyAssignedException {
        assertNull(p.getGame());
        p.setToken(Token.BLUE);
        assertSame(Token.BLUE, p.getToken());
        assertThrows(AlreadyAssignedException.class, () -> p.setToken(Token.RED));
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
        hand.add(g.getGDeck().drawCard());
        hand.add(g.getRDeck().drawCard());
        hand.add(g.getRDeck().drawCard());
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
        StartCard sC = g.getSDeck().drawCard();
        p.setStartCard(sC);
        assertEquals(p.getStartCard(), sC);
        assertThrows(AlreadyAssignedException.class, () -> p.setStartCard(g.getSDeck().drawCard()));
    }

    /**
     * Tests the creation of the myKingdom attribute of the Player.
     * @throws AlreadyAssignedException Tests the Exception after creating the kingdom.
     */
    @Test
    void createKingdomTest() throws NoCardsException, AlreadyAssignedException, IncorrectUserActionException {
        assertNull(p.getMyKingdom());
        GameModel g = new GameModel(createListOfPlayer());
        StartCard sC = g.getSDeck().drawCard();
        p.setStartCard(sC);

        StartCard sC2 = g.getSDeck().drawCard();
        assertThrows(IncorrectUserActionException.class, () -> p.instantiateMyKingdom(sC2, sC2.getFront()));
        assertThrows(IncorrectUserActionException.class, () -> p.instantiateMyKingdom(sC, sC2.getFront()));

        p.instantiateMyKingdom(sC, sC.getFront());
        assertNotNull(p.getMyKingdom());

        assertThrows(AlreadyAssignedException.class, () -> p.instantiateMyKingdom(sC, sC.getFront()));
    }

    /**
     * Tests the set and get of the privateObjective attribute of the Player.
     */
    @Test
    void chooseObjectiveTest() throws NoCardsException, AlreadyAssignedException {
        assertNull(p.getPrivateObjective());
        GameModel g = new GameModel(createListOfPlayer());

        ObjectiveCard[] param = new ObjectiveCard[2];
        param[0] = g.getODeck().drawCard();
        param[1] = g.getODeck().drawCard();

        ObjectiveCard[] param2 = new ObjectiveCard[2];
        param2[0] = g.getODeck().drawCard();
        param2[1] = g.getODeck().drawCard();

        p.setObjectivesToChooseFrom(param);
        assertThrows(AlreadyAssignedException.class, () -> p.setObjectivesToChooseFrom(param2));

        ObjectiveCard other = g.getODeck().drawCard();

        p.setPrivateObjective(param[0]);
        assertNotNull(p.getPrivateObjective());

        assertThrows(AlreadyAssignedException.class, () -> p.setPrivateObjective(other));
    }

    /**
     * Tests all the possible outcomes of the invocation of the drawCardFromDeck method.
     */
    @Test
    void drawCardFromDeckTest() throws NoCardsException, AlreadyAssignedException, IncorrectUserActionException {
        GameModel g = new GameModel(createListOfPlayer());
        List<StandardCard> hand = new ArrayList<>();
        hand.add(g.getGDeck().drawCard());
        hand.add(g.getRDeck().drawCard());
        hand.add(g.getRDeck().drawCard());
        p.setHand(hand);
        assertEquals(3, p.getHand().size());
        assertThrows(IncorrectUserActionException.class, () -> p.drawCardFromDeck(g.getGDeck()));
        assertThrows(IncorrectUserActionException.class, () -> p.drawCardFromDeck(g.getRDeck()));

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        p.drawCardFromDeck(g.getGDeck());
        assertEquals(3, p.getHand().size());

        p.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p.getHand().size());

        p.drawCardFromDeck(g.getRDeck());
        assertEquals(3, p.getHand().size());
    }

    public List<Player> createListOfPlayer2 () {
        List<Player> lOP = new ArrayList<>();
        lOP.add(p2);
        return lOP;
    }

    /**
     * Tests all the possible outcomes of the invocation of the drawCardFromAvailable method.
     */
    @RepeatedTest(value = 10)
    void drawCardFromAvailableTest() throws AlreadyAssignedException, NoCardsException, IncorrectUserActionException {
        GameModel g = new GameModel(createListOfPlayer2());
        g.setAvailableCards();
        g.createHand();

        assertEquals(3, p2.getHand().size());
        assertThrows(IncorrectUserActionException.class, () -> p2.drawCardFromAvailable(g.getAvailableGCards().get(0)));

        p2.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p2.getHand().size());

        assertThrows(IncorrectUserActionException.class, () -> p2.drawCardFromAvailable(g.getGDeck().drawCard()));
        assertEquals(2, p2.getHand().size());

        p2.drawCardFromAvailable(g.getAvailableGCards().get(0));
        assertEquals(3, p2.getHand().size());
        assertEquals(2, g.getAvailableGCards().size());


        p2.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p2.getHand().size());

        assertThrows(IncorrectUserActionException.class, () -> p2.drawCardFromAvailable(g.getRDeck().drawCard()));
        assertEquals(2, p2.getHand().size());

        p2.drawCardFromAvailable(g.getAvailableRCards().get(0));
        assertEquals(3, p2.getHand().size());
        assertEquals(2, g.getAvailableGCards().size());


        while (!g.getGDeck().isEmpty()) g.getGDeck().drawCard();
        assertThrows(NoCardsException.class, () -> g.getGDeck().drawCard());

        p2.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p2.getHand().size());

        p2.drawCardFromAvailable(g.getAvailableGCards().get(0));
        assertEquals(3, p2.getHand().size());
        assertEquals(2, g.getAvailableGCards().size());


        while (!g.getRDeck().isEmpty()) g.getRDeck().drawCard();
        assertThrows(NoCardsException.class, () -> g.getRDeck().drawCard());

        p2.getHand().remove(0);      //EQUIVALENT TO placeCard()
        assertEquals(2, p2.getHand().size());

        p2.drawCardFromAvailable(g.getAvailableGCards().get(0));
        assertEquals(3, p2.getHand().size());
        assertEquals(1, g.getAvailableGCards().size());
    }

    /**
     * Tests all the possible outcomes of the invocation of the placeCard method. It starts with easy assertion that
     * regulate the error messages and then creates a kingdom such that is always possible to place a GoldCard's Front
     * in order to test the isPlacementConditionSatisfied method.
     */
    //@Test
    @RepeatedTest(value = 100)
    void placeCardTest() throws NoCardsException, AlreadyAssignedException, IncorrectUserActionException {
        GameModel g = new GameModel(createListOfPlayer());
        List<StandardCard> hand = new ArrayList<>();
        hand.add(g.getRDeck().drawCard());
        hand.add(g.getRDeck().drawCard());
        hand.add(g.getGDeck().drawCard());
        g.getParticipants().get(0).setHand(hand);

        StartCard sC = g.getSDeck().drawCard();
        g.getParticipants().get(0).setStartCard(sC);
        g.getParticipants().get(0).instantiateMyKingdom(sC, sC.getFront());

        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        StandardCard notInHand = g.getGDeck().drawCard();
        assertThrows(IncorrectUserActionException.class, () -> p.placeCard(notInHand, notInHand.getBack(), new Position(1,1)));
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        assertThrows(IncorrectUserActionException.class, () -> p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(0,0)));
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        assertThrows(IncorrectUserActionException.class, () -> p.placeCard(p.getHand().get(0), p.getHand().get(1).getBack(), new Position(1,1)));
        assertEquals(1, p.getMyKingdom().getPlacedSides().size());

        p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(1,1));
        assertEquals(2, p.getMyKingdom().getPlacedSides().size());

        StandardCard gold = g.getGDeck().drawCard();
        p.getHand().add(gold);
        boolean check = true;
        for (Resource r: gold.getFront().getResourcePlacementCondition().keySet())
            if (p.getMyKingdom().getOnFieldResources().get(r) < gold.getFront().getResourcePlacementCondition().get(r))
                check = false;

        if (check) {
            p.placeCard(gold, gold.getFront(), new Position(-1,1));
            assertEquals(3, p.getMyKingdom().getPlacedSides().size());
        } else {
            assertThrows(IncorrectUserActionException.class, () -> p.placeCard(gold, gold.getFront(), new Position(-1,1)));

            if (gold.getFront().getResourcePlacementCondition().keySet().size() == 1) {
                p.getHand().remove(gold);
                p.getHand().add(g.getRDeck().drawCard());

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
                        p.getHand().add(g.getRDeck().drawCard());
                    } else {
                        p.getHand().remove(0);
                        p.getHand().add(g.getRDeck().drawCard());
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
    void addPointsTest() throws NoCardsException, IncorrectUserActionException, AlreadyAssignedException {
        GameModel g = new GameModel(createListOfPlayer());
        g.giveStartCard();
        g.createHand();
        p.instantiateMyKingdom(p.getStartCard(), p.getStartCard().getFront());

        int x = 1;
        int y = 1;
        while (x < 20) {
            p.placeCard(p.getHand().get(0), p.getHand().get(0).getBack(), new Position(x,y));

            x++;
            y++;

            p.getHand().add(g.getRDeck().drawCard());
        }

        p.getHand().clear();

        p.getHand().add(g.getGDeck().drawCard());
        p.getHand().add(g.getGDeck().drawCard());
        p.getHand().add(g.getGDeck().drawCard());

        int numQ; int numI; int numM ;

        int points = g.getScoreboard().getParticipantsPoints().get(p);
        int i = 0;
        StandardCard toPlace = p.getHand().get(i);

        if (toPlace.getFront().isPlacementConditionSatisfied(p.getMyKingdom().getOnFieldResources())) {
            p.placeCard(p.getHand().get(i), p.getHand().get(i).getFront(), new Position(x,y));
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + toPlace.getFront().getPointsGivenOnPlacement());
        } else {
            int finalX = x; int finalY = y; int finalI = i;
            assertThrows(IncorrectUserActionException.class, () -> p.placeCard(p.getHand().get(finalI), p.getHand().get(finalI).getFront(), new Position(finalX,finalY)));
            i++;
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points);
        }

        numQ = p.getMyKingdom().getOnFieldResources().get(Resource.QUILL); numI = p.getMyKingdom().getOnFieldResources().get(Resource.INKWELL);  numM = p.getMyKingdom().getOnFieldResources().get(Resource.MANUSCRIPT);

        if (i == 0) {
            points = g.getScoreboard().getParticipantsPoints().get(p);
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
        if (toPlace.getFront().isPlacementConditionSatisfied(p.getMyKingdom().getOnFieldResources())) {
            p.placeCard(p.getHand().get(i), p.getHand().get(i).getFront(), new Position(x,y));
        } else {
            int finalX = x; int finalY = y; int finalI = i;
            assertThrows(IncorrectUserActionException.class, () -> p.placeCard(p.getHand().get(finalI), p.getHand().get(finalI).getFront(), new Position(finalX,finalY)));
        }

        int j = 0;
        if (toPlace.getFront().getIsPlaced()) {
            if (toPlace.getFront().getBonus() == Bonus.QUILL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + (numQ+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.INKWELL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + (numI+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.MANUSCRIPT)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + (numM+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + toPlace.getFront().getPointsGivenOnPlacement());
        } else {
            j++;
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points);
        }

        numQ = p.getMyKingdom().getOnFieldResources().get(Resource.QUILL); numI = p.getMyKingdom().getOnFieldResources().get(Resource.INKWELL);  numM = p.getMyKingdom().getOnFieldResources().get(Resource.MANUSCRIPT);

        if (j == 0) {
            points = g.getScoreboard().getParticipantsPoints().get(p);
            x = -1;
            y = 1;
        }

        toPlace = p.getHand().get(i + j);
        if (toPlace.getFront().isPlacementConditionSatisfied(p.getMyKingdom().getOnFieldResources())) {
            p.placeCard(p.getHand().get(i + j), p.getHand().get(i + j).getFront(), new Position(x,y));
        } else {
            int finalX = x; int finalY = y; int finalI = i; int finalJ = j;
            assertThrows(IncorrectUserActionException.class, () -> p.placeCard(p.getHand().get(finalI + finalJ), p.getHand().get(finalI + finalJ).getFront(), new Position(finalX,finalY)));
        }

        if (toPlace.getFront().getIsPlaced()) {
            if (toPlace.getFront().getBonus() == Bonus.QUILL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + (numQ+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.INKWELL)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + (numI+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else if (toPlace.getFront().getBonus() == Bonus.MANUSCRIPT)
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + (numM+1) * toPlace.getFront().getPointsGivenOnPlacement());
            else
                assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points + toPlace.getFront().getPointsGivenOnPlacement());
        } else {
            assertEquals(g.getScoreboard().getParticipantsPoints().get(p), points);
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