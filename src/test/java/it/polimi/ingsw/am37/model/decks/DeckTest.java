package it.polimi.ingsw.am37.model.decks;

import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.cards.objective.ObjectiveCard;
import it.polimi.ingsw.am37.model.cards.placeable.StandardCard;
import it.polimi.ingsw.am37.model.cards.placeable.StartCard;
import it.polimi.ingsw.am37.exceptions.NoCardsException;

import it.polimi.ingsw.am37.model.sides.Back;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private final CardCreator cc = new CardCreator();

    /**
     * Tests the creation and initialization of the resource deck, the drawCard, firstBack and isEmpty method and the
     * correct way to handle the NoCardsException.
     */
    @Test
    public void testResourceDeck() {
        StandardCard testCard;
        ResourceDeck rDeck = new ResourceDeck(cc);
        Back b;

        assertFalse(rDeck.isEmpty());

        for (int i = 0; i < 40; i++) {
            try {
                b = rDeck.firstBack();
                testCard = rDeck.drawCard();

                assertSame(b, testCard.getBack());

                System.out.println(testCard);
            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, rDeck::drawCard);
        assertTrue(rDeck.isEmpty());
    }

    /**
     * Tests the creation and initialization of the objective deck, the drawCard, firstBack and isEmpty method and the
     * correct way to handle the NoCardsException.
     */
    @Test
    public void testObjectiveDeck () {
        ObjectiveCard testCard;
        ObjectiveDeck oDeck = new ObjectiveDeck(cc);
        Back b;

        assertFalse(oDeck.isEmpty());

        for (int i = 0; i < 16; i++) {
            try {
                b = oDeck.firstBack();
                testCard = oDeck.drawCard();

                assertNull(b);

                System.out.println(testCard.toString());
            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, oDeck::drawCard);
        assertTrue(oDeck.isEmpty());
    }

    /**
     * Tests the creation and initialization of the start deck, the drawCard, firstBack and isEmpty method and the
     * correct way to handle the NoCardsException.
     */
    @Test
    public void testStartDeck () {
        StartCard testCard;
        StartDeck sDeck = new StartDeck(cc);
        Back b;

        assertFalse(sDeck.isEmpty());

        for (int i = 0; i < 6; i++) {
            try {
                b = sDeck.firstBack();
                testCard = sDeck.drawCard();

                assertNull(b);

                System.out.println(testCard.toString());
            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, sDeck::drawCard);
        assertTrue(sDeck.isEmpty());
    }

    /**
     * Tests the creation and initialization of the gold deck, the drawCard, firstBack and isEmpty method and the
     * correct way to handle the NoCardsException.
     */
    @Test
    public void testGoldDeck () {
        StandardCard testCard;
        GoldDeck gDeck = new GoldDeck(cc);
        Back b;

        assertFalse(gDeck.isEmpty());

        for (int i = 0; i < 40; i++) {
            try {
                b = gDeck.firstBack();
                testCard = gDeck.drawCard();

                assertSame(b, testCard.getBack());

                System.out.println(testCard);

            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, gDeck::drawCard);
        assertTrue(gDeck.isEmpty());
    }

}
