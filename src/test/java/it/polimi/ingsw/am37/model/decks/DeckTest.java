package it.polimi.ingsw.am37.model.decks;

import it.polimi.ingsw.am37.model.cards.*;
import it.polimi.ingsw.am37.model.exceptions.NoCardsException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    Card testCard;
    private final CardCreator cc = new CardCreator();
    @Test
    public void testResourceDeck() {

        ResourceDeck rDeck = new ResourceDeck(cc);

        assertFalse(rDeck.isEmpty());

        for (int i = 0; i < 40; i++) {
            try {
                testCard = rDeck.drawCard();
                System.out.println(testCard.toString());
            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, rDeck::drawCard);
        assertTrue(rDeck.isEmpty());
    }

    @Test
    public void testObjectiveDeck () {
        ObjectiveDeck oDeck = new ObjectiveDeck(cc);

        assertFalse(oDeck.isEmpty());

        for (int i = 0; i < 16; i++) {
            try {
                testCard = oDeck.drawCard();
                System.out.println(testCard.toString());
            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, oDeck::drawCard);
        assertTrue(oDeck.isEmpty());
    }

    @Test
    public void testStartDeck () {
        StartDeck sDeck = new StartDeck(cc);

        assertFalse(sDeck.isEmpty());

        for (int i = 0; i < 6; i++) {
            try {
                testCard = sDeck.drawCard();
                System.out.println(testCard.toString());
            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, sDeck::drawCard);
        assertTrue(sDeck.isEmpty());
    }

    @Test
    public void testGoldDeck () {
        GoldDeck gDeck = new GoldDeck(cc);

        assertFalse(gDeck.isEmpty());

        for (int i = 0; i < 40; i++) {
            try {
                testCard = gDeck.drawCard();
                System.out.println(testCard.toString());

            } catch (NoCardsException e) {
                throw new RuntimeException(e);
            }
        }

        assertThrows(NoCardsException.class, gDeck::drawCard);
        assertTrue(gDeck.isEmpty());
    }

}
